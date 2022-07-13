package com.triton.healthzpartners.responsepojo;

import java.util.List;

public class PetLoverVendorOrderDetailsResponse {


    /**
     * Status : Success
     * Message : Petlover Product Grouped
     * Data : {"order_details":{"order_id":"ORDER-1633602094600","order_product":1,"order_status":"New","order_text":"Food Products","order_payment_id":"pay_I6Pa8NaEkeDkda","order_price":500,"order_booked":"07-10-2021 03:51 PM","order_image":"http://54.212.108.156:3000/api/uploads/1625747987678.png","order_cancelled":"","order_completed":"","coupon_status":"","coupon_code":"","original_price":0,"coupon_discount_price":0,"total_price":500},"shipping_address":{"location_title":"Home","shipping_location":"Karatampatti Iluppaiyur road, Iluppaiyur, Tamil Nadu 621006, India","user_name":"Priyanka Nandikolmat","user_phone":"9879879877","user_lat":11.064702232611461,"user_long":78.63427575677635},"product_details":[{"product_id":0,"product_image":"http://54.212.108.156:3000/api/uploads/1625747987678.png","product_name":"Organic Anti-Tick and Flea Spray for Dogs","product_count":1,"product_price":500,"product_discount":0,"product_total_price":500,"product_total_discount":0,"product_stauts":"Order Booked","product_booked":"07-10-2021 03:51 PM"}]}
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * order_details : {"order_id":"ORDER-1633602094600","order_product":1,"order_status":"New","order_text":"Food Products","order_payment_id":"pay_I6Pa8NaEkeDkda","order_price":500,"order_booked":"07-10-2021 03:51 PM","order_image":"http://54.212.108.156:3000/api/uploads/1625747987678.png","order_cancelled":"","order_completed":"","coupon_status":"","coupon_code":"","original_price":0,"coupon_discount_price":0,"total_price":500}
     * shipping_address : {"location_title":"Home","shipping_location":"Karatampatti Iluppaiyur road, Iluppaiyur, Tamil Nadu 621006, India","user_name":"Priyanka Nandikolmat","user_phone":"9879879877","user_lat":11.064702232611461,"user_long":78.63427575677635}
     * product_details : [{"product_id":0,"product_image":"http://54.212.108.156:3000/api/uploads/1625747987678.png","product_name":"Organic Anti-Tick and Flea Spray for Dogs","product_count":1,"product_price":500,"product_discount":0,"product_total_price":500,"product_total_discount":0,"product_stauts":"Order Booked","product_booked":"07-10-2021 03:51 PM"}]
     */

