package com.triton.healthZpartners.requestpojo;

public class RefundCouponCreateRequest {

    /**
     * created_by : User
     * coupon_type : 1
     * code : REF100
     * amount : 100
     * user_details : 123123
     * used_status : Not Used
     * mobile_type : "Android"
     */

    private String created_by;
    private String coupon_type;
    private String code;
    private int amount;
    private String user_details;
    private String used_status;
    private String mobile_type;

    public String getMobile_type() {
        return mobile_type;
    }

    public void setMobile_type(String mobile_type) {
        this.mobile_type = mobile_type;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUser_details() {
        return user_details;
    }

    public void setUser_details(String user_details) {
        this.user_details = user_details;
    }

    public String getUsed_status() {
        return used_status;
    }

    public void setUsed_status(String used_status) {
        this.used_status = used_status;
    }
}
