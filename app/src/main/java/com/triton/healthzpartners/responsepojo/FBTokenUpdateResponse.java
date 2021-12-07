package com.triton.healthzpartners.responsepojo;

public class FBTokenUpdateResponse {

    /**
     * Status : Success
     * Message : FB Updated
     * Data : {"_id":"60acaaa968492a4567b3f505","first_name":"Priyanka","last_name":"Nandikolmat","user_email":"rpriya9394@gmail.com","user_phone":"9879879877","date_of_reg":"2021-09-07T12:34:58.499Z","user_type":1,"ref_code":"","my_ref_code":"1VIQDLW","user_status":"complete","otp":123456,"profile_img":"https://petfolio.app/api/uploads/60acaaa968492a4567b3f5050810131600","user_email_verification":false,"fb_token":"dl4-W7SQR4iGb1GQ7Nqui8:APA91bEJTUlcd0IRbBpPRrqqYIeI7Q3tUC6V3Li1Xdrro_cl4xoT3gcy0MexzaEgPmVaC8JQB1Vve7FVXziiAik4eG_aMVd07OFbyZ0LKp1mna3NMPyznXUpNsrRNZGaUoZiUcQVHN0O","device_id":"","device_type":"","mobile_type":"Admin","delete_status":false,"updatedAt":"2021-10-27T10:00:21.551Z","createdAt":"2021-05-25T07:43:37.986Z","__v":0}
     * payment_gateway_detail : {"rzpkey":"rzp_test_zioohqmxDjJJtd","isproduction":false}
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * _id : 60acaaa968492a4567b3f505
     * first_name : Priyanka
     * last_name : Nandikolmat
     * user_email : rpriya9394@gmail.com
     * user_phone : 9879879877
     * date_of_reg : 2021-09-07T12:34:58.499Z
     * user_type : 1
     * ref_code :
     * my_ref_code : 1VIQDLW
     * user_status : complete
     * otp : 123456
     * profile_img : https://petfolio.app/api/uploads/60acaaa968492a4567b3f5050810131600
     * user_email_verification : false
     * fb_token : dl4-W7SQR4iGb1GQ7Nqui8:APA91bEJTUlcd0IRbBpPRrqqYIeI7Q3tUC6V3Li1Xdrro_cl4xoT3gcy0MexzaEgPmVaC8JQB1Vve7FVXziiAik4eG_aMVd07OFbyZ0LKp1mna3NMPyznXUpNsrRNZGaUoZiUcQVHN0O
     * device_id :
     * device_type :
     * mobile_type : Admin
     * delete_status : false
     * updatedAt : 2021-10-27T10:00:21.551Z
     * createdAt : 2021-05-25T07:43:37.986Z
     * __v : 0
     */

    private DataBean Data;
    /**
     * rzpkey : rzp_test_zioohqmxDjJJtd
     * isproduction : false
     */

    private PaymentGatewayDetailBean payment_gateway_detail;
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

    public PaymentGatewayDetailBean getPayment_gateway_detail() {
        return payment_gateway_detail;
    }

    public void setPayment_gateway_detail(PaymentGatewayDetailBean payment_gateway_detail) {
        this.payment_gateway_detail = payment_gateway_detail;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public static class DataBean {
        private String _id;
        private String first_name;
        private String last_name;
        private String user_email;
        private String user_phone;
        private String date_of_reg;
        private int user_type;
        private String ref_code;
        private String my_ref_code;
        private String user_status;
        private int otp;
        private String profile_img;
        private boolean user_email_verification;
        private String fb_token;
        private String device_id;
        private String device_type;
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

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getDate_of_reg() {
            return date_of_reg;
        }

        public void setDate_of_reg(String date_of_reg) {
            this.date_of_reg = date_of_reg;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public String getRef_code() {
            return ref_code;
        }

        public void setRef_code(String ref_code) {
            this.ref_code = ref_code;
        }

        public String getMy_ref_code() {
            return my_ref_code;
        }

        public void setMy_ref_code(String my_ref_code) {
            this.my_ref_code = my_ref_code;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public int getOtp() {
            return otp;
        }

        public void setOtp(int otp) {
            this.otp = otp;
        }

        public String getProfile_img() {
            return profile_img;
        }

        public void setProfile_img(String profile_img) {
            this.profile_img = profile_img;
        }

        public boolean isUser_email_verification() {
            return user_email_verification;
        }

        public void setUser_email_verification(boolean user_email_verification) {
            this.user_email_verification = user_email_verification;
        }

        public String getFb_token() {
            return fb_token;
        }

        public void setFb_token(String fb_token) {
            this.fb_token = fb_token;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
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

    public static class PaymentGatewayDetailBean {
        private String rzpkey;
        private boolean isproduction;

        public String getRzpkey() {
            return rzpkey;
        }

        public void setRzpkey(String rzpkey) {
            this.rzpkey = rzpkey;
        }

        public boolean isIsproduction() {
            return isproduction;
        }

        public void setIsproduction(boolean isproduction) {
            this.isproduction = isproduction;
        }
    }
}
