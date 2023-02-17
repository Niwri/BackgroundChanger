package org.niwri.backgroundchanger;

import android.graphics.Bitmap;
import android.net.Uri;

import java.text.DateFormat;

public class BackgroundImage {

    private String backgroundName;
    private boolean enable;
    private Bitmap backgroundBitmap;
    private Date backgroundDate;

    public BackgroundImage(String backgroundName, Bitmap backgroundBitmap, Date backgroundDate) {
        this.backgroundName = backgroundName;
        this.backgroundBitmap = backgroundBitmap;
        this.backgroundDate = backgroundDate;
    }

    //Setters
    public void setBackgroundName(String backgroundName) {
        this.backgroundName = backgroundName;
    }

    public void setBackgroundPic(Bitmap backgroundBitmap) { this.backgroundBitmap = backgroundBitmap; }

    public void setBackgroundDate(Date backgroundDate) { this.backgroundDate = backgroundDate; }

    public void setEnabled(boolean enable) { this.enable = enable; }

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



}
