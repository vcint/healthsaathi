package com.vinaychitade.rsm.myhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CalldoctorActivity extends AppCompatActivity {
    ImageView imageView2, imageView4;
    private static final int REQUEST_PHONE_CALL = 1;
    private boolean isCallEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calldoctor);
        imageView2 = findViewById(R.id.imageView2);
        imageView4 = findViewById(R.id.imageView4);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tomainact = new Intent(CalldoctorActivity.this, MainActivity.class);
                startActivity(tomainact);
                finish();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Myprofile = new Intent(CalldoctorActivity.this, MyProfilebtnActivity.class);
                startActivity(Myprofile);
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            makePhoneCall();
        }
    }

    private void makePhoneCall() {
        Toast.makeText(this, "Calling Doctor", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "+917972791880"));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                // Handle permission denied
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                if (state == TelephonyManager.CALL_STATE_IDLE && isCallEnded) {
                    //if call is going on
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // Call ended
                    isCallEnded = true;
                    Intent tohome = new Intent(CalldoctorActivity.this, MainActivity.class);
                    startActivity(tohome);
                    finish();
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}



