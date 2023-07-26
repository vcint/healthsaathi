package com.vinaychitade.rsm.myhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vinaychitade.rsm.myhealth.R.id;

public class MyProfilebtnActivity extends AppCompatActivity {
    Button btnedtprof,btnmyordr,btnabtus;
    ImageView imageView2,imageView3,imageView4;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profilebtn);
        imageView2 =findViewById(R.id.imageView2);
        imageView3=findViewById(R.id.imageView3);
        imageView4=findViewById(R.id.imageView4);
        btnedtprof=findViewById(R.id.btnedtprof);
        btnmyordr=findViewById(id.btnmyordr);
        btnabtus=findViewById(id.btnabtus);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent uploadredirect= new Intent(MyProfilebtnActivity.this,UploadprescripActivity.class);
                startActivity(uploadredirect);
                finish();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hometocalldoctor= new Intent(MyProfilebtnActivity.this,CalldoctorActivity.class);
                startActivity(hometocalldoctor);
                finish();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnedtprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editprof=new Intent(MyProfilebtnActivity.this,EdtProfileActivity.class);
                startActivity(editprof);
                finish();
            }
        });
        btnmyordr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myordr=new Intent(MyProfilebtnActivity.this,MyordrActivity.class);
                startActivity(myordr);
                finish();

            }
        });
        btnabtus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abtus=new Intent(MyProfilebtnActivity.this,AboutusActivity.class);
                startActivity(abtus);
                finish();
            }
        });
    }
}