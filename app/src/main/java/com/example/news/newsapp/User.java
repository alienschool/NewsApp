package com.example.news.newsapp;

import com.google.gson.annotations.SerializedName;


class User {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("type")
    public String type;

    @SerializedName("action")
    public String action;

    @SerializedName("response")
    public String response;

    public String getResponse()
    {
        return response;
    }
    public String getId()
    {
        return id;
    }
}
