package com.example.news.newsapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.news.newsapp.R.id.parent;
import static com.example.news.newsapp.R.styleable.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Session Manager Class
    SessionManager session;
    // Button Logout
    Button btnLogout;

    String SessionId,SessionEmail,SessionPassword;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        setTitle("My Reports");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainActivity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        // Session class instance

        session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

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
        setNavigationViewListener();
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        // Button logout
        //btnLogout = (Button) findViewById(R.id.btnLogout);
        /**
         * Logout button click event
         * */
        //btnLogout.setOnClickListener(new View.OnClickListener() {

           // @Override
          //  public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                //session.logoutUser();
           // }
        //});
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 125);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 125);
        /*Button newReportButton=(Button)findViewById(R.id.newReport_button);
        newReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this, EditNewsActivity.class);
                //Toast.makeText(MainActivity.this, "this is " + view.getTag(), Toast.LENGTH_SHORT).show();
                i.putExtra("reportId", "0");
                startActivity(i);
            }

        });*/
        getReports();
    }

    void getReports(){
        APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);
        Call<List<Report>> call=apiInterface.getReports(SessionEmail,SessionPassword,"getUserReports");
        call.enqueue(new Callback<List<Report>>() {

            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                List<Report> report=response.body();
                //Toast.makeText(MainActivity.this, "Server response: "+report.get(0).name, Toast.LENGTH_SHORT).show();

                    ArrayAdapter<Report> adapter = new ArrayAdapter<Report>(MainActivity.this,android.R.layout.simple_list_item_1,report);
                    final ListView lv= (ListView) findViewById(R.id.listView1);
                    lv.setAdapter(new ListAdapter(MainActivity.this, 0,report));
                    //lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {

                    //String UserInfo = lv.getItemAtPosition(position).toString();
                    //String reportId = UserInfo.substring(0, UserInfo .indexOf(" "));
                    Intent i =new Intent(MainActivity.this, EditNewsActivity.class);
                    //Toast.makeText(MainActivity.this, "this is " + view.getTag(), Toast.LENGTH_SHORT).show();
                    i.putExtra("reportId", view.getTag().toString());
                    startActivity(i);
                    finish();
                }
            });
            }
            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
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
}

class ListAdapter extends ArrayAdapter<Report> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<Report> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_listitem, null);
        }

        Report p = getItem(position);

        if (p != null) {
            TextView tt2 = (TextView) v.findViewById(R.id.name_custom);
            TextView tt3 = (TextView) v.findViewById(R.id.detail_custom);

            ImageView im1= (ImageView) v.findViewById(R.id.imageView_custom);
            v.setTag(p.id);

            if(im1 !=null){
                if(p.ImageThumbType != null){
                    if (p.ImageThumbType.equalsIgnoreCase("image")){
                        Picasso.with(getContext()).load("http://dibukhanmathematician.com/news/php/"+ p.ImageThumbUrl).into(im1);
                    }
                }else{
                    im1.setBackgroundResource(R.drawable.tech1);
                }
            }
            if (tt2 != null) {
                tt2.setText(p.name);
            }

            if (tt3 != null) {
                tt3.setText(p.detail);
            }
        }

        return v;
    }

}
