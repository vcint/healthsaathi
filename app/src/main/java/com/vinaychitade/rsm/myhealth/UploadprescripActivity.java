package com.vinaychitade.rsm.myhealth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class UploadprescripActivity extends AppCompatActivity {
ImageView previewprescrip;
Button uploadcnf,canceluploadbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadprescrip);
        previewprescrip=findViewById(R.id.previewprescrip);

        Toast.makeText(this, "PLease Select Desired Option", Toast.LENGTH_SHORT).show();
        ImagePicker.with(UploadprescripActivity.this)
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();



    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        uploadcnf=findViewById(R.id.uploadcnf);
        canceluploadbtn=findViewById(R.id.canceluploadbtn);
        super.onActivityResult(requestCode,resultCode,data);

        Uri uri=data.getData();
        previewprescrip.setImageURI(uri);


        uploadcnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadto=new Intent(UploadprescripActivity.this,UploadedImage.class);
                startActivity(uploadto);
                finish();
            }
        });


        canceluploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadtomain= new Intent( UploadprescripActivity.this,MainActivity.class);
                startActivity(uploadtomain);
                finish();
            }
        });


    }


}