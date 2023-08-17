package com.vinaychitade.rsm.myhealth;

public class Order {
    private String userId;
    private String imageUrl;
    private boolean pending;

    public Order(String userId, String imageUrl, boolean pending) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.pending = pending;
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
}
