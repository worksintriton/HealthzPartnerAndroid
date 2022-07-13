package com.triton.healthzpartners.responsepojo;

import java.util.List;

public class TrackOrderDetailsResponse {


    /**
     * Status : Success
     * Message : Vendor single product detail
     * Data : {"product_id":0,"product_image":{"product_img":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg"},"product_name":"Sample","product_count":1,"product_price":192,"product_discount":1,"product_total_price":192,"product_total_discount":1,"product_stauts":"Order Dispatch","product_booked":"10-12-2021 12:53 PM","prodcut_track_details":[{"id":0,"title":"Order Booked","date":"10-12-2021 12:53 PM","text":"","Status":true},{"id":1,"title":"Order Accept","date":"10-12-2021 12:54 PM","text":"Vendor Accept the order","Status":true},{"id":2,"title":"Order Dispatch","date":"10-12-2021 12:55 PM","text":"123456","Status":true},{"id":3,"title":"In Transit","date":"10-12-2021 12:55 PM","text":"Vendor Dispatch the order","Status":true},{"id":4,"title":"Order Cancelled","date":"","text":"","Status":false},{"id":5,"title":"Vendor cancelled","date":"","text":"","Status":false}]}
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * product_id : 0
     * product_image : {"product_img":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg"}
     * product_name : Sample
     * product_count : 1
     * product_price : 192
     * product_discount : 1
     * product_total_price : 192
     * product_total_discount : 1
     * product_stauts : Order Dispatch
     * product_booked : 10-12-2021 12:53 PM
     * prodcut_track_details : [{"id":0,"title":"Order Booked","date":"10-12-2021 12:53 PM","text":"","Status":true},{"id":1,"title":"Order Accept","date":"10-12-2021 12:54 PM","text":"Vendor Accept the order","Status":true},{"id":2,"title":"Order Dispatch","date":"10-12-2021 12:55 PM","text":"123456","Status":true},{"id":3,"title":"In Transit","date":"10-12-2021 12:55 PM","text":"Vendor Dispatch the order","Status":true},{"id":4,"title":"Order Cancelled","date":"","text":"","Status":false},{"id":5,"title":"Vendor cancelled","date":"","text":"","Status":false}]
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
        private int product_id;
        /**
         * product_img : http://35.164.43.170:3000/api/uploads/1638874408182.jpg
         */

        private ProductImageBean product_image;
        private String product_name;
        private int product_count;
        private int product_price;
        private int product_discount;
        private int product_total_price;
        private int product_total_discount;
        private String product_stauts;
        private String product_booked;
        /**
         * id : 0
         * title : Order Booked
         * date : 10-12-2021 12:53 PM
         * text :
         * Status : true
         */

        private List<ProdcutTrackDetailsBean> prodcut_track_details;

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public ProductImageBean getProduct_image() {
            return product_image;
        }

        public void setProduct_image(ProductImageBean product_image) {
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

        public List<ProdcutTrackDetailsBean> getProdcut_track_details() {
            return prodcut_track_details;
        }

        public void setProdcut_track_details(List<ProdcutTrackDetailsBean> prodcut_track_details) {
            this.prodcut_track_details = prodcut_track_details;
        }

        public static class ProductImageBean {
            private String product_img;

            public String getProduct_img() {
                return product_img;
            }

            public void setProduct_img(String product_img) {
                this.product_img = product_img;
            }
        }

        public static class ProdcutTrackDetailsBean {
            private int id;
            private String title;
            private String date;
            private String text;
            private boolean Status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public boolean isStatus() {
                return Status;
            }

            public void setStatus(boolean Status) {
                this.Status = Status;
            }
        }
    }
}
