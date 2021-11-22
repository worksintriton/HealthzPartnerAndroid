package com.triton.healthZpartners.responsepojo;

import java.util.List;

public class PrescriptionFetchResponse {

    /**
     * Status : Success
     * Message : Prescription detail
     * Data : {"doctorname":"Maddy San","doctor_speci":[{"specialization":"General Consultant"},{"specialization":"Virology"},{"specialization":"Microbiology"},{"specialization":"Pathology"},{"specialization":"Theriogenology"},{"specialization":"Equine Medicine"},{"specialization":"Radiation Oncology"},{"specialization":"Behavior"},{"specialization":"Surgery"},{"specialization":"Nutrition"},{"specialization":"Dentistry"},{"specialization":"Laboratory Animal Medicine"},{"specialization":"Radiology"},{"specialization":"Emergency and Critical Care"},{"specialization":"Orthopaedics"}],"web_name":"www.petfolio.com","phone_number":"+91-9988776655","app_logo":"http://54.212.108.156:3000/api/uploads/logo.png","owner_name":"Dinesh","pet_type":"Teenu","pet_breed":"Dogs - (Indian & Foreign) ","pet_name":"Teenu","gender":"male","weight":0,"age":"5 years 1 months","diagnosis":"Urinary System","sub_diagnosis":" Kidney stone ","allergies":"no","Doctor_Comments":"no comments","digital_sign":"http://54.212.108.156:3000/api/uploads/1626176527437.jpg","Prescription_data":[{"Quantity":"2","Tablet_name":"dolo 650","consumption":"Morning - 1, Afternoon - 0, Night - 1"}],"_id":"60eea2de64de2123d1c15a49","doctor_id":"DR12345676","Appointment_ID":"60eea27c64de2123d1c15a46","Treatment_Done_by":"","user_id":"","Prescription_type":"PDF","PDF_format":"http://54.212.108.156:3000/api/public/4fdc3ee0-a9fc-4f7e-8854-bb5a8f38be02.pdf","Prescription_img":"","Date":"14/07/2021 02:09 PM","delete_status":false,"updatedAt":"2021-07-14T08:39:58.823Z","createdAt":"2021-07-14T08:39:58.823Z","health_issue_title":"General Checkup","__v":0,"clinic_no":"987987989902","clinic_loc":"Salem 133, Vmr Theatre Back Side Rd, Shevapet, Salem, Tamil Nadu 636002, India","clinic_name":"Maddy San Pet Clinic","Prescription_id":"PRE-1626251998821"}
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * doctorname : Maddy San
     * doctor_speci : [{"specialization":"General Consultant"},{"specialization":"Virology"},{"specialization":"Microbiology"},{"specialization":"Pathology"},{"specialization":"Theriogenology"},{"specialization":"Equine Medicine"},{"specialization":"Radiation Oncology"},{"specialization":"Behavior"},{"specialization":"Surgery"},{"specialization":"Nutrition"},{"specialization":"Dentistry"},{"specialization":"Laboratory Animal Medicine"},{"specialization":"Radiology"},{"specialization":"Emergency and Critical Care"},{"specialization":"Orthopaedics"}]
     * web_name : www.petfolio.com
     * phone_number : +91-9988776655
     * app_logo : http://54.212.108.156:3000/api/uploads/logo.png
     * owner_name : Dinesh
     * pet_type : Teenu
     * pet_breed : Dogs - (Indian & Foreign)
     * pet_name : Teenu
     * gender : male
     * weight : 0
     * age : 5 years 1 months
     * diagnosis : Urinary System
     * sub_diagnosis :  Kidney stone
     * allergies : no
     * Doctor_Comments : no comments
     * digital_sign : http://54.212.108.156:3000/api/uploads/1626176527437.jpg
     * Prescription_data : [{"Quantity":"2","Tablet_name":"dolo 650","consumption":"Morning - 1, Afternoon - 0, Night - 1"}]
     * _id : 60eea2de64de2123d1c15a49
     * doctor_id : DR12345676
     * Appointment_ID : 60eea27c64de2123d1c15a46
     * Treatment_Done_by :
     * user_id :
     * Prescription_type : PDF
     * PDF_format : http://54.212.108.156:3000/api/public/4fdc3ee0-a9fc-4f7e-8854-bb5a8f38be02.pdf
     * Prescription_img :
     * Date : 14/07/2021 02:09 PM
     * delete_status : false
     * updatedAt : 2021-07-14T08:39:58.823Z
     * createdAt : 2021-07-14T08:39:58.823Z
     * health_issue_title : General Checkup
     * __v : 0
     * clinic_no : 987987989902
     * clinic_loc : Salem 133, Vmr Theatre Back Side Rd, Shevapet, Salem, Tamil Nadu 636002, India
     * clinic_name : Maddy San Pet Clinic
     * Prescription_id : PRE-1626251998821
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
        private String pet_type;
        private String pet_breed;
        private String pet_name;
        private String gender;
        private int weight;
        private String age;
        private String diagnosis;
        private String sub_diagnosis;
        private String allergies;
        private String Doctor_Comments;
        private String digital_sign;
        private String _id;
        private String doctor_id;
        private String Appointment_ID;
        private String Treatment_Done_by;
        private String user_id;
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

        public String getShare_msg() {
            return share_msg;
        }

        public void setShare_msg(String share_msg) {
            this.share_msg = share_msg;
        }

        /**
         * specialization : General Consultant
         */

        private List<DoctorSpeciBean> doctor_speci;
        /**
         * Quantity : 2
         * Tablet_name : dolo 650
         * consumption : Morning - 1, Afternoon - 0, Night - 1
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

        public String getPet_type() {
            return pet_type;
        }

        public void setPet_type(String pet_type) {
            this.pet_type = pet_type;
        }

        public String getPet_breed() {
            return pet_breed;
        }

        public void setPet_breed(String pet_breed) {
            this.pet_breed = pet_breed;
        }

        public String getPet_name() {
            return pet_name;
        }

        public void setPet_name(String pet_name) {
            this.pet_name = pet_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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
        }
    }
}
