package com.vinaychitade.rsm.myhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CalldoctorActivity extends AppCompatActivity {
   ImageView imageView2,imageView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calldoctor);
        imageView2=findViewById(R.id.imageView2);
        imageView4=findViewById(R.id.imageView4);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tomainact=new Intent(CalldoctorActivity.this,MainActivity.class);
                startActivity(tomainact);
                finish();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Myprofile=new Intent(CalldoctorActivity.this,MyProfilebtnActivity.class);
                startActivity(Myprofile);
                finish();
            }
        });
        Toast.makeText(this, "Calling Doctor", Toast.LENGTH_SHORT).show();
        dialPhoneNumber("+919022968010");

    }

    private void dialPhoneNumber(String phoneNumber) {
        try {
            Thread.sleep(5000); // wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }






}