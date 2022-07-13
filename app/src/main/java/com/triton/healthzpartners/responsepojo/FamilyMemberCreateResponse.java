package com.triton.healthzpartners.responsepojo;

import java.util.List;

public class FamilyMemberCreateResponse {


    /**
     * Status : Success
     * Message : Added successfully
     * Data : {"pic":[{"image":"http://Google.com"}],"_id":"618b7d9a99566a5096d9e2d4","user_id":"618230269dcc2a290e5bae9a","name":"Mohammed","gender":"Male","relation_type":"Son","health_issue":"No issue","dateofbirth":"23-10-2021","anymedicalinfo":"No Issue","covide_vac":"Yes","weight":"70","delete_status":false,"createdAt":"2021-11-10T08:06:50.070Z","updatedAt":"2021-11-10T08:06:50.070Z","__v":0}
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * pic : [{"image":"http://Google.com"}]
     * _id : 618b7d9a99566a5096d9e2d4
     * user_id : 618230269dcc2a290e5bae9a
     * name : Mohammed
     * gender : Male
     * relation_type : Son
     * health_issue : No issue
     * dateofbirth : 23-10-2021
     * anymedicalinfo : No Issue
     * covide_vac : Yes
     * weight : 70
     * delete_status : false
     * createdAt : 2021-11-10T08:06:50.070Z
     * updatedAt : 2021-11-10T08:06:50.070Z
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
        private String name;
        private String gender;
        private String relation_type;
        private String health_issue;
        private String dateofbirth;
        private String anymedicalinfo;
        private String covide_vac;
        private String weight;
        private boolean delete_status;
        private String createdAt;
        private String updatedAt;
        private int __v;
        /**
         * image : http://Google.com
         */

        private List<PicBean> pic;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRelation_type() {
            return relation_type;
        }

        public void setRelation_type(String relation_type) {
            this.relation_type = relation_type;
        }

        public String getHealth_issue() {
            return health_issue;
        }

        public void setHealth_issue(String health_issue) {
            this.health_issue = health_issue;
        }

        public String getDateofbirth() {
            return dateofbirth;
        }

        public void setDateofbirth(String dateofbirth) {
            this.dateofbirth = dateofbirth;
        }

        public String getAnymedicalinfo() {
            return anymedicalinfo;
        }

        public void setAnymedicalinfo(String anymedicalinfo) {
            this.anymedicalinfo = anymedicalinfo;
        }

        public String getCovide_vac() {
            return covide_vac;
        }

        public void setCovide_vac(String covide_vac) {
            this.covide_vac = covide_vac;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public boolean isDelete_status() {
            return delete_status;
        }

        public void setDelete_status(boolean delete_status) {
            this.delete_status = delete_status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public List<PicBean> getPic() {
            return pic;
        }

        public void setPic(List<PicBean> pic) {
            this.pic = pic;
        }

        public static class PicBean {
            private String image;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }
}
