package org.niwri.backgroundchanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<BackgroundImage> backgroundList;
    private RecyclerView recyclerView;

    private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundList = new ArrayList<BackgroundImage>();
        recyclerView = findViewById(R.id.recyclerView);

        setBackgroundInfo();
        setAdapter();

        setButtons();
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

                //Appends BackgroundImage generated from information and image file
                backgroundList.add(new BackgroundImage(
                        name,
                        BitmapFactory.decodeFile(directory.getPath() + "/image.png"),
                        new Date(dayEnable, hour, minute, 0),
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
        ImageButton addBackground = findViewById(R.id.btnAddBackground);

        addBackground.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, BackgroundAddActivity.class);
                intent.putExtra("loadData", false);
                startActivity(new Intent( MainActivity.this, BackgroundAddActivity.class));
            }
        });
    }
}
