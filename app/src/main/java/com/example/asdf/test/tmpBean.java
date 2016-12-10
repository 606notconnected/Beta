package com.example.asdf.test;

/**
 * Created by Useradmin on 2016/11/18.
 */
public class tmpBean {

    private String title;
    private String picUrl;

    public tmpBean(String title, String picUrl) {
        this.title = title;
        this.picUrl = picUrl;
    }

    public tmpBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
