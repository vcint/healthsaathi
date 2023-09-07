package com.vinaychitade.rsm.myhealth;

public class Order {
    private String userId;
    private String imageUrl;
    private boolean pending;
    private String orderId;
    private String pushId;

    public Order(String userId, String imageUrl, boolean pending, String orderId,String pushId) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.pending = pending;
        this.orderId = orderId; // Set the orderId in the constructor
        this.pushId=pushId;

    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isPending() {
        return pending;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPushId() {
        return pushId;
    }


    // Add a setter method for orderId
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
