package com.vinaychitade.rsm.myhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class dispatchedAmbulance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatched_ambulance);
        Toast.makeText(this, "Calling Doctor", Toast.LENGTH_SHORT).show();

        dialPhoneNumber("+917972791880");
        Intent tomain=new Intent(dispatchedAmbulance.this,MainActivity.class);
        try {

            Thread.sleep(5000);
            startActivity(tomain);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    private void dialPhoneNumber(String phoneNumber) {
        try {
            Thread.sleep(7000); // wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}