package com.vinaychitade.rsm.myhealth;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        String userId = order.getUserId();
        String imageUrl = order.getImageUrl();
        boolean isPendingOrder = order.isPending();
        String orderId = order.getOrderId();
        String pushId = order.getPushId(); // Retrieve the pushId

        holder.bindOrderData(userId, imageUrl, isPendingOrder,pushId,orderId);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView userIdTextView;
        private ImageView orderImageView;
        private Button payNowButton;
        private TextView orderIdTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            orderImageView = itemView.findViewById(R.id.orderImageView);
            payNowButton = itemView.findViewById(R.id.payNowButton);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
        }

        public void bindOrderData(String userId, String imageUrl, boolean isPendingOrder, String pushId,String orderId) {
            userIdTextView.setText(userId);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.useract)
                    .into(orderImageView);
            orderIdTextView.setText("Order ID: " + orderId);

            if (isPendingOrder) {
                payNowButton.setVisibility(View.VISIBLE);
                payNowButton.setOnClickListener(v -> {
                    // Step 1: Redirect to CheckoutActivity
                    Intent intent = new Intent(context, CheckOutActivity.class);
                    context.startActivity(intent);
                    Toast.makeText(context, "push=" + pushId, Toast.LENGTH_SHORT).show();
                    // Step 2: Send a request to your server to move the order
                    // from Pending Orders to Shipped Orders
                    moveOrderToShipped(pushId);
                });
            } else {
                payNowButton.setVisibility(View.GONE);
            }
        }

        private void moveOrderToShipped(String pushId) { // Change parameter name to pushId
            // Create an AsyncTask to perform the HTTP request in the background
            if (pushId != null && !pushId.isEmpty()) {
                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            String pushId = params[0]; // Change variable name to pushId
                            Log.d("MyAsyncTask", "Async pushId: " + pushId); // Update log message

                            // Create a JSON object with the pushId
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

                            // Send the HTTP request
                            Response response = client.newCall(request).execute();

                            // Check the response code
                            if (response.isSuccessful()) {
                                // Success: The order has been moved
                            } else {
                                // Handle the error
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(pushId); // Pass pushId as a parameter
            } else {
                // Handle the case where pushId is empty or null
            }
        }


    }
}
