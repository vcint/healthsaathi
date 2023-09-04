package com.vinaychitade.rsm.myhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckOutActivity extends AppCompatActivity {
    Button btntoorders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        btntoorders=findViewById(R.id.btntoorders);
        btntoorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadredirect = new Intent(CheckOutActivity.this, MyordrActivity.class);
                startActivity(uploadredirect);
                finish();
            }
        });
    }
}