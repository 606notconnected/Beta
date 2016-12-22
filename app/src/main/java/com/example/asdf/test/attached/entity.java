package com.example.asdf.test.attached;

public class entity {

    private int imageUri;//读取图片的地址
    private boolean isSelected;//是否被选中状体的标示符

    public entity(int imageUri,boolean isSelected) {
        this.imageUri = imageUri;
        this.isSelected = isSelected;
    }

    public int getImageUri() {
        return imageUri;
    }
    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


}