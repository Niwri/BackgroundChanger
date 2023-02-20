package org.niwri.backgroundchanger;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class BackgroundAddActivity extends AppCompatActivity {
    private String backgroundName;
    private String day;
    private int hour, minute;
    private File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_background);

        setButtons();
    }

    private void saveToAssets() {


    }

    private void setButtons() {
        Button btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);

        Button[] btnDays = {findViewById(R.id.btnSunday), findViewById(R.id.btnMonday), findViewById(R.id.btnTuesday),
                findViewById(R.id.btnTuesday), findViewById(R.id.btnWednesday), findViewById(R.id.btnThursday),
                findViewById(R.id.btnFriday), findViewById(R.id.btnSaturday)};

        for(Button btnDay : btnDays) {
            btnDay.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      btnDay.setTextColor(btnDay.getCurrentTextColor() == Color.RED ? Color.BLUE : Color.RED);
                  }
            });
          };


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BackgroundAddActivity.this, MainActivity.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToAssets();
                startActivity(new Intent(BackgroundAddActivity.this, MainActivity.class));
            }
        });


    }
}
