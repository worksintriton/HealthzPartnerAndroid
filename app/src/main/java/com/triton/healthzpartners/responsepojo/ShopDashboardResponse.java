package com.triton.healthzpartners.responsepojo;

import java.util.List;

public class ShopDashboardResponse {


    /**
     * Status : Success
     * Message : product list
     * Data : {"Banner_details":[{"banner_img":"http://35.164.43.170:3000/api/uploads/1638611849928.png","banner_title":"."},{"banner_img":"http://35.164.43.170:3000/api/uploads/1638611837867.png","banner_title":"."},{"banner_img":"http://35.164.43.170:3000/api/uploads/1638611775742.png","banner_title":"."}],"Today_Special":[{"_id":"61af3d669e6f552291dbfa6e","product_img":{"product_img":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg"},"product_title":"Sample","thumbnail_image":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg","product_price":192,"product_discount":1,"product_discount_price":194,"product_fav":false,"product_rating":5,"product_review":0}],"Product_details":[{"cat_id":"6198b594518ad4520ab14792","cat_name":"Organic Products","product_list":[{"_id":"61af3d669e6f552291dbfa6e","product_img":{"product_img":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg"},"product_title":"Sample","thumbnail_image":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg","product_price":192,"product_discount":1,"product_discount_price":194,"product_fav":false,"product_rating":5,"product_review":0}]}],"product_cate":[{"_id":"6198b5e1518ad4520ab14796","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Health Care","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:42:23 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:12:24.711Z","createdAt":"2021-11-20T08:46:25.853Z","__v":0},{"_id":"6198b5cc518ad4520ab14795","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Baby Products","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:14:49 PM","delete_status":false,"updatedAt":"2021-11-20T08:46:04.099Z","createdAt":"2021-11-20T08:46:04.099Z","__v":0},{"_id":"6198b5b5518ad4520ab14794","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Beauty Products","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:42:31 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:12:32.419Z","createdAt":"2021-11-20T08:45:41.571Z","__v":0},{"_id":"6198b5a2518ad4520ab14793","img_path":"http://35.164.43.170:3000/api/uploads/1638787069561.jpg","product_cate":"Men Care","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:41:46 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:11:46.820Z","createdAt":"2021-11-20T08:45:22.893Z","__v":0},{"_id":"6198b594518ad4520ab14792","img_path":"http://35.164.43.170:3000/api/uploads/1638787048263.jpg","product_cate":"Organic Products","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:41:39 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:11:40.266Z","createdAt":"2021-11-20T08:45:08.260Z","__v":0},{"_id":"6198b572518ad4520ab14791","img_path":"http://35.164.43.170:3000/api/uploads/1638787013129.jpg","product_cate":"Women Care","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:41:35 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:11:36.373Z","createdAt":"2021-11-20T08:44:34.670Z","__v":0},{"_id":"6198b507518ad4520ab14790","img_path":"http://35.164.43.170:3000/api/uploads/1638787001659.jpg","product_cate":"Skin & Body Care","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:41:31 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:11:32.174Z","createdAt":"2021-11-20T08:42:47.494Z","__v":0},{"_id":"60e6ffbafe7500511a7b419d","img_path":"http://54.212.108.156:3000/api/uploads/1625751478722.png","product_cate":"Pet Accessories","img_index":0,"show_status":true,"date_and_time":"7/8/2021, 7:08:01 PM","delete_status":false,"updatedAt":"2021-07-08T13:38:02.721Z","createdAt":"2021-07-08T13:38:02.721Z","__v":0},{"_id":"5fec22eeea832e2e73c1fc7b","img_path":"http://52.25.163.13:3000/api/uploads/template%20(5).jpg","product_cate":"Pet Grooming","img_index":0,"show_status":true,"date_and_time":"Thu Jul 08 2021 18:13:44 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-07-08T14:06:47.786Z","createdAt":"2020-12-30T06:49:18.019Z","__v":0},{"_id":"5fec1573ea832e2e73c1fc7a","img_path":"http://35.164.43.170:3000/api/uploads/1638786952208.jpg","product_cate":"Bedding","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:41:21 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:11:21.631Z","createdAt":"2020-12-30T05:51:47.787Z","__v":0},{"_id":"5fec14a5ea832e2e73c1fc79","img_path":"http://52.25.163.13:3000/api/uploads/template%20(3).jpg","product_cate":"Pet Foods - Wet","img_index":0,"show_status":true,"date_and_time":"Thu Jul 08 2021 01:55:56 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-07-08T09:39:49.690Z","createdAt":"2020-12-30T05:48:21.363Z","__v":0},{"_id":"5fec1424ea832e2e73c1fc78","img_path":"http://35.164.43.170:3000/api/uploads/1638786852874.jpg","product_cate":"Hair","img_index":0,"show_status":true,"date_and_time":"Mon Dec 06 2021 16:41:28 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-12-06T11:11:28.393Z","createdAt":"2020-12-30T05:46:12.099Z","__v":0}]}
     * Code : 200
     */

    private String Status;
    private String Message;
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
         * banner_img : http://35.164.43.170:3000/api/uploads/1638611849928.png
         * banner_title : .
         */

        private List<BannerDetailsBean> Banner_details;
        /**
         * _id : 61af3d669e6f552291dbfa6e
         * product_img : {"product_img":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg"}
         * product_title : Sample
         * thumbnail_image : http://35.164.43.170:3000/api/uploads/1638874408182.jpg
         * product_price : 192
         * product_discount : 1
         * product_discount_price : 194
         * product_fav : false
         * product_rating : 5
         * product_review : 0
         */

