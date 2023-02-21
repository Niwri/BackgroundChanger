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

    Bitmap assetsToBitmap(String fileName) {
        try {
            return BitmapFactory.decodeStream(getAssets().open(fileName));
        } catch (Exception e) {
            return null;
        }
    }

    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(backgroundList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        SpacingItemDecorator itemSpacing = new SpacingItemDecorator(50);
        recyclerView.addItemDecoration(itemSpacing);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setBackgroundInfo() {
        InputStream ims;
        File parentDirectory = new File(getFilesDir(), "background-alarms");
        if(!parentDirectory.exists())
            parentDirectory.mkdir();
        File[] backgroundDirectories = parentDirectory.listFiles();

        for(File directory : backgroundDirectories) {

            try {
                FileInputStream information = new FileInputStream(new File(directory.getPath() + "\\information.txt"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(information));
                String name = reader.readLine();
                String day = reader.readLine();
                int hour = Integer.parseInt(reader.readLine());
                int minute = Integer.parseInt(reader.readLine());
                //Name

                backgroundList.add(new BackgroundImage(name, BitmapFactory.decodeFile(directory.getPath() + "\\image.png"), new Date(day, hour, minute, 0)));
                information.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void setButtons() {
        ImageButton addBackground = findViewById(R.id.addBackground);

        addBackground.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent( MainActivity.this, BackgroundAddActivity.class));
            }
        });
    }
}