    private DataBean Data;
    private int Code;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public static class DataBean {
        /**
         * order_id : ORDER-1633602094600
         * order_product : 1
         * order_status : New
         * order_text : Food Products
         * order_payment_id : pay_I6Pa8NaEkeDkda
         * order_price : 500
         * order_booked : 07-10-2021 03:51 PM
         * order_image : http://54.212.108.156:3000/api/uploads/1625747987678.png
         * order_cancelled :
         * order_completed :
         * coupon_status :
         * coupon_code :
         * original_price : 0
         * coupon_discount_price : 0
         * total_price : 500
         */

        private OrderDetailsBean order_details;
        /**
         * location_title : Home
         * shipping_location : Karatampatti Iluppaiyur road, Iluppaiyur, Tamil Nadu 621006, India
         * user_name : Priyanka Nandikolmat
         * user_phone : 9879879877
         * user_lat : 11.064702232611461
         * user_long : 78.63427575677635
         */

        private ShippingAddressBean shipping_address;
        /**
         * product_id : 0
         * product_image : http://54.212.108.156:3000/api/uploads/1625747987678.png
         * product_name : Organic Anti-Tick and Flea Spray for Dogs
         * product_count : 1
         * product_price : 500
         * product_discount : 0
         * product_total_price : 500
         * product_total_discount : 0
         * product_stauts : Order Booked
         * product_booked : 07-10-2021 03:51 PM
         */

        private List<ProductDetailsBean> product_details;

        public OrderDetailsBean getOrder_details() {
            return order_details;
        }

        public void setOrder_details(OrderDetailsBean order_details) {
            this.order_details = order_details;
        }

        public ShippingAddressBean getShipping_address() {
            return shipping_address;
        }

        public void setShipping_address(ShippingAddressBean shipping_address) {
            this.shipping_address = shipping_address;
        }

        public List<ProductDetailsBean> getProduct_details() {
            return product_details;
        }

        public void setProduct_details(List<ProductDetailsBean> product_details) {
            this.product_details = product_details;
        }

        public static class OrderDetailsBean {
            private String order_id;
            private int order_product;
            private String order_status;
            private String order_text;
            private String order_payment_id;
            private int order_price;
            private String order_booked;
            private String order_image;
            private String order_cancelled;
            private String order_completed;
            private String coupon_status;
            private String coupon_code;
            private int original_price;
            private int coupon_discount_price;
            private int total_price;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public int getOrder_product() {
                return order_product;
            }

            public void setOrder_product(int order_product) {
                this.order_product = order_product;
            }

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public String getOrder_text() {
                return order_text;
            }

            public void setOrder_text(String order_text) {
                this.order_text = order_text;
            }

            public String getOrder_payment_id() {
                return order_payment_id;
            }

            public void setOrder_payment_id(String order_payment_id) {
                this.order_payment_id = order_payment_id;
            }

            public int getOrder_price() {
                return order_price;
            }

            public void setOrder_price(int order_price) {
                this.order_price = order_price;
            }

            public String getOrder_booked() {
                return order_booked;
            }

            public void setOrder_booked(String order_booked) {
                this.order_booked = order_booked;
            }

            public String getOrder_image() {
                return order_image;
            }

            public void setOrder_image(String order_image) {
                this.order_image = order_image;
            }

            public String getOrder_cancelled() {
                return order_cancelled;
            }

            public void setOrder_cancelled(String order_cancelled) {
                this.order_cancelled = order_cancelled;
            }

            public String getOrder_completed() {
                return order_completed;
            }

            public void setOrder_completed(String order_completed) {
                this.order_completed = order_completed;
            }

            public String getCoupon_status() {
                return coupon_status;
            }

            public void setCoupon_status(String coupon_status) {
                this.coupon_status = coupon_status;
            }

            public String getCoupon_code() {
                return coupon_code;
            }

            public void setCoupon_code(String coupon_code) {
                this.coupon_code = coupon_code;
            }

            public int getOriginal_price() {
                return original_price;
            }

            public void setOriginal_price(int original_price) {
                this.original_price = original_price;
            }

            public int getCoupon_discount_price() {
                return coupon_discount_price;
            }

            public void setCoupon_discount_price(int coupon_discount_price) {
                this.coupon_discount_price = coupon_discount_price;
            }

            public int getTotal_price() {
                return total_price;
            }

            public void setTotal_price(int total_price) {
                this.total_price = total_price;
            }
        }

        public static class ShippingAddressBean {
            private String location_title;
            private String shipping_location;
            private String user_name;
            private String user_phone;
            private String land_mark;

            public String getLand_mark() {
                return land_mark;
            }

            public void setLand_mark(String land_mark) {
                this.land_mark = land_mark;
            }

            private double user_lat;
            private double user_long;

            public String getLocation_title() {
                return location_title;
            }

            public void setLocation_title(String location_title) {
                this.location_title = location_title;
            }

            public String getShipping_location() {
                return shipping_location;
            }

            public void setShipping_location(String shipping_location) {
                this.shipping_location = shipping_location;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_phone() {
                return user_phone;
            }

            public void setUser_phone(String user_phone) {
                this.user_phone = user_phone;
            }

            public double getUser_lat() {
                return user_lat;
            }

            public void setUser_lat(double user_lat) {
                this.user_lat = user_lat;
            }

            public double getUser_long() {
                return user_long;
            }

            public void setUser_long(double user_long) {
                this.user_long = user_long;
            }
        }

        public static class ProductDetailsBean {
            private int product_id;
            private String product_image;
            private String product_name;
            private int product_count;
            private int product_price;
            private int product_discount;
            private int product_total_price;
            private int product_total_discount;
            private String product_stauts;
            private String product_booked;

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getProduct_image() {
                return product_image;
            }

            public void setProduct_image(String product_image) {
                this.product_image = product_image;
            }

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public int getProduct_count() {
                return product_count;
            }

            public void setProduct_count(int product_count) {
                this.product_count = product_count;
            }

            public int getProduct_price() {
                return product_price;
            }

            public void setProduct_price(int product_price) {
                this.product_price = product_price;
            }

            public int getProduct_discount() {
                return product_discount;
            }

            public void setProduct_discount(int product_discount) {
                this.product_discount = product_discount;
            }

            public int getProduct_total_price() {
                return product_total_price;
            }

            public void setProduct_total_price(int product_total_price) {
                this.product_total_price = product_total_price;
            }

            public int getProduct_total_discount() {
                return product_total_discount;
            }

            public void setProduct_total_discount(int product_total_discount) {
                this.product_total_discount = product_total_discount;
            }

            public String getProduct_stauts() {
                return product_stauts;
            }

            public void setProduct_stauts(String product_stauts) {
                this.product_stauts = product_stauts;
            }

            public String getProduct_booked() {
                return product_booked;
            }

            public void setProduct_booked(String product_booked) {
                this.product_booked = product_booked;
            }
        }
    }
}