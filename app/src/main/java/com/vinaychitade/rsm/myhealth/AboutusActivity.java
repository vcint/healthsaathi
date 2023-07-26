package com.vinaychitade.rsm.myhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutusActivity extends AppCompatActivity {
    ImageView imageView2,imageView3,imageView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        imageView2 =findViewById(R.id.imageView2);
        imageView3=findViewById(R.id.imageView3);
        imageView4=findViewById(R.id.imageView4);


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent uploadredirect= new Intent(AboutusActivity.this,UploadprescripActivity.class);
                startActivity(uploadredirect);
                finish();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hometocalldoctor= new Intent(AboutusActivity.this,CalldoctorActivity.class);
                startActivity(hometocalldoctor);
                finish();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Myprofile=new Intent(AboutusActivity.this,MyProfilebtnActivity.class);
                startActivity(Myprofile);
                finish();
            }
        });
    }
}