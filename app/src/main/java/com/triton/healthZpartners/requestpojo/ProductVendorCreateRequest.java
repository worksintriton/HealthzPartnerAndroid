package com.triton.healthZpartners.requestpojo;

import java.util.List;

public class ProductVendorCreateRequest {


    /**
     * vendor_id : 6198b507518ad4520ab14790
     * cat_id : 6198b507518ad4520ab14790
     * thumbnail_image : http://google.png
     * product_img : [{"product_img":"http://google.png"},{"product_img":"http://google.png"}]
     * product_name : Good Food
     * cost : 200
     * product_discription : This is good food
     * condition : Testing
     * price_type : Testing
     * addition_detail : ["testing1","testing2","testing3"]
     * date_and_time : 23-10-2021 11:00 AM
     * threshould : 1000
     * mobile_type : Admin
     */

    private String vendor_id;
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
    /**
     * product_img : http://google.png
     */

    private List<ProductImgBean> product_img;
    private List<String> addition_detail;

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
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

