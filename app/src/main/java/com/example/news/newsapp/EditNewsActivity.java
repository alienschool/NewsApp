package com.example.news.newsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditNewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // Session Manager Class
    SessionManager session;
    String SessionId,SessionEmail,SessionPassword;

    Intent getInten;
    String reportId,type="election";
    ImageView img;
    EditText mName,mDetail;
    int counter;
    private static final int IMG_REQUEST=777;
    private static final int VIDEO_REQUEST=888;
    private static final int AUDIO_REQUEST=999;
    private Bitmap bitmap;
    Spinner dropdown;
    Context mContext;
    ProgressBar progressBar;
    ArrayAdapter<String> spinnerAdapter;
    String decodableString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);
        mContext=EditNewsActivity.this;

        setTitle("My Reports");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarEditNews);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.editNewsActivity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        session = new SessionManager(getApplicationContext());
        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        SessionId = user.get(SessionManager.KEY_NAME);

        // email
        SessionEmail = user.get(SessionManager.KEY_EMAIL);

        // password
        SessionPassword = user.get(SessionManager.KEY_PASSWORD);

        //get intent values from previous activity
        getInten = getIntent();
        reportId=getInten.getExtras().get("reportId").toString();
        setNavigationViewListener();

        progressBar=(ProgressBar)findViewById(R.id.editNews_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        img=(ImageView)findViewById(R.id.imageView_editNews);
        mName=(EditText)findViewById(R.id.Edit_name_editNews);
        mDetail=(EditText)findViewById(R.id.Edit_detail_editNews);
        //get the spinner from the xml.
        dropdown = (Spinner)findViewById(R.id.Edit_type_spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Political", "Sports","Local","International","Entertainment"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(spinnerAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        type="election";
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        type="international";
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
                        type="islamabad";
                        break;

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button uploadImage,uploadVideo,uploadAudio,update;

        //selected image button click
        uploadImage=(Button)findViewById(R.id.uploadImageButton_editNews);
        if(reportId.equalsIgnoreCase("0")){
            uploadImage.setVisibility(View.INVISIBLE);
        }
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog=new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Upload Image");
                alertDialog.setMessage("Select image from :)");
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, IMG_REQUEST);

                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE,"Gallary",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent galleryImageintent=new Intent();
                                galleryImageintent.setType("image/jpg");
                                galleryImageintent.setAction(Intent.ACTION_PICK);
                                startActivityForResult(galleryImageintent,IMG_REQUEST );
                            }
                        });
                alertDialog.show();



            }

        });

        //selected video button click
        uploadVideo=(Button)findViewById(R.id.uploadVideoButton_editNews);
        if(reportId.equalsIgnoreCase("0")){
            uploadVideo.setVisibility(View.INVISIBLE);
        }
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog=new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Upload Video");
                alertDialog.setMessage("Select image from :)");
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent takeVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                startActivityForResult(takeVideo, VIDEO_REQUEST);

                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE,"Gallary",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent galleryVideoIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryVideoIntent, VIDEO_REQUEST);
                            }
                        });
                alertDialog.show();

            }

        });

        //selected audio button click
        uploadAudio=(Button)findViewById(R.id.uploadAudioButton_editNews);
        if(reportId.equalsIgnoreCase("0")){
            uploadAudio.setVisibility(View.INVISIBLE);
        }
        uploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog=new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Upload Audio");
                alertDialog.setMessage("Select image from :)");
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent takeAudio = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                //takeAudio.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imageToStore));
                                startActivityForResult(takeAudio, AUDIO_REQUEST);



                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE,"Gallary",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent galleryAudioIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryAudioIntent, AUDIO_REQUEST);
                            }
                        });
                alertDialog.show();

            }

        });

        //create or update button click
        update=(Button)findViewById(R.id.edit_update_button);
        if(reportId.equalsIgnoreCase("0")){
            update.setText("Create");
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reportId.equalsIgnoreCase("0")){
                    //create new
                    createReport();
                }else{
                    //update current
                    updateReport();
                }
            }

        });

        if(!reportId.equalsIgnoreCase("0")){
            //getReport();
            getReportById();
            //get all uploaded files with this report (image/video/audio)
            getReportImages();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                type="election";
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                type="international";
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                type="islamabad";
                break;

        }
    }


    File myFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST&&resultCode ==RESULT_OK&&data!=null)
        {
            // Get the image from data
            Uri path=data.getData();

            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);

                //preview selected image
                img.setImageBitmap(bitmap);
                UploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode==VIDEO_REQUEST&&resultCode ==RESULT_OK&&data!=null)
        {


                    // Get the video from data
                    Uri selectedVideo = data.getData();
                    myFile = new File(getRealPathFromURI1(EditNewsActivity.this,selectedVideo));
                    uploadVideo(myFile);
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 125);

        }else if(requestCode==AUDIO_REQUEST&&resultCode ==RESULT_OK&&data!=null)
        {


            // Get the video from data
            Uri selectedVideo = data.getData();
            myFile = new File(getRealPathFromURI1(EditNewsActivity.this,selectedVideo));
            uploadAudio(myFile);
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 125);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            uploadVideo(myFile);
        }
    }

    void createReport(){
        APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);
        Call<Report> call=apiInterface.createReport(mName.getText().toString(),mDetail.getText().toString(),type,SessionId,"createNewReport");
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                Report r =response.body();
                Toast.makeText(EditNewsActivity.this, "response" +  r.response , Toast.LENGTH_SHORT).show();
                if(!r.response.equalsIgnoreCase("null")){
                    Intent i =new Intent(EditNewsActivity.this, EditNewsActivity.class);
                    //Toast.makeText(MainActivity.this, "this is " + view.getTag(), Toast.LENGTH_SHORT).show();
                    i.putExtra("reportId", r.response.toString());
                    startActivity(i);
                    EditNewsActivity.this.finish();
                }
            }
            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                Toast.makeText(EditNewsActivity.this, "Fail"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    void updateReport(){
        APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);
        Call<Report> call=apiInterface.updateReport(mName.getText().toString(),mDetail.getText().toString(),type,reportId,"reportDetailUpdateByReporter");
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                Report r =response.body();
                Toast.makeText(EditNewsActivity.this, "response" +  r.response , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                Toast.makeText(EditNewsActivity.this, "Fail"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
     * UPLOAD THE SELECTED VIDEO TO THE SERVER
     */
    public void uploadVideo(File file)
    {
        progressBar.setVisibility(View.VISIBLE);
        APIMyInterface apiInterface = APIClient.getApiClient().create(APIMyInterface.class);
        MediaType MEDIA_TYPE = MediaType.parse("video/mp4");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part  requestBody = MultipartBody.Part.createFormData("upload","upload.mp4", requestFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "testvideo");
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), reportId);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), "reportVideoFileUpload");

        //calling upload function in APIMyInterface and sending parameters
        Call<ReportFile> call = apiInterface.uploadVideo(id,name,requestBody,action);
        call.enqueue(new Callback<ReportFile>(){
            @Override
            public void onResponse(Call<ReportFile> call, Response<ReportFile> response) {
                //Toast.makeText(EditNewsActivity.this, response.body().response+ " ye h", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    //Toast.makeText(EditNewsActivity.this, "success: " + response.code()+"\n"+"success: "+response.message()+"\n"+response.body(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(EditNewsActivity.this, "Server response: " + response.code()+"\n"+"success: "+response.message(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(EditNewsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ReportFile> call, Throwable t) {
                //Toast.makeText(EditNewsActivity.this, t.getMessage()+ "nhe chla", Toast.LENGTH_SHORT).show();
                Toast.makeText(EditNewsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            } //error even before compile***



        });
    }

    //upload audio
    public void uploadAudio(File file)
    {
        progressBar.setVisibility(View.VISIBLE);
        APIMyInterface apiInterface = APIClient.getApiClient().create(APIMyInterface.class);
        //MediaType MEDIA_TYPE = MediaType.parse("video/mp4");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part  requestBody = MultipartBody.Part.createFormData("upload","upload.mp3", requestFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "testvideo");
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), reportId);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), "reportVideoFileUpload");

        //calling upload function in APIMyInterface and sending parameters
        Call<ReportFile> call = apiInterface.uploadVideo(id,name,requestBody,action);
        call.enqueue(new Callback<ReportFile>(){
            @Override
            public void onResponse(Call<ReportFile> call, Response<ReportFile> response) {
                //Toast.makeText(EditNewsActivity.this, response.body().response+ " ye h", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    //Toast.makeText(EditNewsActivity.this, "success: " + response.code()+"\n"+"success: "+response.message()+"\n"+response.body(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(EditNewsActivity.this, "Server response: " + response.code()+"\n"+"success: "+response.message(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(EditNewsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ReportFile> call, Throwable t) {
                //Toast.makeText(EditNewsActivity.this, t.getMessage()+ "nhe chla", Toast.LENGTH_SHORT).show();
                Toast.makeText(EditNewsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            } //error even before compile***



        });
    }

    //upload image
    private void UploadImage()
    {
        progressBar.setVisibility(View.VISIBLE);
        String Image= imagetoString();
        String title="A"; //Name of Image

        APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);

        //calling upload function in APIMyInterface and sending parameters
        Toast.makeText(EditNewsActivity.this, "report ID "+ reportId, Toast.LENGTH_SHORT).show();
        Call<ReportFile> call=apiInterface.uploadImage(reportId,"mytest",Image,"reportFileUpload");
        call.enqueue(new Callback<ReportFile>() {

            //when response is received from server
            @Override
            public void onResponse(Call<ReportFile> call, Response<ReportFile> response) {
                progressBar.setVisibility(View.INVISIBLE);
                ReportFile imageClass=response.body();
                Toast.makeText(EditNewsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                //Toast.makeText(EditNewsActivity.this, "Server response: "+response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ReportFile> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditNewsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                //Toast.makeText(EditNewsActivity.this, "nhe chla"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //get all uploaded files with this report (image/video/audio)
    void getReportImages(){
        APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);

        //Sending parameters to APIMyInterface for getting report files
        Call<List<ReportFile>> call=apiInterface.getReportImage(SessionEmail,SessionPassword,reportId,"getReportImages");
        call.enqueue(new Callback<List<ReportFile>>() {

            @Override
            public void onResponse(Call<List<ReportFile>> call, Response<List<ReportFile>> response) {
                List<ReportFile> reportFiles =response.body();
                LinearLayout layout = (LinearLayout)findViewById(R.id.imagesLayout_editNews);
                ImageView image;
                VideoView video;
                for(int i = 0; i< reportFiles.size(); i++)
                {
                    //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,260+(i*10)));
                    //image.setMaxHeight(20);
                    //image.setMaxWidth(20);

                    // Adds the view to the layout
                    if(reportFiles.get(i).type.equalsIgnoreCase("image")){
                        image = new ImageView(EditNewsActivity.this);
                        layout.addView(image);
                        Picasso.with(EditNewsActivity.this).load("http://dibukhanmathematician.com/news/php/"+ reportFiles.get(i).url).into(image);
                    }else if(reportFiles.get(i).type.equalsIgnoreCase("video")){
                        //video = new VideoView(EditNewsActivity.this);
                        //layout.addView(video);
                        //video.setVideoPath("http://dibukhanmathematician.com/news/php/"+reportFiles.get(i).url);
                        //video.start();
                    }else{
                        //layout.addView(image);
                    }
                    //Toast.makeText(EditNewsActivity.this, "Server response: "+ reportFiles.get(i).name, Toast.LENGTH_SHORT).show();
                }


                //ArrayAdapter<ReportFile> adapter = new ArrayAdapter<ReportFile>(EditNewsActivity.this,android.R.layout.simple_list_item_1,reportFiles);
                //ListView lv= (ListView) findViewById(R.id.listView1);
                //lv.setAdapter(adapter);

            }
            @Override
            public void onFailure(Call<List<ReportFile>> call, Throwable t) {
                Toast.makeText(EditNewsActivity.this, "Fail to get files", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //getReport
    void getReportById(){
        APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);
        Call<Report> call=apiInterface.getReportById(SessionEmail,SessionPassword,reportId,"getReportById");
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                Report r =response.body();
                Toast.makeText(EditNewsActivity.this, "response" +  r.name , Toast.LENGTH_SHORT).show();
                if(r.name!=null && !r.name.equalsIgnoreCase("null")){
                    mName.setText(r.name);
                    if (!r.type.equals(null)) {
                        int spinnerPosition = spinnerAdapter.getPosition(r.type);
                        dropdown.setSelection(spinnerPosition);
                    }
                    mDetail.setText(r.detail);
                }

            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                Toast.makeText(EditNewsActivity.this, "Fail"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //convert image into string
    private String imagetoString()
    {

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
        byte[] imgbyt=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyt, Base64.DEFAULT);

    }
    public String getRealPathFromURI1(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home: {
                Intent intent = new Intent(mContext,MainActivity.class);
                mContext.startActivity(intent);
                finish();
                break;
            }
            case R.id.nav_newReport: {
                Intent intent = new Intent(mContext,EditNewsActivity.class);
                intent.putExtra("reportId", "0");
                mContext.startActivity(intent);
                finish();
                break;
            }
            case R.id.nav_logout: {
                session.logoutUser();
                finish();
                break;
            }
        }
        return true;
    }
    /*private String getRealPathFromURI(Uri contentURI) {

        String thePath = "no-path-found";
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return  thePath;
    }*/
//    public String getRealPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
}
