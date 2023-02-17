package org.niwri.backgroundchanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.niwri.backgroundchanger.BackgroundImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
/*
        Date thisDate = new Date( "Mon", 13, 14, 32);
        TextView name = findViewById(R.id.backgroundName);
        TextView date = findViewById(R.id.backgroundDate);
        TextView time = findViewById(R.id.backgroundTime);
        ImageView img = findViewById(R.id.backgroundImage);
        Switch onOff = findViewById(R.id.backgroundEnable);

        name.setText("Hu Tao");
        date.setText("Every " + thisDate.getDay());
        time.setText(thisDate.getHour() + ":" + thisDate.getMinute());
        img.setImageBitmap(assetsToBitmap("hutao.png"));*/

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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setBackgroundInfo() {
        backgroundList.add(new BackgroundImage("Hu Tao", assetsToBitmap("hutao.png"), new Date( "Mon", 13, 14, 32)));
    }
}
