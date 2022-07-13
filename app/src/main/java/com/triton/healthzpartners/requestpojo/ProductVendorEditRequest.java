package com.triton.healthzpartners.requestpojo;

import java.util.List;

public class ProductVendorEditRequest {


    /**
     * product_img : [{"product_img":"http://google.png"},{"product_img":"http://google.png"}]
     * addition_detail : ["testing1","testing2","testing3"]
     * _id : 61a614dcd681bf5291cb9b34
     * user_id : 6198b507518ad4520ab14790
     * cat_id : 6198b507518ad4520ab14790
     * thumbnail_image : http://google.png
     * product_name : Good Food
     * cost : 200
     * product_discription : This is good food
     * condition : Testing
     * price_type : Testing
     * date_and_time : 23-10-2021 11:00 AM
     * threshould : 1000
     * mobile_type : Admin
     * related :
     * count : 0
     * status : true
     * verification_status : Not Verified
     * delete_status : false
     * fav_status : false
     * today_deal : false
     * discount : 0
     * discount_amount : 0
     * discount_status : false
     * discount_cal : 0
     * discount_start_date :
     * discount_end_date :
     * product_rating : 5
     * product_review : 0
     */

    private String _id;
    private String user_id;
    private String cat_id;
    private String thumbnail_image;
    private String product_name;
    private int cost;
    private String product_discription;
    private String condition;
    private String price_type;
    private String date_and_time;
    private String threshould;
    private String mobile_type;
    private String related;
    private int count;
    private String status;
    private String verification_status;
    private boolean delete_status;
    private boolean fav_status;
    private boolean today_deal;
    private int discount;
    private int discount_amount;
    private boolean discount_status;
    private int discount_cal;
    private String discount_start_date;
    private String discount_end_date;
    private int product_rating;
    private int product_review;
    /**
     * product_img : http://google.png
     */

    private List<ProductImgBean> product_img;
    private List<String> addition_detail;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getProduct_discription() {
        return product_discription;
    }

    public void setProduct_discription(String product_discription) {
        this.product_discription = product_discription;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }

    public String getThreshould() {
        return threshould;
    }

    public void setThreshould(String threshould) {
        this.threshould = threshould;
    }

    public String getMobile_type() {
        return mobile_type;
    }

    public void setMobile_type(String mobile_type) {
        this.mobile_type = mobile_type;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerification_status() {
        return verification_status;
    }

    public void setVerification_status(String verification_status) {
        this.verification_status = verification_status;
    }

    public boolean isDelete_status() {
        return delete_status;
    }

    public void setDelete_status(boolean delete_status) {
        this.delete_status = delete_status;
    }

    public boolean isFav_status() {
        return fav_status;
    }

    public void setFav_status(boolean fav_status) {
        this.fav_status = fav_status;
    }

    public boolean isToday_deal() {
        return today_deal;
    }

    public void setToday_deal(boolean today_deal) {
        this.today_deal = today_deal;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(int discount_amount) {
        this.discount_amount = discount_amount;
    }

    public boolean isDiscount_status() {
        return discount_status;
    }

    public void setDiscount_status(boolean discount_status) {
        this.discount_status = discount_status;
    }

    public int getDiscount_cal() {
        return discount_cal;
    }

    public void setDiscount_cal(int discount_cal) {
        this.discount_cal = discount_cal;
    }

    public String getDiscount_start_date() {
        return discount_start_date;
    }

    public void setDiscount_start_date(String discount_start_date) {
        this.discount_start_date = discount_start_date;
    }

    public String getDiscount_end_date() {
        return discount_end_date;
    }

    public void setDiscount_end_date(String discount_end_date) {
        this.discount_end_date = discount_end_date;
    }

    public int getProduct_rating() {
        return product_rating;
    }

    public void setProduct_rating(int product_rating) {
        this.product_rating = product_rating;
    }

    public int getProduct_review() {
        return product_review;
    }

    public void setProduct_review(int product_review) {
        this.product_review = product_review;
    }

    public List<ProductImgBean> getProduct_img() {
        return product_img;
    }

    public void setProduct_img(List<ProductImgBean> product_img) {
        this.product_img = product_img;
    }

    public List<String> getAddition_detail() {
        return addition_detail;
    }

    public void setAddition_detail(List<String> addition_detail) {
        this.addition_detail = addition_detail;
    }

    public static class ProductImgBean {
        private String product_img;

        public String getProduct_img() {
            return product_img;
        }

        public void setProduct_img(String product_img) {
            this.product_img = product_img;
        }
    }
}
