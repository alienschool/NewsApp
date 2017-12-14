package com.example.news.newsapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSignup extends AppCompatActivity {

    //CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    EditText mNameInput,mEmailInput,mPasswordInput;
    Button mRegisterButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        //logo font
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
        TextView logo=(TextView) findViewById(R.id.logo_signUp);
        logo.setTypeface(face);

        mNameInput = (EditText) findViewById(R.id.name_signUp);
        mEmailInput = (EditText) findViewById(R.id.email_signUp);
        mPasswordInput = (EditText) findViewById(R.id.password_signUp);

        Button mSignInButton = (Button) findViewById(R.id.buttonSignin_signUp);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSignup.this,Userlogin.class);
                UserSignup.this.startActivity(intent);
            }
        });
        mRegisterButton = (Button) findViewById(R.id.buttonRegister_signUp);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();

            }
        });
    }

    private void attemptRegister() {
        String name,email,password;
        // Reset errors.
        mNameInput.setError(null);
        mEmailInput.setError(null);
        mPasswordInput.setError(null);
        user=new User();
        user.name = mNameInput.getText().toString();
        user.email = mEmailInput.getText().toString();
        user.password = mPasswordInput.getText().toString();
        user.type = "reporter";

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(user.password)) {
            mPasswordInput.setError("Password is required");
            focusView = mPasswordInput;
            cancel = true;
        }else if (!isPasswordValid(user.password)) {
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
        // Check for a valid name.
        if (TextUtils.isEmpty(user.name)) {
            mNameInput.setError(getString(R.string.error_field_required));
            focusView = mNameInput;
            cancel = true;
        } else if (!isNameValid(user.name)) {
            mNameInput.setError(getString(R.string.error_invalid_name));
            focusView = mNameInput;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            APIMyInterface apiInterface= APIClient.getApiClient().create(APIMyInterface.class);
            user.action="signup";
            Call<User> call=apiInterface.Signup(user.name,user.email,user.password,user.type,user.action);
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user=response.body();
                    if(user.response.equalsIgnoreCase("success")) {
                        Toast.makeText(UserSignup.this, "User "+user.name+" Registered!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserSignup.this,Userlogin.class);
                        UserSignup.this.startActivity(intent);
                        UserSignup.this.finish();
                    }else{
                        Toast.makeText(UserSignup.this, "Server response: "+user.response, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(UserSignup.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 2;
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
            return password.length() > 4;
    }

}
