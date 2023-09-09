package com.vinaychitade.rsm.myhealth;

public class Order {
    private String userId;
    private String imageUrl;
    private boolean pending;
    private String orderId;
    private String pushId;
    private boolean shipped;
    private String billAmount;

    public Order(String userId, String imageUrl, boolean pending,boolean shipped, String orderId,String pushId,String billAmount) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.pending = pending;
        this.orderId = orderId;
        this.pushId=pushId;
        this.shipped=shipped;
        this.billAmount = billAmount;

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
    public boolean isShipped(){return shipped;}

    public String getOrderId() {
        return orderId;
    }

    public String getPushId() {
        return pushId;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

}
