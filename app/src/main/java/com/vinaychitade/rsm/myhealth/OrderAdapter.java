package com.vinaychitade.rsm.myhealth;

import android.content.Context;
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

import com.squareup.picasso.Picasso;
import com.vinaychitade.rsm.myhealth.Order;

import java.util.List;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private PayNowClickListener payNowClickListener;

    public OrderAdapter(List<Order> orderList, Context context, PayNowClickListener payNowClickListener) {
        this.orderList = orderList;
        this.context = context;
        this.payNowClickListener = payNowClickListener;
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
            txtbillamt = itemView.findViewById(R.id.txtbillamt);
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
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                payNowButton.setVisibility(View.GONE);
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                payNowButton.setVisibility(View.VISIBLE);
                payNowButton.setOnClickListener(v -> {
                    Toast.makeText(context, "push=" + pushId, Toast.LENGTH_SHORT).show();
                    payNowClickListener.onPayNowClicked(order);
                });
            }

            if (isPendingOrder) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
                payNowButton.setVisibility(View.VISIBLE);
            } else {
                payNowButton.setVisibility(View.GONE);
            }
        }
    }

    public interface PayNowClickListener {
        void onPayNowClicked(Order order);
    }
}
