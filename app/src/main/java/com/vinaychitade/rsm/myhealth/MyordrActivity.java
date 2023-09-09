package com.vinaychitade.rsm.myhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyordrActivity extends AppCompatActivity {
    ImageView imageView2, imageView3, imageView4;
    Button Backbtn;
    String pushId;
    private DatabaseReference ordersRef;
    private List<Order> ordersList;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myordr);

        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        Backbtn=findViewById(R.id.Backbtn);

        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Backtomyprof = new Intent(MyordrActivity.this, MyProfilebtnActivity.class);
                startActivity(Backtomyprof);
                finish();
            }
        });



        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        ordersRef = firebaseDatabase.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        ordersList = new ArrayList<>();
        orderAdapter = new OrderAdapter(ordersList,this);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);

        // Retrieve and display orders
        retrieveOrders();


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadredirect = new Intent(MyordrActivity.this, UploadprescripActivity.class);
                startActivity(uploadredirect);
                finish();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hometocalldoctor = new Intent(MyordrActivity.this, CalldoctorActivity.class);
                startActivity(hometocalldoctor);
                finish();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Myprofile = new Intent(MyordrActivity.this, MyProfilebtnActivity.class);
                startActivity(Myprofile);
                finish();
            }
        });
    }

    private void retrieveOrders() {
        String currentUserEmail = getCurrentUserEmail();
        ordersRef.child("orders").orderByChild("userId").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ordersList.clear();
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            String userId = orderSnapshot.child("userId").getValue(String.class);
                            String imageUrl = orderSnapshot.child("imageUrl").getValue(String.class);
                            boolean isPendingOrder = false;
                            boolean isShippedOrder=false;
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String billAmount = orderSnapshot.child("billAmount").getValue(String.class);

                            ordersList.add(new Order(userId, imageUrl, isPendingOrder,isShippedOrder,orderId,pushId,billAmount));
                        }
                        orderAdapter.notifyDataSetChanged();
                        retrievePendingOrders();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void retrievePendingOrders() {
        String currentUserEmail = getCurrentUserEmail();

        ordersRef.child("pending_orders").orderByChild("userId").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            String userId = orderSnapshot.child("userId").getValue(String.class);
                            String imageUrl = orderSnapshot.child("imageUrl").getValue(String.class);
                            boolean isPendingOrder = true;
                            boolean isShippedOrder=false;
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String pushId = orderSnapshot.getKey();
                            String billAmount = orderSnapshot.child("billAmount").getValue(String.class);
                            ordersList.add(0, new Order(userId, imageUrl, isPendingOrder,isShippedOrder,orderId,pushId,billAmount)); // Add at the beginning
                        }
                        orderAdapter.notifyDataSetChanged();
                        retrieveShippedOrders();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    private void retrieveShippedOrders() {
        String currentUserEmail = getCurrentUserEmail();

        ordersRef.child("shipped_orders").orderByChild("userId").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            String userId = orderSnapshot.child("userId").getValue(String.class);
                            String imageUrl = orderSnapshot.child("imageUrl").getValue(String.class);
                            boolean ispendingOrder = false;
                            boolean isShippedOrder=true;
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String pushId = orderSnapshot.getKey();
                            String billAmount = orderSnapshot.child("billAmount").getValue(String.class);
                            ordersList.add(0, new Order(userId, imageUrl, ispendingOrder,isShippedOrder,orderId,pushId,billAmount)); // Add at the beginning
                        }
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user=currentUser.getEmail();
        return user;
    }
}