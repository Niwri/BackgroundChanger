package org.niwri.backgroundchanger;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class BackgroundAddActivity extends AppCompatActivity {
    private String backgroundName;
    private int hour, minute;
    private Bitmap imageBitmap;


    private EditText backgroundNameField;
    private String directory;
    private ImageButton btnImage;
    private Button btnSave, btnBack;
    private Button[] btnDays;
    private boolean[] daysEnable;
    private boolean enable;

    int offColor = 0xff << 24 | 0x25 << 16 | 0x25 << 8 | 0x25;
    int onColor = 0xff << 24 | 0x38 << 16 | 0xb8 << 8 | 0xfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_background);

        //Loads default values
        daysEnable = new boolean[7];
        backgroundName = "N/A";
        imageBitmap = null;
        Arrays.fill(daysEnable, false);
        enable = true;

        //Retrieves necessary views from the layout
        backgroundNameField = findViewById(R.id.backgroundName);
        setButtons();

        //If edit mode, load existing data to write over
        Intent intent = getIntent();
        if(intent.getBooleanExtra("loadData", false))
            loadExistingData();

    }

    //Creates a new directory in background-alarms folder.
    // Stores both text file containing information and image file into the directory
    private void saveToAssets() {

        //Finds background-alarms folder
        File parentDirectory = new File(getFilesDir(), "background-alarms");
        if(!parentDirectory.exists())
            parentDirectory.mkdir();

        boolean loadData = getIntent().getBooleanExtra("loadData", false);
        //Creates background_(random unique id) folder for storing data
        File childDirectory;
        if(parentDirectory.listFiles().length >= 500) {
            Toast.makeText(getBaseContext(), "Too many background alarms saved!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(loadData) {
            childDirectory = new File(directory);

        } else {
            do {
                int backgroundFolderId = (int) (Math.random() * (100000));
                directory = "background_" + backgroundFolderId;
                childDirectory = new File(parentDirectory, directory);
            } while (childDirectory.exists());

            childDirectory.mkdir();
        }




        //Creates and writes background name and time into a text file
        FileWriter fosInfo = null;
        File informationFile = new File(childDirectory, "/information.txt");

        try {
            if(!loadData)
                informationFile.createNewFile();
            fosInfo = new FileWriter(informationFile, false);
            fosInfo.append("Name:" + backgroundName + "\r\n");
            fosInfo.append("Days:");
            for(int i = 0; i < daysEnable.length; i++)
                fosInfo.append(daysEnable[i] + " ");
            fosInfo.append("\r\n");
            fosInfo.append("Hour:" + hour + "\r\n");
            fosInfo.append("Minute:" + minute + "\r\n");
            fosInfo.append("Enable:" + enable);
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

        //Copies image from gallery into background directory
        File imgFile = new File(childDirectory, "/image.png");
        FileOutputStream fosImg = null;

        try {
            fosImg = new FileOutputStream(imgFile, false);
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

        //Goes back to main activity
        Toast.makeText(getApplicationContext(), "Saved " + backgroundName, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(BackgroundAddActivity.this, MainActivity.class));


    }

    private void setButtons() {
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnImage = findViewById(R.id.btnImage);
        btnDays = new Button[]{findViewById(R.id.btnSunday), findViewById(R.id.btnMonday), findViewById(R.id.btnTuesday),
                findViewById(R.id.btnWednesday), findViewById(R.id.btnThursday), findViewById(R.id.btnFriday),
                findViewById(R.id.btnSaturday)};

        //Sets up the Weekday Buttons
        for(int i = 0; i < btnDays.length; i++) {
            int dayNum = i;
            btnDays[dayNum].setOnClickListener(new View.OnClickListener() {
                  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                  @Override
                  public void onClick(View view) {
                      //Flips between Red and Blue
                      daysEnable[dayNum] = !daysEnable[dayNum];
                      btnDays[dayNum].setTextColor(daysEnable[dayNum] ? onColor : offColor);
                      btnDays[dayNum].setBackgroundTintList(
                              ContextCompat.getColorStateList(
                                      getApplicationContext(),
                                      daysEnable[dayNum] ? R.color.onColorCircle : R.color.offColorCircle));

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

                //Retrieving name field
                backgroundName = backgroundNameField.getText().toString();

                //Retrieving day enabled field
                boolean dayExist = false;
                for(int i = 0; i < daysEnable.length && !dayExist; i++)
                    if(daysEnable[i])
                        dayExist = true;
                if(!dayExist || backgroundName == "N/A" || imageBitmap == null) {
                    System.out.println(daysEnable);
                    System.out.println(backgroundName);
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Retrieving hour field

                //Retrieving minute field

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
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Got result");
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    System.out.println("Received");
                    ImageButton btnImage = findViewById(R.id.btnImage);

                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap imageRounded=Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getConfig());
                    Canvas canvas=new Canvas(imageRounded);
                    Paint mpaint=new Paint();
                    mpaint.setAntiAlias(true);
                    mpaint.setShader(new BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    canvas.drawRoundRect((new RectF(0, 0, imageBitmap.getWidth(), imageBitmap.getHeight())), imageBitmap.getWidth()*0.3f, imageBitmap.getHeight()*0.3f, mpaint); // Round Image Corner 100 100 100 100

                    btnImage.setImageURI(null);
                    btnImage.setImageBitmap(imageRounded);
                    btnImage.setBackgroundResource(0);
                }
            }
        }
    }

    private void loadExistingData() {

        //Loads saved data to variables
        Intent intent = getIntent();
        backgroundName = intent.getStringExtra("backgroundName");
        daysEnable = intent.getBooleanArrayExtra("daysEnable");
        hour = intent.getIntExtra("hour", 0);
        minute = intent.getIntExtra("minute", 0);
        imageBitmap = BitmapFactory.decodeFile(intent.getStringExtra("imageFilePath"));
        enable = intent.getBooleanExtra("enable", true);
        directory = getIntent().getStringExtra("directoryName");

        //Changes views with respect to the saved data
        backgroundNameField.setText(backgroundName);

        btnImage.setImageURI(null);
        btnImage.setBackgroundResource(0);

        Bitmap imageRounded=Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getConfig());
        Canvas canvas=new Canvas(imageRounded);
        Paint mpaint=new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, imageBitmap.getWidth(), imageBitmap.getHeight())), imageBitmap.getWidth()*0.3f, imageBitmap.getHeight()*0.3f, mpaint); // Round Image Corner 100 100 100 100


        btnImage.setImageBitmap(imageRounded);

        btnDays = new Button[]{findViewById(R.id.btnSunday), findViewById(R.id.btnMonday), findViewById(R.id.btnTuesday),
                findViewById(R.id.btnWednesday), findViewById(R.id.btnThursday), findViewById(R.id.btnFriday),
                findViewById(R.id.btnSaturday)};

        for(int i = 0; i < btnDays.length; i++) {
            int dayNum = i;
            btnDays[dayNum].setTextColor(daysEnable[dayNum] ? onColor : offColor);
            btnDays[dayNum].setBackgroundTintList(
                    ContextCompat.getColorStateList(
                            getApplicationContext(),
                            daysEnable[dayNum] ? R.color.onColorCircle : R.color.offColorCircle));
        }



    }

}
