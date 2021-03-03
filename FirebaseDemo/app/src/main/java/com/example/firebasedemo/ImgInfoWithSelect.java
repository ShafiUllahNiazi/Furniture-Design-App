package com.example.firebasedemo;

public class ImgInfoWithSelect {

    ImageInfo imageInfo;
    boolean isSelected;

    public ImgInfoWithSelect(ImageInfo imageInfo, boolean isSelected) {
        this.imageInfo = imageInfo;
        this.isSelected = isSelected;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
