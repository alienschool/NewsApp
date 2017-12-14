package com.example.news.newsapp;

import com.google.gson.annotations.SerializedName;


public class ReportFile {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    public String name;

    @SerializedName("url")
    public String url;

    @SerializedName("type")
    public String type;

    @SerializedName("reportId")
    public String reportId;

    @SerializedName("action")
    public String action;

    @SerializedName("response")
    public String response;

}
