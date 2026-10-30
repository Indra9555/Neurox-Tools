package com.example.neuroxtools;


// ⚠️ Match your project package

public class VideoModel {
    private final String path;
    private final String title;

    public VideoModel(String path, String title) {
        this.path = path;
        this.title = title;
    }

    public String getPath() { return path; }
    public String getTitle() { return title; }
}