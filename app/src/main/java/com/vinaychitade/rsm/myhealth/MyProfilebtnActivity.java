package com.vinaychitade.rsm.myhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.vinaychitade.rsm.myhealth.R.id;

public class MyProfilebtnActivity extends AppCompatActivity {
    Button btnedtprof,btnmyordr,btnabtus,profToHomeBtn;
    ImageView imageView2,imageView3,imageView4,profileImageView;
    TextView txtvuser;

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
        profToHomeBtn=findViewById(id.profToHomeBtn);
        profileImageView = findViewById(id.usericon1);
        txtvuser= findViewById(id.txtvuser);

        // Get the current user from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Get the user's profile photo URL
            Uri photoUrl = currentUser.getPhotoUrl();
            txtvuser.setText("Hi, "+currentUser.getDisplayName());

            if (photoUrl != null) {
                //  Picasso to load and display the image
                Picasso.get().load(photoUrl.toString()).into(profileImageView);
            } else {
                // If the user does not have a profile photo
                profileImageView.setImageResource(R.drawable.useract);
            }
        }else {
            profileImageView.setImageResource(R.drawable.useract);
        }


        profToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gohome=new Intent(MyProfilebtnActivity.this,MainActivity.class);
                startActivity(gohome);
                finish();
            }
        });

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