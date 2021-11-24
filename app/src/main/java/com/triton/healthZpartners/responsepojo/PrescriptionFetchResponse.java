package com.triton.healthZpartners.responsepojo;

import java.util.List;

public class PrescriptionFetchResponse {

    /**
     * Status : Success
     * Message : Prescription detail
     * Data : {"doctorname":"Bille ","doctor_speci":[{"specialization":"Surgery"},{"specialization":"Nutrition"},{"specialization":"Laboratory Animal Medicine"},{"specialization":"Emergency and Critical Care"},{"specialization":"Equine Medicine"},{"specialization":"Behavior"},{"specialization":"Dentistry"}],"web_name":"www.petfolio.in","phone_number":"+91-9988776655","app_logo":"http://54.212.108.156:3000/api/uploads/logo.png","owner_name":"Sandy","user_id":"","name":"Dinesh","gender":"Male","relation_type":"Self","health_issue":"OCD","dateofbirth":"16-05-1992","anymedicalinfo":"I'm 29 years old boy","covide_vac":"Yes","weight":"75","diagnosis":"Cancer and Tumour","sub_diagnosis":"All Cancers / Tumours","allergies":"","Doctor_Comments":"High fever","digital_sign":"http://54.212.108.156:3000/api/uploads/60b4e899541a437c7b9c60fd3105191744","Prescription_data":[{"Quantity":"5","Tablet_name":"Dolo","consumption":"Morning - 1, Afternoon - 0, Night - 1","intakeBean":{"afterfood":false,"beforefood":true}}],"_id":"619e0adb2a9dd179ff1c1fb1","doctor_id":"dr123456789","Appointment_ID":"619e0aa52a9dd179ff1c1fb0","Treatment_Done_by":"","Prescription_type":"PDF","PDF_format":"https://petfolio.app/api/public/ic/1637747418697.pdf","Prescription_img":"","Date":"24/11/2021 03:20 PM","delete_status":false,"updatedAt":"2021-11-24T09:50:19.340Z","createdAt":"2021-11-24T09:50:19.340Z","health_issue_title":"General Checkup","__v":0,"clinic_no":"123456789","clinic_loc":"4/270, Azhagappa Nagar, Bharathiyar Nagar, Raj Nagar, Thoraipakkam, Tamil Nadu 600097, India","clinic_name":"Barking Fine Pet Speciality Hospital","Prescription_id":"PRE-1637747419338","share_msg":"Please find the Prescription for the appointment: https://petfolio.app/api/public/ic/1637747418697.pdf. You can download Petfolio Mobile App using this link below. http://petfolio.in"}
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * doctorname : Bille
     * doctor_speci : [{"specialization":"Surgery"},{"specialization":"Nutrition"},{"specialization":"Laboratory Animal Medicine"},{"specialization":"Emergency and Critical Care"},{"specialization":"Equine Medicine"},{"specialization":"Behavior"},{"specialization":"Dentistry"}]
     * web_name : www.petfolio.in
     * phone_number : +91-9988776655
     * app_logo : http://54.212.108.156:3000/api/uploads/logo.png
     * owner_name : Sandy
     * user_id :
     * name : Dinesh
     * gender : Male
     * relation_type : Self
     * health_issue : OCD
     * dateofbirth : 16-05-1992
     * anymedicalinfo : I'm 29 years old boy
     * covide_vac : Yes
     * weight : 75
     * diagnosis : Cancer and Tumour
     * sub_diagnosis : All Cancers / Tumours
     * allergies :
     * Doctor_Comments : High fever
     * digital_sign : http://54.212.108.156:3000/api/uploads/60b4e899541a437c7b9c60fd3105191744
     * Prescription_data : [{"Quantity":"5","Tablet_name":"Dolo","consumption":"Morning - 1, Afternoon - 0, Night - 1","intakeBean":{"afterfood":false,"beforefood":true}}]
     * _id : 619e0adb2a9dd179ff1c1fb1
     * doctor_id : dr123456789
     * Appointment_ID : 619e0aa52a9dd179ff1c1fb0
     * Treatment_Done_by :
     * Prescription_type : PDF
     * PDF_format : https://petfolio.app/api/public/ic/1637747418697.pdf
     * Prescription_img :
     * Date : 24/11/2021 03:20 PM
     * delete_status : false
     * updatedAt : 2021-11-24T09:50:19.340Z
     * createdAt : 2021-11-24T09:50:19.340Z
     * health_issue_title : General Checkup
     * __v : 0
     * clinic_no : 123456789
     * clinic_loc : 4/270, Azhagappa Nagar, Bharathiyar Nagar, Raj Nagar, Thoraipakkam, Tamil Nadu 600097, India
     * clinic_name : Barking Fine Pet Speciality Hospital
     * Prescription_id : PRE-1637747419338
     * share_msg : Please find the Prescription for the appointment: https://petfolio.app/api/public/ic/1637747418697.pdf. You can download Petfolio Mobile App using this link below. http://petfolio.in
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
        private String doctorname;
        private String web_name;
        private String phone_number;
        private String app_logo;
        private String owner_name;
        private String user_id;
        private String name;
        private String gender;
        private String relation_type;
        private String health_issue;
        private String dateofbirth;
        private String anymedicalinfo;
        private String covide_vac;
        private String weight;
        private String diagnosis;
        private String sub_diagnosis;
        private String allergies;
        private String Doctor_Comments;
        private String digital_sign;
        private String _id;
        private String doctor_id;
        private String Appointment_ID;
        private String Treatment_Done_by;
        private String Prescription_type;
        private String PDF_format;
        private String Prescription_img;
        private String Date;
        private boolean delete_status;
        private String updatedAt;
        private String createdAt;
        private String health_issue_title;
        private int __v;
        private String clinic_no;
        private String clinic_loc;
        private String clinic_name;
        private String Prescription_id;
        private String share_msg;
        /**
         * specialization : Surgery
         */

