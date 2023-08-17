package com.vinaychitade.rsm.myhealth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
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

        // Bind the order data to the ViewHolder's views
        holder.bindOrderData(userId, imageUrl, isPendingOrder);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView userIdTextView;
        private ImageView orderImageView;
        private Button payNowButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            orderImageView = itemView.findViewById(R.id.orderImageView);
            payNowButton = itemView.findViewById(R.id.payNowButton);
        }

        public void bindOrderData(String userId, String imageUrl, boolean isPendingOrder) {
            userIdTextView.setText(userId);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.useract)
                    .into(orderImageView);

            if (isPendingOrder) {
                payNowButton.setVisibility(View.VISIBLE);
            } else {
                payNowButton.setVisibility(View.GONE);
            }

            payNowButton.setOnClickListener(v -> {
                // Implement the payment functionality and move the order to "Shipped Orders"
            });
        }
    }
}