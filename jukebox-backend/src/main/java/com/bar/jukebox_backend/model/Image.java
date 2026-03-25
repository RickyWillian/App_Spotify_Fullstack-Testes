package com.bar.jukebox_backend.model;

public class Image {

    private int height;
    private String url;
    private int width;

    // Construtor, getters e setters
    public Image() {}

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}