        private List<TodaySpecialBean> Today_Special;
        /**
         * cat_id : 6198b594518ad4520ab14792
         * cat_name : Organic Products
         * product_list : [{"_id":"61af3d669e6f552291dbfa6e","product_img":{"product_img":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg"},"product_title":"Sample","thumbnail_image":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg","product_price":192,"product_discount":1,"product_discount_price":194,"product_fav":false,"product_rating":5,"product_review":0}]
         */

        private List<ProductDetailsBean> Product_details;
        /**
         * _id : 6198b5e1518ad4520ab14796
         * img_path : http://13.57.9.246:3000/api/uploads/1625751478722.png
         * product_cate : Health Care
         * img_index : 0
         * show_status : true
         * date_and_time : Mon Dec 06 2021 16:42:23 GMT+0530 (India Standard Time)
         * delete_status : false
         * updatedAt : 2021-12-06T11:12:24.711Z
         * createdAt : 2021-11-20T08:46:25.853Z
         * __v : 0
         */

        private List<ProductCateBean> product_cate;

        public List<BannerDetailsBean> getBanner_details() {
            return Banner_details;
        }

        public void setBanner_details(List<BannerDetailsBean> Banner_details) {
            this.Banner_details = Banner_details;
        }

        public List<TodaySpecialBean> getToday_Special() {
            return Today_Special;
        }

        public void setToday_Special(List<TodaySpecialBean> Today_Special) {
            this.Today_Special = Today_Special;
        }

        public List<ProductDetailsBean> getProduct_details() {
            return Product_details;
        }

        public void setProduct_details(List<ProductDetailsBean> Product_details) {
            this.Product_details = Product_details;
        }

        public List<ProductCateBean> getProduct_cate() {
            return product_cate;
        }

        public void setProduct_cate(List<ProductCateBean> product_cate) {
            this.product_cate = product_cate;
        }

        public static class BannerDetailsBean {
            private String banner_img;
            private String banner_title;

            public String getBanner_img() {
                return banner_img;
            }

            public void setBanner_img(String banner_img) {
                this.banner_img = banner_img;
            }

            public String getBanner_title() {
                return banner_title;
            }

            public void setBanner_title(String banner_title) {
                this.banner_title = banner_title;
            }
        }

        public static class TodaySpecialBean {
            private String _id;
            /**
             * product_img : http://35.164.43.170:3000/api/uploads/1638874408182.jpg
             */

            private ProductImgBean product_img;
            private String product_title;
            private String thumbnail_image;
            private int product_price;
            private int product_discount;
            private int product_discount_price;
            private boolean product_fav;
            private int product_rating;
            private int product_review;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public ProductImgBean getProduct_img() {
                return product_img;
            }

            public void setProduct_img(ProductImgBean product_img) {
                this.product_img = product_img;
            }

            public String getProduct_title() {
                return product_title;
            }

            public void setProduct_title(String product_title) {
                this.product_title = product_title;
            }

            public String getThumbnail_image() {
                return thumbnail_image;
            }

            public void setThumbnail_image(String thumbnail_image) {
                this.thumbnail_image = thumbnail_image;
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

            public int getProduct_discount_price() {
                return product_discount_price;
            }

            public void setProduct_discount_price(int product_discount_price) {
                this.product_discount_price = product_discount_price;
            }

            public boolean isProduct_fav() {
                return product_fav;
            }

            public void setProduct_fav(boolean product_fav) {
                this.product_fav = product_fav;
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

        public static class ProductDetailsBean {
            private String cat_id;
            private String cat_name;
            /**
             * _id : 61af3d669e6f552291dbfa6e
             * product_img : {"product_img":"http://35.164.43.170:3000/api/uploads/1638874408182.jpg"}
             * product_title : Sample
             * thumbnail_image : http://35.164.43.170:3000/api/uploads/1638874408182.jpg
             * product_price : 192
             * product_discount : 1
             * product_discount_price : 194
             * product_fav : false
             * product_rating : 5
             * product_review : 0
             */

            private List<ProductListBean> product_list;

            public String getCat_id() {
                return cat_id;
            }

            public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
            }

            public String getCat_name() {
                return cat_name;
            }

            public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
            }

            public List<ProductListBean> getProduct_list() {
                return product_list;
            }

            public void setProduct_list(List<ProductListBean> product_list) {
                this.product_list = product_list;
            }

            public static class ProductListBean {
                private String _id;
                /**
                 * product_img : http://35.164.43.170:3000/api/uploads/1638874408182.jpg
                 */

                private ProductImgBean product_img;
                private String product_title;
                private String thumbnail_image;
                private int product_price;
                private int product_discount;
                private int product_discount_price;
                private boolean product_fav;
                private int product_rating;
                private int product_review;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public ProductImgBean getProduct_img() {
                    return product_img;
                }

                public void setProduct_img(ProductImgBean product_img) {
                    this.product_img = product_img;
                }

                public String getProduct_title() {
                    return product_title;
                }

                public void setProduct_title(String product_title) {
                    this.product_title = product_title;
                }

                public String getThumbnail_image() {
                    return thumbnail_image;
                }

                public void setThumbnail_image(String thumbnail_image) {
                    this.thumbnail_image = thumbnail_image;
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

                public int getProduct_discount_price() {
                    return product_discount_price;
                }

                public void setProduct_discount_price(int product_discount_price) {
                    this.product_discount_price = product_discount_price;
                }

                public boolean isProduct_fav() {
                    return product_fav;
                }

                public void setProduct_fav(boolean product_fav) {
                    this.product_fav = product_fav;
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
        }

        public static class ProductCateBean {
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
    }
}
