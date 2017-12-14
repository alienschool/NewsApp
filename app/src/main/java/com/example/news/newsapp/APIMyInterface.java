package com.example.news.newsapp;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIMyInterface {

    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<User> Login(@Field("email") String email, @Field("password") String password, @Field("action") String action);

    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<User> Signup(@Field("name") String name,@Field("email") String email, @Field("password") String password,@Field("type") String type, @Field("action") String action);

    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<List<Report>> getReports(@Field("email") String email, @Field("password") String password, @Field("action") String action);

    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<Report> getReportById(@Field("email") String email,
                               @Field("password") String password,
                               @Field("reportId") String reportId,
                               @Field("action") String action);
    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<Report> updateReport(@Field("name") String name,
                              @Field("detail") String detail,
                              @Field("type") String type,
                               @Field("reportId") String reportId,
                               @Field("action") String action);
    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<Report> createReport(@Field("name") String name,
                              @Field("detail") String detail,
                              @Field("type") String type,
                              @Field("reporterId") String reporterId,
                              @Field("action") String action);

    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<List<ReportFile>> getReportImage(@Field("email") String email, @Field("password") String password, @Field("reportId") String reportId, @Field("action") String action);

    @FormUrlEncoded
    @POST("mainAPI.php")
    Call<ReportFile> uploadImage(@Field("reportId") String reportId, @Field("name") String name, @Field("upload") String image, @Field("action") String action);






    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part("description") RequestBody description, @Part MultipartBody.Part file);
//RequestBody
    @Multipart
    @POST("mainAPI.php")
    Call<ReportFile> uploadVideo(@Part("reportId") RequestBody reportId, @Part("name") RequestBody name, @Part MultipartBody.Part  upload, @Part("action") RequestBody action);
    //Call<ImageClass>uploadImage(@Field("title") String title, @Field("image") String image);

}
