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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.admin.v1.Index;

public class UploadprescripActivity extends AppCompatActivity {
    ImageView previewprescrip;
    Button uploadcnf, canceluploadbtn;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadprescrip);
        previewprescrip = findViewById(R.id.previewprescrip);
        uploadcnf = findViewById(R.id.uploadcnf);
        canceluploadbtn = findViewById(R.id.canceluploadbtn);
        FirebaseApp.initializeApp(this);

        Toast.makeText(this, "Please Select Desired Option", Toast.LENGTH_SHORT).show();
        ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();

        uploadcnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUploadButtonClick(view);
            }
        });

        canceluploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelButtonClick(view);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            selectedImageUri = data.getData();
            previewprescrip.setImageURI(selectedImageUri);
        }
    }

    public void onUploadButtonClick(View view) {
        if (selectedImageUri != null) {
            uploadImageToRealtimeDatabase(selectedImageUri);
        } else {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToRealtimeDatabase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String imageFileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + imageFileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL to Database
                        String downloadUrl = uri.toString();

                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getEmail();
                            String userName = currentUser.getDisplayName();

                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ordersRef = databaseRef.child("orders").push();
                            ordersRef.child("userId").setValue(userId);
                            ordersRef.child("userName").setValue(userName);
                            ordersRef.child("imageUrl").setValue(downloadUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, " Order Placed successfully", Toast.LENGTH_SHORT).show();
                                        Intent ordersuccess= new Intent(UploadprescripActivity.this,UploadedImage.class);
                                        startActivity(ordersuccess);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        }


    public void onCancelButtonClick(View view) {
        Intent uploadtomain = new Intent(UploadprescripActivity.this, MainActivity.class);
        startActivity(uploadtomain);
        finish();
    }
}