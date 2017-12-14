package com.example.news.newsapp;

import com.google.gson.annotations.SerializedName;


public class Report {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("detail")
    public String detail;

    @SerializedName("date")
    public String date;

    @SerializedName("type")
    public String type;

    @SerializedName("status")
    public String status;

    @SerializedName("likes")
    public String likes;

    @SerializedName("userId")
    public String userId;

    @SerializedName("action")
    public String action;

    @SerializedName("ImageThumbUrl")
    public String ImageThumbUrl;

    @SerializedName("ImageThumbType")
    public String ImageThumbType;

    @SerializedName("response")
    public String response;

    public Report(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.name.toString();
    }
}
