package com.triton.healthzpartners.responsepojo;

import java.util.List;

public class FetctProductByCatDetailsResponse {

    /**
     * Status : Success
     * Message : product categories list Details
     * Data : [{"_id":"6198b507518ad4520ab14790","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Skin & Body Care","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:11:32 PM","delete_status":false,"updatedAt":"2021-11-20T08:42:47.494Z","createdAt":"2021-11-20T08:42:47.494Z","__v":0},{"_id":"6198b572518ad4520ab14791","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Women Care","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:13:20 PM","delete_status":false,"updatedAt":"2021-11-20T08:44:34.670Z","createdAt":"2021-11-20T08:44:34.670Z","__v":0},{"_id":"6198b594518ad4520ab14792","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Organic Products","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:13:53 PM","delete_status":false,"updatedAt":"2021-11-20T08:45:08.260Z","createdAt":"2021-11-20T08:45:08.260Z","__v":0},{"_id":"6198b5a2518ad4520ab14793","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Men Care","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:14:08 PM","delete_status":false,"updatedAt":"2021-11-20T08:45:22.893Z","createdAt":"2021-11-20T08:45:22.893Z","__v":0},{"_id":"6198b5cc518ad4520ab14795","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Baby Products","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:14:49 PM","delete_status":false,"updatedAt":"2021-11-20T08:46:04.099Z","createdAt":"2021-11-20T08:46:04.099Z","__v":0},{"_id":"6198b5e1518ad4520ab14796","img_path":"http://13.57.9.246:3000/api/uploads/1625751478722.png","product_cate":"Health Care","img_index":0,"show_status":true,"date_and_time":"11/20/2021, 2:15:11 PM","delete_status":false,"updatedAt":"2021-11-20T08:46:25.853Z","createdAt":"2021-11-20T08:46:25.853Z","__v":0},{"_id":"5fec14a5ea832e2e73c1fc79","img_path":"http://52.25.163.13:3000/api/uploads/template%20(3).jpg","product_cate":"Pet Foods - Wet","img_index":0,"show_status":true,"date_and_time":"Thu Jul 08 2021 01:55:56 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-07-08T09:39:49.690Z","createdAt":"2020-12-30T05:48:21.363Z","__v":0},{"_id":"5fec1573ea832e2e73c1fc7a","img_path":"http://52.25.163.13:3000/api/uploads/template%20(4).jpg","product_cate":"Bedding","img_index":0,"show_status":true,"date_and_time":"Thu Jul 08 2021 01:57:45 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-07-08T09:39:55.461Z","createdAt":"2020-12-30T05:51:47.787Z","__v":0},{"_id":"5fec22eeea832e2e73c1fc7b","img_path":"http://52.25.163.13:3000/api/uploads/template%20(5).jpg","product_cate":"Pet Grooming","img_index":0,"show_status":true,"date_and_time":"Thu Jul 08 2021 18:13:44 GMT+0530 (India Standard Time)","delete_status":false,"updatedAt":"2021-07-08T14:06:47.786Z","createdAt":"2020-12-30T06:49:18.019Z","__v":0},{"_id":"60e6ffbafe7500511a7b419d","img_path":"http://54.212.108.156:3000/api/uploads/1625751478722.png","product_cate":"Pet Accessories","img_index":0,"show_status":true,"date_and_time":"7/8/2021, 7:08:01 PM","delete_status":false,"updatedAt":"2021-07-08T13:38:02.721Z","createdAt":"2021-07-08T13:38:02.721Z","__v":0}]
     * Code : 200
     */

    private String Status;
    private String Message;
    private int Code;
    /**
     * _id : 6198b507518ad4520ab14790
     * img_path : http://13.57.9.246:3000/api/uploads/1625751478722.png
     * product_cate : Skin & Body Care
     * img_index : 0
     * show_status : true
     * date_and_time : 11/20/2021, 2:11:32 PM
     * delete_status : false
     * updatedAt : 2021-11-20T08:42:47.494Z
     * createdAt : 2021-11-20T08:42:47.494Z
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

    public static class DataBean {
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
