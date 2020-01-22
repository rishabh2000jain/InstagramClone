package com.example.instagram;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

import static com.parse.ParseUser.getCurrentUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePicture extends Fragment implements View.OnClickListener {


    public SharePicture() {
        // Required empty public constructor
    }

    ImageView sharedImage;
    Button submit;
    EditText descOfImage;
    Bitmap recievedImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture, container, false);
        sharedImage = view.findViewById(R.id.imageshare);
        descOfImage = view.findViewById(R.id.description);
        submit = view.findViewById(R.id.sharebutton);
        submit.setOnClickListener(SharePicture.this);
        sharedImage.setOnClickListener(SharePicture.this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sharebutton:
                if(recievedImage!=null)
                {
                    if(descOfImage.getText().toString().equals(""))
                    {
                        FancyToast.makeText(getContext(),"Description can not be empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }
                    else
                    {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        recievedImage.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("pic.png",bytes);
                        ParseObject parseObject = new ParseObject("Images");
                        parseObject.put("images",parseFile);
                        parseObject.put("username",ParseUser.getCurrentUser().getUsername());
                        parseObject.put("Description",descOfImage.getText().toString());
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("uploding");
                        progressDialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null)
                                {
                                    FancyToast.makeText(getContext(),"uploded success",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                }
                                else
                                {
                                    FancyToast.makeText(getContext(),"unknown error",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                                }
                                progressDialog.cancel();
                            }
                        });
                    }
                }
                break;
            case R.id.imageshare:
                if(Build.VERSION.SDK_INT>=23&&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);

                }
                else
                {
                    pickImage();
                }
                break;
                default:
                    break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1000)
        {
            if(permissions.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                pickImage();
            }
        }
    }

    private void pickImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                try {
                    Uri selectedImage= data.getData();//identifies resource by location or name
                    String[] imagePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,imagePath,
                            null,null,null);
                    //it contain the result of the queary made against database
                    cursor.moveToFirst();
                    //move the poition of cursor to first position
                    int colIndex= cursor.getColumnIndex(imagePath[0]);
                    //Returns the zero-based index for the given column name, or -1 if the column doesn't exist.
                    String picturePath = cursor.getString(colIndex);
                    cursor.close();
                     recievedImage = BitmapFactory.decodeFile(picturePath);
                    sharedImage.setImageBitmap(recievedImage);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
