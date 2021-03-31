package com.example.firebasedemo;

public class CategoryInfoWithSelect {
    boolean isSelected;
    String categoryName;

    public CategoryInfoWithSelect(boolean isSelected, String categoryName) {
        this.isSelected = isSelected;
        this.categoryName = categoryName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

