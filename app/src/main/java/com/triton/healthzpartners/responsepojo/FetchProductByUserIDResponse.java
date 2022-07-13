package com.triton.healthzpartners.responsepojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FetchProductByUserIDResponse {


    /**
     * Status : Success
     * Message : Product details list
     * Data : [{"cat_id":{"_id":"5fec1424ea832e2e73c1fc78","img_path":"http://52.25.163.13:3000/api/uploads/template%20(2).jpg","product_cate":"Hair","img_index":0,"show_status":true,"date_and_time":"Sat Nov 20 2021 14:09:41 GMT+0530 (India Standard Time)","delete_status":true,"updatedAt":"2021-11-20T08:40:56.849Z","createdAt":"2020-12-30T05:46:12.099Z","__v":0},"product_id":"60ae2c0c48ffef65a41bc546","products_image":["http://54.212.108.156:3000/api/uploads/1625750185578.png"],"thumbnail_image":"http://54.212.108.156:3000/api/uploads/1625752843017.png","product_name":"Pedigree Vegetarian Adult","product_desc":"Your pet gets protein from the soybean and vegetable oils while the vitamins from vegetables (including Vitamin E) boosts your pooch's immune system. ","product_price":109,"pet_threshold":"1999","today_deal":true},{"cat_id":{"_id":"5fec1424ea832e2e73c1fc78","img_path":"http://52.25.163.13:3000/api/uploads/template%20(2).jpg","product_cate":"Hair","img_index":0,"show_status":true,"date_and_time":"Sat Nov 20 2021 14:09:41 GMT+0530 (India Standard Time)","delete_status":true,"updatedAt":"2021-11-20T08:40:56.849Z","createdAt":"2020-12-30T05:46:12.099Z","__v":0},"product_id":"60ae2d1f48ffef65a41bc547","products_image":["http://54.212.108.156:3000/api/uploads/1625748449964.png"],"thumbnail_image":"http://54.212.108.156:3000/api/uploads/1625752792943.png","product_name":"Orijen","product_desc":"Feed your dog the wholesome goodness of Orijen\u2019s Clean Protein Formula Plant-Based Dry Dog Food! This recipe is nutritionally complete and formulated ","product_price":99,"pet_threshold":"2000","today_deal":true},{"cat_id":{"_id":"5fec14a5ea832e2e73c1fc79","img_path":"http://52.25.163.13:3000/api/uploads/template%20(3).jpg","product_cate":"Pet Foods - Wet","img_index":0,"show_status":true,"date_and_time":"Thu Jul 08 2021 01:55:56 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-07-08T09:39:49.690Z","createdAt":"2020-12-30T05:48:21.363Z","__v":0},"product_id":"60b0c4ae67f25056fe286ca2","products_image":["http://54.212.108.156:3000/api/uploads/1625748318970.png"],"thumbnail_image":"http://54.212.108.156:3000/api/uploads/1625752592166.png","product_name":"Royal Canin Maxi Adult Dog Wet Food","product_desc":"Royal Canin Maxi Adult Dog Gravy Food is formulated to meet the unique nutritional needs of adult dogs of maxi breed aged between 15 months to 8 years like Labrador, Golden Retriever, German Shepherd, Great Dane, English Mastiff, Saint Bernard, etc. Royal Canin Maxi Adult Dog Gravy Food is complete and balanced nutrition for maxi breed adult dogs, who have a long growth period. This gravy food comes with small chunks that are made with human grade chicken and its by-products that make it a diet rich in protein. Royal Canin put the animal first in everything they do. Every decision they make is based on years of study done with veterinary schools, universities, and breeders. The aim is always the same, to provide precise nutritional solutions to meet the need of your pet.","product_price":200,"pet_threshold":"1999","today_deal":false},{"cat_id":{"_id":"5fec1424ea832e2e73c1fc78","img_path":"http://52.25.163.13:3000/api/uploads/template%20(2).jpg","product_cate":"Hair","img_index":0,"show_status":true,"date_and_time":"Sat Nov 20 2021 14:09:41 GMT+0530 (India Standard Time)","delete_status":true,"updatedAt":"2021-11-20T08:40:56.849Z","createdAt":"2020-12-30T05:46:12.099Z","__v":0},"product_id":"61307bbc5896366c8fcc68de","products_image":["http://54.212.108.156:3000/api/uploads/1622643072891.jpg"],"product_name":"Shopiee Food","product_desc":"Sample Food","product_price":450,"pet_threshold":"1998","today_deal":true},{"cat_id":{"_id":"5fec1424ea832e2e73c1fc78","img_path":"http://52.25.163.13:3000/api/uploads/template%20(2).jpg","product_cate":"Hair","img_index":0,"show_status":true,"date_and_time":"Sat Nov 20 2021 14:09:41 GMT+0530 (India Standard Time)","delete_status":true,"updatedAt":"2021-11-20T08:40:56.849Z","createdAt":"2020-12-30T05:46:12.099Z","__v":0},"product_id":"6130cee15896366c8fcc695d","products_image":["http://54.212.108.156:3000/api/uploads/1622530397531.jpeg"],"product_name":"Orijen Original Dry Dog Food","product_desc":"Give your dog the goodness of quality ingredients and food that is nutritionally complete. Unlock the secret to healthy and happy dog with a range of ","product_price":100,"pet_threshold":"2000","today_deal":true}]
     * Code : 200
     */

    private String Status;
    private String Message;
    private int Code;
    /**
     * cat_id : {"_id":"5fec1424ea832e2e73c1fc78","img_path":"http://52.25.163.13:3000/api/uploads/template%20(2).jpg","product_cate":"Hair","img_index":0,"show_status":true,"date_and_time":"Sat Nov 20 2021 14:09:41 GMT+0530 (India Standard Time)","delete_status":true,"updatedAt":"2021-11-20T08:40:56.849Z","createdAt":"2020-12-30T05:46:12.099Z","__v":0}
     * product_id : 60ae2c0c48ffef65a41bc546
     * products_image : ["http://54.212.108.156:3000/api/uploads/1625750185578.png"]
     * thumbnail_image : http://54.212.108.156:3000/api/uploads/1625752843017.png
     * product_name : Pedigree Vegetarian Adult
     * product_desc : Your pet gets protein from the soybean and vegetable oils while the vitamins from vegetables (including Vitamin E) boosts your pooch's immune system.
     * product_price : 109
     * pet_threshold : 1999
     * today_deal : true
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
        /**
         * _id : 5fec1424ea832e2e73c1fc78
         * img_path : http://52.25.163.13:3000/api/uploads/template%20(2).jpg
         * product_cate : Hair
         * img_index : 0
         * show_status : true
         * date_and_time : Sat Nov 20 2021 14:09:41 GMT+0530 (India Standard Time)
         * delete_status : true
         * updatedAt : 2021-11-20T08:40:56.849Z
         * createdAt : 2020-12-30T05:46:12.099Z
         * __v : 0
         */

        private CatIdBean cat_id;
        private String product_id;
        private String thumbnail_image;
        private String product_name;
        private String product_desc;
        private int product_price;
        private String pet_threshold;
        private boolean today_deal;
        private ArrayList<String> products_image;

        public CatIdBean getCat_id() {
            return cat_id;
        }

        public void setCat_id(CatIdBean cat_id) {
            this.cat_id = cat_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
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

        public String getProduct_desc() {
            return product_desc;
        }

        public void setProduct_desc(String product_desc) {
            this.product_desc = product_desc;
        }

        public int getProduct_price() {
            return product_price;
        }

        public void setProduct_price(int product_price) {
            this.product_price = product_price;
        }

        public String getPet_threshold() {
            return pet_threshold;
        }

        public void setPet_threshold(String pet_threshold) {
            this.pet_threshold = pet_threshold;
        }

        public boolean isToday_deal() {
            return today_deal;
        }

        public void setToday_deal(boolean today_deal) {
            this.today_deal = today_deal;
        }

        public ArrayList<String> getProducts_image() {
            return products_image;
        }

        public void setProducts_image(ArrayList<String> products_image) {
            this.products_image = products_image;
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
    }
}