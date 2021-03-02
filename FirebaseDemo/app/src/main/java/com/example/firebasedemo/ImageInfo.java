package com.example.firebasedemo;

import com.google.firebase.database.Exclude;

public class ImageInfo {
    private boolean isSelected = false;
    private String mName;
    private String mImageUrl;
    private String mKey;

    public ImageInfo() {
    }



    public ImageInfo(String mName, String mImageUrl, String mKey) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mKey = mKey;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
