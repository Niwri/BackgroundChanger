package org.niwri.backgroundchanger;

import static android.os.SystemClock.elapsedRealtime;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import java.util.ArrayList;

public class BackgroundService extends Service {

    boolean pauseCheck;
    int currentMin;

    private Handler mHandler = new Handler();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMyOwnForeground();

        pauseCheck = false;
        return START_STICKY;
    }

    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        mHandler.postDelayed(mRunnable, 10000);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<BackgroundImage> backgroundList = MainActivity.getBackgroundList();
            Log.i("Running", "Checking Background...");
            for(BackgroundImage background : backgroundList)
                checkBackground(background);

            if(pauseCheck) {
                while(currentMin == Calendar.getInstance().get(Calendar.MINUTE));
                pauseCheck = false;
            }

            mHandler.postDelayed(this, 10000);

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mRunnable);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void checkBackground(BackgroundImage background) {
        Calendar currentTime = Calendar.getInstance();

        int backgroundHour = background.getBackgroundDate().getHour();

        int meridiem = background.getBackgroundDate().isPM() ? 1 : 0;

        if(backgroundHour == 12)
            backgroundHour = 0;

        int backgroundMinute = background.getBackgroundDate().getMinute();
        boolean[] days = background.getBackgroundDate().getDay();

        if(background.isEnabled() == false)
            return;

        if(!days[currentTime.get(Calendar.DAY_OF_WEEK)-1])
            return;

        if(currentTime.get(Calendar.AM_PM) != meridiem)
            return;

        if(currentTime.get(Calendar.HOUR) != backgroundHour)
            return;

        if(currentTime.get(Calendar.MINUTE) != backgroundMinute)
            return;


        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        currentMin = currentTime.get(Calendar.MINUTE);
        // Set the wallpaper from a drawable resource
        try {
            wallpaperManager.setBitmap(background.getBackgroundBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Poll until next minute arrives
        pauseCheck = true;
    }
}
