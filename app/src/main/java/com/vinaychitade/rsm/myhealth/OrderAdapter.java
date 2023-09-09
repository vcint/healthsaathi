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
import androidx.core.content.ContextCompat;
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
        holder.bindOrderData(order);
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
        private TextView txtbillamt;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            orderImageView = itemView.findViewById(R.id.orderImageView);
            payNowButton = itemView.findViewById(R.id.payNowButton);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            txtbillamt=itemView.findViewById(R.id.txtbillamt);
        }

        public void bindOrderData(Order order) {
            String userId = order.getUserId();
            String imageUrl = order.getImageUrl();
            String orderId = order.getOrderId();
            boolean isPendingOrder = order.isPending();
            String pushId = order.getPushId();

            userIdTextView.setText(userId);
            txtbillamt.setText("Bill Amount: ₹" + order.getBillAmount());

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.useract)
                    .into(orderImageView);

            orderIdTextView.setText("Order ID: " + orderId);

            boolean isShippedOrder = order.isShipped();

            if (isShippedOrder) {
                // Set the background color to green for shipped orders
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                payNowButton.setVisibility(View.GONE);
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                payNowButton.setVisibility(View.VISIBLE);
                payNowButton.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CheckOutActivity.class);
                    context.startActivity(intent);
                    Toast.makeText(context, "push=" + pushId, Toast.LENGTH_SHORT).show();
                    moveOrderToShipped(pushId);
                });
            }


            if (isPendingOrder) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
                payNowButton.setVisibility(View.VISIBLE);
                payNowButton.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CheckOutActivity.class);
                    context.startActivity(intent);
                    Toast.makeText(context, "push=" + pushId, Toast.LENGTH_SHORT).show();
                    moveOrderToShipped(pushId);
                });
            } else {
                payNowButton.setVisibility(View.GONE);
            }
        }

        private void moveOrderToShipped(String pushId) {
            //AsyncTask to perform the HTTP request in the background
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
                            } else {
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(pushId);
            } else {
            }
        }
    }
}
