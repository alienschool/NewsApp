package com.example.news.newsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Userlogin extends AppCompatActivity {

    //CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    EditText mEmailInput,mPasswordInput;
    Button mSignInButton;
    User user;
    Context mContext;
    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        mContext=Userlogin.this;

        session = new SessionManager(getApplicationContext());
        if(session.isLoggedIn()){
            Intent intent = new Intent(mContext,MainActivity.class);
            mContext.startActivity(intent);
            finish();
        }
        //logo font
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
        TextView logo=(TextView) findViewById(R.id.logo_userlogin);
        logo.setTypeface(face);

        mEmailInput = (EditText) findViewById(R.id.email_userLogin);
        mPasswordInput = (EditText) findViewById(R.id.password_userLogin);

        TextView mRegisterText = (TextView) findViewById(R.id.textViewRegister_signIn);
        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Userlogin.this,UserSignup.class);
                Userlogin.this.startActivity(intent);
            }
        });
        mSignInButton = (Button) findViewById(R.id.loginButton_userLogin);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

    }
    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        String email,password;
        // Reset errors.
        mEmailInput.setError(null);
        mPasswordInput.setError(null);
        user=new User();
        user.email = mEmailInput.getText().toString();
        user.password = mPasswordInput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(user.password) && !isPasswordValid(user.password)) {
            mPasswordInput.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordInput;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user.email)) {
            mEmailInput.setError(getString(R.string.error_field_required));
            focusView = mEmailInput;
            cancel = true;
        } else if (!isEmailValid(user.email)) {
            mEmailInput.setError(getString(R.string.error_invalid_email));
            focusView = mEmailInput;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);
            user.action="signin";
            Call<User> call=apiInterface.Login(user.email,user.password,user.action);
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User u=response.body();
                    if(u.response.equalsIgnoreCase("success")) {
                        //Toast.makeText(Userlogin.this, "u.email : "+u.email +"u.pass : " +u.password+ "user.email : "+user.email+"user.pass : " +user.password, Toast.LENGTH_SHORT).show();
                        // Session Manager
                        session = new SessionManager(getApplicationContext());
                        session.createLoginSession(u.id, u.email, u.password);

                        Intent intent = new Intent(Userlogin.this,MainActivity.class);
                        Userlogin.this.startActivity(intent);
                        Userlogin.this.finish();
                    }else{
                        Toast.makeText(Userlogin.this, "Server response: "+u.response, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                      Toast.makeText(Userlogin.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

}
