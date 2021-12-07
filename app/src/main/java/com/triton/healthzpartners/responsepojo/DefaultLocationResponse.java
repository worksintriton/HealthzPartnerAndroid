package com.triton.healthzpartners.responsepojo;

public class DefaultLocationResponse {

    /**
     * Status : Success
     * Message : Location List
     * Data : {"_id":"60acab2268492a4567b3f507","user_id":"60acaaa968492a4567b3f505","location_state":"Tamil Nadu","location_country":"India","location_city":"Iluppaiyur","location_pin":"621006","location_address":"Karatampatti Iluppaiyur road, Iluppaiyur, Tamil Nadu 621006, India","location_lat":11.064702232611461,"location_long":78.63427575677635,"location_title":"Home","location_nickname":"DKK APARTMENT","default_status":true,"date_and_time":"25-05-2021 01:15 PM","mobile_type":"Android","delete_status":false,"updatedAt":"2021-09-23T13:06:45.747Z","createdAt":"2021-05-25T07:45:38.365Z","__v":0}
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * _id : 60acab2268492a4567b3f507
     * user_id : 60acaaa968492a4567b3f505
     * location_state : Tamil Nadu
     * location_country : India
     * location_city : Iluppaiyur
     * location_pin : 621006
     * location_address : Karatampatti Iluppaiyur road, Iluppaiyur, Tamil Nadu 621006, India
     * location_lat : 11.064702232611461
     * location_long : 78.63427575677635
     * location_title : Home
     * location_nickname : DKK APARTMENT
     * default_status : true
     * date_and_time : 25-05-2021 01:15 PM
     * mobile_type : Android
     * delete_status : false
     * updatedAt : 2021-09-23T13:06:45.747Z
     * createdAt : 2021-05-25T07:45:38.365Z
     * __v : 0
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
        private String _id;
        private String user_id;
        private String location_state;
        private String location_country;
        private String location_city;
        private String location_pin;
        private String location_address;
        private double location_lat;
        private double location_long;
        private String location_title;
        private String location_nickname;
        private boolean default_status;
        private String date_and_time;
        private String mobile_type;
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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getLocation_state() {
            return location_state;
        }

        public void setLocation_state(String location_state) {
            this.location_state = location_state;
        }

        public String getLocation_country() {
            return location_country;
        }

        public void setLocation_country(String location_country) {
            this.location_country = location_country;
        }

        public String getLocation_city() {
            return location_city;
        }

        public void setLocation_city(String location_city) {
            this.location_city = location_city;
        }

        public String getLocation_pin() {
            return location_pin;
        }

        public void setLocation_pin(String location_pin) {
            this.location_pin = location_pin;
        }

        public String getLocation_address() {
            return location_address;
        }

        public void setLocation_address(String location_address) {
            this.location_address = location_address;
        }

        public double getLocation_lat() {
            return location_lat;
        }

        public void setLocation_lat(double location_lat) {
            this.location_lat = location_lat;
        }

        public double getLocation_long() {
            return location_long;
        }

        public void setLocation_long(double location_long) {
            this.location_long = location_long;
        }

        public String getLocation_title() {
            return location_title;
        }

        public void setLocation_title(String location_title) {
            this.location_title = location_title;
        }

        public String getLocation_nickname() {
            return location_nickname;
        }

        public void setLocation_nickname(String location_nickname) {
            this.location_nickname = location_nickname;
        }

        public boolean isDefault_status() {
            return default_status;
        }

        public void setDefault_status(boolean default_status) {
            this.default_status = default_status;
        }

        public String getDate_and_time() {
            return date_and_time;
        }

        public void setDate_and_time(String date_and_time) {
            this.date_and_time = date_and_time;
        }

        public String getMobile_type() {
            return mobile_type;
        }

        public void setMobile_type(String mobile_type) {
            this.mobile_type = mobile_type;
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
