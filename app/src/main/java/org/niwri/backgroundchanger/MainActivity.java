package org.niwri.backgroundchanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static ArrayList<BackgroundImage> backgroundList;
    private RecyclerView recyclerView;
    public static ArrayList<RecyclerAdapter.MyViewHolder> holderList;

    private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private  Intent serviceIntent;
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundList = new ArrayList<BackgroundImage>();
        holderList = new ArrayList<RecyclerAdapter.MyViewHolder>();
        recyclerView = findViewById(R.id.recyclerView);

        setBackgroundInfo();
        setAdapter();

        setButtons();

        serviceIntent = new Intent(this, BackgroundService.class);
        if(isMyServiceRunning(BackgroundService.class) == false) {
            Log.i("Service", "Started");
            startService(serviceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    //Sets up RecyclerView for list of background alarms
    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(backgroundList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        SpacingItemDecorator itemSpacing = new SpacingItemDecorator(50);
        recyclerView.addItemDecoration(itemSpacing);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    //Sets up an array list of background alarms for the RecyclerView
    private void setBackgroundInfo() {
        InputStream ims;
        File parentDirectory = new File(getFilesDir(), "background-alarms");
        if(!parentDirectory.exists())
            parentDirectory.mkdir();
        File[] backgroundDirectories = parentDirectory.listFiles();

        //Iterates through all files in the parentDirectory
        for(File directory : backgroundDirectories) {

            try {
                File infoFile = new File(directory.getPath() + "/information.txt");

                FileInputStream information = new FileInputStream(infoFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(information));
                String name = reader.readLine().replace("Name:", "");
                boolean[] dayEnable = new boolean[7];

                String[] dayString = reader.readLine().replace("Days:", "").split(" ");
                for(int i = 0; i < dayString.length; i++) {
                    dayEnable[i] = false;
                    if(dayString[i].equals("true"))
                       dayEnable[i] = true;
                }
                int hour = Integer.parseInt(reader.readLine().replace("Hour:", ""));
                int minute = Integer.parseInt(reader.readLine().replace("Minute:", ""));
                boolean enable = reader.readLine().replace("Enable:", "").equals("true");
                boolean meridiemPM = reader.readLine().replace("Meridiem:", "").equals("true");

                //Appends BackgroundImage generated from information and image file
                backgroundList.add(new BackgroundImage(
                        name,
                        BitmapFactory.decodeFile(directory.getPath() + "/image.png"),
                        new BackgroundDate(dayEnable, hour, minute, 0, meridiemPM),
                        enable,
                        directory.getPath()));
                information.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //Sets up the Add Background button
    private void setButtons() {
        ImageButton btnAddBackground = findViewById(R.id.btnAddBackground);
        ImageButton btnDeleteBackground = findViewById(R.id.btnDeleteBackground);
        Button btnDeleteItems = findViewById(R.id.btnDeleteItems);

        btnAddBackground.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, BackgroundAddActivity.class);
                intent.putExtra("loadData", false);
                startActivity(new Intent( MainActivity.this, BackgroundAddActivity.class));
            }
        });

        btnDeleteBackground.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for(RecyclerAdapter.MyViewHolder holder : holderList) {
                    boolean enable = holder.onOff.getVisibility() == View.VISIBLE;
                    holder.onOff.setVisibility(enable ? View.GONE : View.VISIBLE);
                    holder.radioDelete.setVisibility(enable ? View.VISIBLE : View.GONE);
                    btnDeleteItems.setVisibility(enable ? View.VISIBLE : View.GONE);
                }
            }
        });

        btnDeleteItems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for(RecyclerAdapter.MyViewHolder holder: holderList) {
                    if(!holder.radioDelete.isChecked()) {
                        System.out.println("Skipped"); // Temp
                        continue;
                    }
                    File directoryToDelete = new File(holder.directoryPath);
                    deleteFile(directoryToDelete); //Fix to delete file
                    System.out.println(directoryToDelete.exists()); // Temp
                    System.out.println(directoryToDelete.getPath()); // Temp



                };

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    public static void addHolder(RecyclerAdapter.MyViewHolder holder) {
        holderList.add(holder);
    }

    void deleteFile(File file) {
        if(file.isDirectory())
            for(File child : file.listFiles())
                deleteFile(child);

        file.delete();

    }

    public static ArrayList<BackgroundImage> getBackgroundList() { return backgroundList; }
}
