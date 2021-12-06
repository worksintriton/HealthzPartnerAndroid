package com.triton.healthZpartners.responsepojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageProductsListResponse {

    /**
     * Status : Success
     * Message : Product details list
     * Data : [{"product_img":[{"product_img":"http://35.164.43.170:3000/api/uploads/1638435359547.jpg"}],"addition_detail":["yes"],"_id":"61a88aad02651d5e799071a5","user_id":"61a503503052a016fed7bb94","cat_id":{"_id":"60e6ffbafe7500511a7b419d","img_path":"http://54.212.108.156:3000/api/uploads/1625751478722.png","product_cate":"Pet Accessories","img_index":0,"show_status":true,"date_and_time":"7/8/2021, 7:08:01 PM","delete_status":false,"updatedAt":"2021-07-08T13:38:02.721Z","createdAt":"2021-07-08T13:38:02.721Z","__v":0},"thumbnail_image":"http://35.164.43.170:3000/api/uploads/1638435359547.jpg","product_name":"","cost":200,"product_discription":"Pet Accessories","condition":"organic","price_type":"basic","date_and_time":"02/12/2021 02:28 PM","threshould":"66","mobile_type":"Android","related":"","count":0,"status":"true","verification_status":"Not Verified","delete_status":false,"fav_status":false,"today_deal":false,"discount":0,"discount_amount":0,"discount_status":false,"discount_cal":0,"discount_start_date":"","discount_end_date":"","product_rating":5,"product_review":0,"updatedAt":"2021-12-02T08:58:21.274Z","createdAt":"2021-12-02T08:58:21.274Z","__v":0},{"product_img":[{"product_img":"http://35.164.43.170:3000/api/uploads/1638447394865.jpg"}],"addition_detail":["sample"],"_id":"61a8b97902651d5e799071a8","user_id":"61a503503052a016fed7bb94","cat_id":{"_id":"6198b572518ad4520ab14791","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Women Care","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:13:20 PM","delete_status":false,"updatedAt":"2021-11-20T08:44:34.670Z","createdAt":"2021-11-20T08:44:34.670Z","__v":0},"thumbnail_image":"http://35.164.43.170:3000/api/uploads/1638447394865.jpg","product_name":"Sample","cost":200,"product_discription":"Women Care","condition":"sample","price_type":"unit","date_and_time":"02/12/2021 05:47 PM","threshould":"66","mobile_type":"Android","related":"","count":0,"status":"true","verification_status":"Not Verified","delete_status":false,"fav_status":false,"today_deal":false,"discount":0,"discount_amount":0,"discount_status":false,"discount_cal":0,"discount_start_date":"","discount_end_date":"","product_rating":5,"product_review":0,"updatedAt":"2021-12-02T12:18:01.399Z","createdAt":"2021-12-02T12:18:01.399Z","__v":0}]
     * Code : 200
     */

    private String Status;
    private String Message;
    private int Code;
    /**
     * product_img : [{"product_img":"http://35.164.43.170:3000/api/uploads/1638435359547.jpg"}]
     * addition_detail : ["yes"]
     * _id : 61a88aad02651d5e799071a5
     * user_id : 61a503503052a016fed7bb94
     * cat_id : {"_id":"60e6ffbafe7500511a7b419d","img_path":"http://54.212.108.156:3000/api/uploads/1625751478722.png","product_cate":"Pet Accessories","img_index":0,"show_status":true,"date_and_time":"7/8/2021, 7:08:01 PM","delete_status":false,"updatedAt":"2021-07-08T13:38:02.721Z","createdAt":"2021-07-08T13:38:02.721Z","__v":0}
     * thumbnail_image : http://35.164.43.170:3000/api/uploads/1638435359547.jpg
     * product_name :
     * cost : 200
     * product_discription : Pet Accessories
     * condition : organic
     * price_type : basic
     * date_and_time : 02/12/2021 02:28 PM
     * threshould : 66
     * mobile_type : Android
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
     * updatedAt : 2021-12-02T08:58:21.274Z
     * createdAt : 2021-12-02T08:58:21.274Z
     * __v : 0
     */

    private List<DataBean> Data;

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

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean implements Serializable {
        private String _id;
        private String user_id;
        /**
         * _id : 60e6ffbafe7500511a7b419d
         * img_path : http://54.212.108.156:3000/api/uploads/1625751478722.png
         * product_cate : Pet Accessories
         * img_index : 0
         * show_status : true
         * date_and_time : 7/8/2021, 7:08:01 PM
         * delete_status : false
         * updatedAt : 2021-07-08T13:38:02.721Z
         * createdAt : 2021-07-08T13:38:02.721Z
         * __v : 0
         */

        private CatIdBean cat_id;
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
        private String updatedAt;
        private String createdAt;
        private int __v;
        /**
         * product_img : http://35.164.43.170:3000/api/uploads/1638435359547.jpg
         */

        private ArrayList<ProductImgBean> product_img;
        private ArrayList<String> addition_detail;

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

        public CatIdBean getCat_id() {
            return cat_id;
        }

        public void setCat_id(CatIdBean cat_id) {
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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public ArrayList<ProductImgBean> getProduct_img() {
            return product_img;
        }

        public void setProduct_img(ArrayList<ProductImgBean> product_img) {
            this.product_img = product_img;
        }

        public ArrayList<String> getAddition_detail() {
            return addition_detail;
        }

        public void setAddition_detail(ArrayList<String> addition_detail) {
            this.addition_detail = addition_detail;
        }

        public static class CatIdBean {
            private String _id;
            private String img_path;
            private String product_cate;
            private int img_index;
            private boolean show_status;
            private String date_and_time;
            private boolean delete_status;
            private String updatedAt;
            private String createdAt;
            private int __v;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getImg_path() {
                return img_path;
            }

            public void setImg_path(String img_path) {
                this.img_path = img_path;
            }

            public String getProduct_cate() {
                return product_cate;
            }

            public void setProduct_cate(String product_cate) {
                this.product_cate = product_cate;
            }

            public int getImg_index() {
                return img_index;
            }

            public void setImg_index(int img_index) {
                this.img_index = img_index;
            }

            public boolean isShow_status() {
                return show_status;
            }

            public void setShow_status(boolean show_status) {
                this.show_status = show_status;
            }

            public String getDate_and_time() {
                return date_and_time;
            }

            public void setDate_and_time(String date_and_time) {
                this.date_and_time = date_and_time;
            }

            public boolean isDelete_status() {
                return delete_status;
            }

            public void setDelete_status(boolean delete_status) {
                this.delete_status = delete_status;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }
        }

        public static class ProductImgBean implements Serializable {
            private String product_img;

            public String getProduct_img() {
                return product_img;
            }

            public void setProduct_img(String product_img) {
                this.product_img = product_img;
            }
        }
    }
}
