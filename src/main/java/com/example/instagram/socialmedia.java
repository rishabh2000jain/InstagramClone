package com.example.instagram;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class socialmedia extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabAdapter tabAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialmedia);
        viewPager = findViewById(R.id.viewpager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout = findViewById(R.id.tablayout);
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        tabLayout.setupWithViewPager(viewPager,true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.camerabutton)
        {
            if(Build.VERSION.SDK_INT>=23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3000);
            }
            else
            {
                captureImage();
            }
        }else if(item.getItemId()==R.id.logoutmenu)
        {
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent =  new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==3000)
        {
            if(permissions.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                captureImage();
            }
        }

    }

    private void captureImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==3000&&resultCode==RESULT_OK)
    {
        if(data!=null)
        {
            try {
           Uri capturedImage = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),capturedImage);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("img.png",bytes);
                ParseObject parseObject =new ParseObject("Images");
                parseObject.put("images",parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("uploding...");
                progressDialog.show();
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            FancyToast.makeText(socialmedia.this,"uploded success",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        }
                        else
                        {
                            FancyToast.makeText(socialmedia.this,"unknown error",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                        }
                        progressDialog.dismiss();
                    }
                });

        }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
