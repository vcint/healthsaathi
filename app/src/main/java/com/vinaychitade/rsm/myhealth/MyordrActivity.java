package com.vinaychitade.rsm.myhealth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vinaychitade.rsm.myhealth.MyProfilebtnActivity;
import com.vinaychitade.rsm.myhealth.Order;
import com.vinaychitade.rsm.myhealth.OrderAdapter;
import com.vinaychitade.rsm.myhealth.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyordrActivity extends AppCompatActivity implements OrderAdapter.PayNowClickListener, PaymentStatusListener {

    ImageView imageView2, imageView3, imageView4;
    Button Backbtn;
    private DatabaseReference ordersRef;
    private List<Order> ordersList;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    private void moveOrderToShipped(String pushId) {
        // AsyncTask to perform the HTTP request in the background
        if (pushId != null && !pushId.isEmpty()) {
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {
                    try {
                        String pushId = params[0];
                        Log.d("MyAsyncTask", "Async pushId: " + pushId);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("pushId", pushId);

                        // OkHttp client setup
                        OkHttpClient client = new OkHttpClient();
                        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
                        Request request = new Request.Builder()
                                .url("https://healthsaathi.onrender.com/shippedOrder")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            // Handle success
                        } else {
                            // Handle failure
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(pushId);
        } else {
            // Handle the case where pushId is null or empty
        }
    }


    ///////
    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        String pushId=transactionDetails.getTransactionRefId();
        Toast.makeText(this, "Pyment Success", Toast.LENGTH_SHORT).show();
        Intent completedorderredirect = new Intent(MyordrActivity.this, CheckOutActivity.class);
        moveOrderToShipped(pushId);
        startActivity(completedorderredirect);
        finish();
    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myordr);

        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        Backbtn = findViewById(R.id.Backbtn);

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
        orderAdapter = new OrderAdapter(ordersList, this, this);

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
                            String pushId = orderSnapshot.getKey();
                            boolean isPendingOrder = false;
                            boolean isShippedOrder = false;
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String billAmount = orderSnapshot.child("billAmount").getValue(String.class);

                            ordersList.add(new Order(userId, imageUrl, isPendingOrder, isShippedOrder, orderId, pushId, billAmount));
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
                            boolean isShippedOrder = false;
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String pushId = orderSnapshot.getKey();
                            String billAmount = orderSnapshot.child("billAmount").getValue(String.class);
                            ordersList.add(0, new Order(userId, imageUrl, isPendingOrder, isShippedOrder, orderId, pushId, billAmount));
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
                            boolean isPendingOrder = false;
                            boolean isShippedOrder = true;
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String pushId = orderSnapshot.getKey();
                            String billAmount = orderSnapshot.child("billAmount").getValue(String.class);
                            ordersList.add(0, new Order(userId, imageUrl, isPendingOrder, isShippedOrder, orderId, pushId, billAmount));
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
        String user = currentUser.getEmail();
        return user;
    }

    @Override
    public void onPayNowClicked(Order order) {
        // Handle payment initiation using EasyUpiPayment here
        // You can access the order details, including the dynamically calculated bill amount, from the 'order' parameter
        // Create the EasyUpiPayment.Builder and initiate the payment with the dynamic bill amount
        float billAmt= Float.parseFloat(order.getBillAmount());
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .setPayeeVpa("paytmqr2810050501011njvbcz3z7ko@paytm")
                .setPayeeName("paytmqr2810050501011njvbcz3z7ko@paytm")
                .setPayeeMerchantCode("4573")
                .setTransactionId("Tr09102023001")
                .setTransactionRefId("Tr09102023001")
                .setDescription("Your Order From HealthSaathi")
                .setAmount(Float.toString(billAmt));
        try {
            // Build and start the payment
            EasyUpiPayment easyUpiPayment = builder.build();
            easyUpiPayment.startPayment(); // Start the payment
            easyUpiPayment.setPaymentStatusListener(this);
        } catch (AppNotFoundException e) {
            // Handle the exception gracefully
            Toast.makeText(this, "No UPI payment app found. Please install one to make the payment.", Toast.LENGTH_LONG).show();
        }



    }
}
