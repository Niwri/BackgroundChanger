package org.niwri.backgroundchanger;

import android.graphics.Bitmap;
import android.net.Uri;

import java.text.DateFormat;

public class BackgroundImage {

    private String backgroundName;
    private boolean enable;
    private Bitmap backgroundBitmap;
    private Date backgroundDate;
    private String fileDirectory;
    private boolean isPM;

    public BackgroundImage(String backgroundName,
                           Bitmap backgroundBitmap,
                           Date backgroundDate,
                           boolean enable,
                           String fileDirectory) {
        this.backgroundName = backgroundName;
        this.backgroundBitmap = backgroundBitmap;
        this.backgroundDate = backgroundDate;
        this.enable = enable;
        this.fileDirectory = fileDirectory;
    }

    //Setters
    public void setBackgroundName(String backgroundName) {
        this.backgroundName = backgroundName;
    }

    public void setBackgroundPic(Bitmap backgroundBitmap) { this.backgroundBitmap = backgroundBitmap; }

    public void setBackgroundDate(Date backgroundDate) { this.backgroundDate = backgroundDate; }

    public void setEnabled(boolean enable) { this.enable = enable; }

    public void setFileDirectory(String fileDirectory) { this.fileDirectory = fileDirectory; }

    //Getters
    public String getBackgroundName() {
        return backgroundName;
    }

    public Bitmap getBackgroundBitmap() {
        return backgroundBitmap;
    }

    public Date getBackgroundDate() {
        return backgroundDate;
    }

    public boolean isEnabled() { return enable; }

    public String getFileDirectory() { return fileDirectory; }

}
