package com.example.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.List;

public class UplaoadedImages extends AppCompatActivity {
    LinearLayout  linearLayoutXML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplaoaded_images);
        linearLayoutXML= findViewById(R.id.linearlayout);
        Intent intent = getIntent();
        String usersname = intent.getStringExtra("username");
        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Images");
        parseQuery.whereEqualTo("username",usersname);
        //parseQuery.orderByDescending("createdAt");
        final ProgressDialog progressDialog = new ProgressDialog(UplaoadedImages.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
               if(e==null&&objects.size()>0)
               {
                for(ParseObject post:objects)
                {
                    final TextView imageDesc = new TextView(UplaoadedImages.this);
                    imageDesc.setText(post.get("Description")+"");
                    ParseFile postPicture = (ParseFile) post.get("images");

                    postPicture.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(data!=null&&e==null)
                            {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                ImageView postImageView = new ImageView(UplaoadedImages.this);
                                LinearLayout.LayoutParams linearlayout = new LinearLayout
                                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearlayout.setMargins(5,5,5,5);
                                postImageView.setLayoutParams(linearlayout);
                                postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                postImageView.setImageBitmap(bitmap);
                                LinearLayout.LayoutParams descParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                descParam.setMargins(5,5,5,5);
                                imageDesc.setLayoutParams(descParam);
                                imageDesc.setGravity(Gravity.CENTER);
                                imageDesc.setTextSize(30f);
                                imageDesc.setBackgroundColor(Color.BLUE);
                                imageDesc.setTextColor(Color.BLACK);
                                linearLayoutXML.addView(postImageView);
                                linearLayoutXML.addView(imageDesc);


                            }

                        }
                    });
                    progressDialog.dismiss();
                }
               } else
               {
                   finish();
                   FancyToast.makeText(UplaoadedImages.this,"user has not uploded anything",FancyToast.LENGTH_SHORT,FancyToast.CONFUSING,false).show();
               }
            }
        });

    }
}
