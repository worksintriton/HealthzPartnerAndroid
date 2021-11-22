package com.triton.healthZpartners.responsepojo;

import java.util.List;

public class CouponCodeTextResponse {

    /**
     * Status : Success
     * Message : Refund content
     * Data : [{"image":"http://54.212.108.156:3000/api/uploads/1629451447949.jpeg","title":"Bank Transtion","descri":"will be recive with in 4 - 5 working days","refund":"refund"},{"image":"http://54.212.108.156:3000/api/uploads/1629451505391.jpeg","title":"Refund Code","descri":"the amount will be refund has a coupon code","refund":""}]
     * Code : 200
     */

    private String Status;
    private String Message;
    private int Code;
    /**
     * image : http://54.212.108.156:3000/api/uploads/1629451447949.jpeg
     * title : Bank Transtion
     * descri : will be recive with in 4 - 5 working days
     * refund : refund
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
        private String image;
        private String title;
        private String descri;
        private String refund;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescri() {
            return descri;
        }

        public void setDescri(String descri) {
            this.descri = descri;
        }

        public String getRefund() {
            return refund;
        }

        public void setRefund(String refund) {
            this.refund = refund;
        }
    }
}
