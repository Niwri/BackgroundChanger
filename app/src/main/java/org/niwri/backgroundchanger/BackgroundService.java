package org.niwri.backgroundchanger;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import java.util.ArrayList;

public class BackgroundService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandler.postDelayed(mRunnable, 10000);
        return START_STICKY;
    }

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<BackgroundImage> backgroundList = MainActivity.getBackgroundList();

            for(BackgroundImage background : backgroundList)
                checkBackground(background);

            mHandler.postDelayed(this, 10000);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mRunnable);
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

        // Set the wallpaper from a drawable resource
        try {
            wallpaperManager.setBitmap(background.getBackgroundBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