        private List<DoctorSpeciBean> doctor_speci;
        /**
         * Quantity : 5
         * Tablet_name : Dolo
         * consumption : Morning - 1, Afternoon - 0, Night - 1
         * intakeBean : {"afterfood":false,"beforefood":true}
         */

        private List<PrescriptionDataBean> Prescription_data;

        public String getDoctorname() {
            return doctorname;
        }

        public void setDoctorname(String doctorname) {
            this.doctorname = doctorname;
        }

        public String getWeb_name() {
            return web_name;
        }

        public void setWeb_name(String web_name) {
            this.web_name = web_name;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getApp_logo() {
            return app_logo;
        }

        public void setApp_logo(String app_logo) {
            this.app_logo = app_logo;
        }

        public String getOwner_name() {
            return owner_name;
        }

        public void setOwner_name(String owner_name) {
            this.owner_name = owner_name;
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

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public String getSub_diagnosis() {
            return sub_diagnosis;
        }

        public void setSub_diagnosis(String sub_diagnosis) {
            this.sub_diagnosis = sub_diagnosis;
        }

        public String getAllergies() {
            return allergies;
        }

        public void setAllergies(String allergies) {
            this.allergies = allergies;
        }

        public String getDoctor_Comments() {
            return Doctor_Comments;
        }

        public void setDoctor_Comments(String Doctor_Comments) {
            this.Doctor_Comments = Doctor_Comments;
        }

        public String getDigital_sign() {
            return digital_sign;
        }

        public void setDigital_sign(String digital_sign) {
            this.digital_sign = digital_sign;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getDoctor_id() {
            return doctor_id;
        }

        public void setDoctor_id(String doctor_id) {
            this.doctor_id = doctor_id;
        }

        public String getAppointment_ID() {
            return Appointment_ID;
        }

        public void setAppointment_ID(String Appointment_ID) {
            this.Appointment_ID = Appointment_ID;
        }

        public String getTreatment_Done_by() {
            return Treatment_Done_by;
        }

        public void setTreatment_Done_by(String Treatment_Done_by) {
            this.Treatment_Done_by = Treatment_Done_by;
        }

        public String getPrescription_type() {
            return Prescription_type;
        }

        public void setPrescription_type(String Prescription_type) {
            this.Prescription_type = Prescription_type;
        }

        public String getPDF_format() {
            return PDF_format;
        }

        public void setPDF_format(String PDF_format) {
            this.PDF_format = PDF_format;
        }

        public String getPrescription_img() {
            return Prescription_img;
        }

        public void setPrescription_img(String Prescription_img) {
            this.Prescription_img = Prescription_img;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String Date) {
            this.Date = Date;
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

        public String getHealth_issue_title() {
            return health_issue_title;
        }

        public void setHealth_issue_title(String health_issue_title) {
            this.health_issue_title = health_issue_title;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getClinic_no() {
            return clinic_no;
        }

        public void setClinic_no(String clinic_no) {
            this.clinic_no = clinic_no;
        }

        public String getClinic_loc() {
            return clinic_loc;
        }

        public void setClinic_loc(String clinic_loc) {
            this.clinic_loc = clinic_loc;
        }

        public String getClinic_name() {
            return clinic_name;
        }

        public void setClinic_name(String clinic_name) {
            this.clinic_name = clinic_name;
        }

        public String getPrescription_id() {
            return Prescription_id;
        }

        public void setPrescription_id(String Prescription_id) {
            this.Prescription_id = Prescription_id;
        }

        public String getShare_msg() {
            return share_msg;
        }

        public void setShare_msg(String share_msg) {
            this.share_msg = share_msg;
        }

        public List<DoctorSpeciBean> getDoctor_speci() {
            return doctor_speci;
        }

        public void setDoctor_speci(List<DoctorSpeciBean> doctor_speci) {
            this.doctor_speci = doctor_speci;
        }

        public List<PrescriptionDataBean> getPrescription_data() {
            return Prescription_data;
        }

        public void setPrescription_data(List<PrescriptionDataBean> Prescription_data) {
            this.Prescription_data = Prescription_data;
        }

        public static class DoctorSpeciBean {
            private String specialization;

            public String getSpecialization() {
                return specialization;
            }

            public void setSpecialization(String specialization) {
                this.specialization = specialization;
            }
        }

        public static class PrescriptionDataBean {
            private String Quantity;
            private String Tablet_name;
            private String consumption;
            /**
             * afterfood : false
             * beforefood : true
             */

            private IntakeBeanBean intakeBean;

            public String getQuantity() {
                return Quantity;
            }

            public void setQuantity(String Quantity) {
                this.Quantity = Quantity;
            }

            public String getTablet_name() {
                return Tablet_name;
            }

            public void setTablet_name(String Tablet_name) {
                this.Tablet_name = Tablet_name;
            }

            public String getConsumption() {
                return consumption;
            }

            public void setConsumption(String consumption) {
                this.consumption = consumption;
            }

            public IntakeBeanBean getIntakeBean() {
                return intakeBean;
            }

            public void setIntakeBean(IntakeBeanBean intakeBean) {
                this.intakeBean = intakeBean;
            }

            public static class IntakeBeanBean {
                private boolean afterfood;
                private boolean beforefood;

                public boolean isAfterfood() {
                    return afterfood;
                }

                public void setAfterfood(boolean afterfood) {
                    this.afterfood = afterfood;
                }

                public boolean isBeforefood() {
                    return beforefood;
                }

                public void setBeforefood(boolean beforefood) {
                    this.beforefood = beforefood;
                }
            }
        }
    }
}
