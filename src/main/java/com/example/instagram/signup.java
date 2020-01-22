package com.example.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class signup extends AppCompatActivity implements View.OnClickListener{

    Button login,signup;
    EditText user_name,password,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.ssignup);
        user_name = findViewById(R.id.susername);
        password = findViewById(R.id.spassword);
        email = findViewById(R.id.semail);

        signup.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ssignup:
                final ParseUser parseUser = new ParseUser();
                if(email.getText().equals(""))
                {
                    FancyToast.makeText(getApplicationContext(),"email can not be empty",FancyToast.LENGTH_SHORT,FancyToast.CONFUSING,true).show();
                }
                else
                {
                    parseUser.setUsername(user_name.getText().toString());
                    parseUser.setPassword(password.getText().toString());
                    parseUser.setEmail(email.getText().toString());
                    if(ParseUser.getCurrentUser()!=null)
                    {
                        ParseUser.logOut();
                    }
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                FancyToast.makeText(getApplicationContext(),"success",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                toSocialMedia();
                            }
                            else
                            {
                                FancyToast.makeText(getApplicationContext(),"Failed"+e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

                            }
                            progressDialog.dismiss();

                        }
                    });

                }

                break;



        }

    }
    void toSocialMedia()
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
