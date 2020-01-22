package com.example.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText username,pass;
    Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ParseUser.getCurrentUser()!=null)
        {
            Intent intent = new Intent(this,socialmedia.class);
            startActivity(intent);
        }
        username = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        username.setTextColor(Color.WHITE);
        pass.setTextColor(Color.WHITE);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }
@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                ParseUser parseUser = new ParseUser();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("please wait");
                progressDialog.show();
                ParseUser.logInInBackground(username.getText().toString(), pass.getText().toString()
                        , new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {

                                if(e==null)
                                {
                                    //Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                    FancyToast.makeText(getApplicationContext(),"success",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();
                                    transistionToSocialMedia();
                                }

                                else
                                {
                                    //Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    FancyToast.makeText(getApplicationContext(),"something went wrong",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                }
                                progressDialog.dismiss();
                            }
                        }
                );

                break;

            case R.id.signup:
                Intent intent = new Intent(getApplicationContext(),signup.class);
                startActivity(intent);
                break;

        }

    }
    void transistionToSocialMedia()
    {
        Intent intent = new Intent(getApplicationContext(),socialmedia.class);
        startActivity(intent);
    }
    public void rootLayoutTapped(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}

