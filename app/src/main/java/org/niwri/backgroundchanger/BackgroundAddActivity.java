package org.niwri.backgroundchanger;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BackgroundAddActivity extends AppCompatActivity {
    private String backgroundName;
    private String day;
    private int hour, minute;
    private Bitmap imageBitmap;

    private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_background);

        day = "N/A";
        backgroundName = "N/A";
        imageBitmap = null;

        setButtons();
    }

    //Creates a new directory in background-alarms folder.
    // Stores both text file containing information and image file into the directory
    private void saveToAssets() {

        //Finds background-alarms folder
        File parentDirectory = new File(getFilesDir(), "background-alarms");
        if(!parentDirectory.exists())
            parentDirectory.mkdir();


        //Creates background_(random unique id) folder for storing data
        File childDirectory;
        if(parentDirectory.listFiles().length >= 500) {
            Toast.makeText(getBaseContext(), "Too many background alarms saved!", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Test1");
        do {
            int backgroundFolderId = (int) (Math.random() * (100000));
            childDirectory = new File(parentDirectory, "background_" + backgroundFolderId);
        } while (childDirectory.exists());

        childDirectory.mkdir();

        //Creates and writes background name and time into a text file
        FileWriter fosInfo = null;
        File informationFile = new File(childDirectory, "\\information.txt");

        try {
            informationFile.createNewFile();
            fosInfo = new FileWriter(informationFile);
            fosInfo.append(backgroundName + "\r\n");
            fosInfo.append(day + "\r\n");
            fosInfo.append(hour + "\r\n");
            fosInfo.append(Integer.toString(minute));
            fosInfo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fosInfo != null) {
                try {
                    fosInfo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Test2");

        //Copies image from gallery into background directory
        File imgFile = new File(childDirectory, "\\image.png");
        FileOutputStream fosImg = null;

        try {
            fosImg = new FileOutputStream(imgFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fosImg);
            fosImg.flush();
            fosImg.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fosInfo != null) {
                try {
                    fosInfo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Test3");

        //Goes back to main activity
        Toast.makeText(getApplicationContext(), "Saved " + backgroundName, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(BackgroundAddActivity.this, MainActivity.class));


    }

    private void setButtons() {
        Button btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);
        ImageButton btnImage = findViewById(R.id.btnImage);
        Button[] btnDays = {findViewById(R.id.btnSunday), findViewById(R.id.btnMonday), findViewById(R.id.btnTuesday),
                findViewById(R.id.btnWednesday), findViewById(R.id.btnThursday), findViewById(R.id.btnFriday),
                findViewById(R.id.btnSaturday)};

        //Sets up the Weekday Buttons
        for(int i = 0; i < btnDays.length; i++) {
            int dayNum = i;
            btnDays[dayNum].setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      //Flips between Red and Blue
                      day = days[dayNum];
                      btnDays[dayNum].setTextColor(btnDays[dayNum].getCurrentTextColor() == Color.RED ? Color.BLUE : Color.RED);
                  }
            });
          };

        //Back Button -> Goes back to main activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BackgroundAddActivity.this, MainActivity.class));
            }
        });

        //Save Button -> saves current information to
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText backgroundNameField = findViewById(R.id.backgroundName);
                backgroundName = backgroundNameField.getText().toString();

                if(day == "N/A" || backgroundName == "N/A" || imageBitmap == null) {
                    System.out.println(day);
                    System.out.println(backgroundName);
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveToAssets();
                startActivity(new Intent(BackgroundAddActivity.this, MainActivity.class));
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivity(Intent.createChooser(i, "Select Picture"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ImageButton btnImage = findViewById(R.id.btnImage);
                    btnImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

}
