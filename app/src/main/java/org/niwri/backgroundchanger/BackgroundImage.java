package org.niwri.backgroundchanger;

import android.net.Uri;

import java.text.DateFormat;

public class BackgroundImage {

    private String backgroundName;
    private boolean enable;
    private Uri backgroundPic;
    private DateFormat backgroundDate;

    public BackgroundImage(String backgroundName, Uri backgroundPic) {
        this.backgroundName = backgroundName;
        this.backgroundPic = backgroundPic;
    }

    public void setBackgroundName(String backgroundName) {
        this.backgroundName = backgroundName;
    }

    public void setBackgroundPic(Uri backgroundPic) {
        this.backgroundPic = backgroundPic;
    }

    public void setBackgroundDate(DateFormat backgroundDate) {
        this.backgroundDate = backgroundDate;
    }


    public String getBackgroundName() {
        return backgroundName;
    }


    public Uri getBackgroundPic() {
        return backgroundPic;
    }

    public DateFormat getBackgroundDate() {
        return backgroundDate;
    }



}
