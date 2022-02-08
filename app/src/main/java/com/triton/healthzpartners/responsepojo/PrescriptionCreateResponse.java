package com.triton.healthzpartners.responsepojo;

import java.util.List;

public class PrescriptionCreateResponse{


	/**
	 * Status : Success
	 * Message : Added successfully
	 * Data : {"Prescription_data":[{"Quantity":"2","Tablet_name":"dolo 650","consumption":{"evening":false,"morning":true,"night":false},"intakeBean":{"afterfood":true,"beforefood":false}}],"_id":"61bc476f6299c8306bf65cec","doctor_id":"61af0ec058c631738117ee0b","Appointment_ID":"61bc45fe6299c8306bf65c28","Treatment_Done_by":"","user_id":"","Prescription_type":"PDF","PDF_format":"","Prescription_img":"","Doctor_Comments":"take medicine regularly","diagnosis":"Urinary System.","sub_diagnosis":" Kidney stone ","Date":"17/12/2021 01:45 PM","delete_status":false,"Prescription_id":"PRE-1639729007416","updatedAt":"2021-12-17T08:16:47.417Z","createdAt":"2021-12-17T08:16:47.417Z","__v":0}
	 * Code : 200
	 */

	private String Status;
	private String Message;
	/**
	 * Prescription_data : [{"Quantity":"2","Tablet_name":"dolo 650","consumption":{"evening":false,"morning":true,"night":false},"intakeBean":{"afterfood":true,"beforefood":false}}]
	 * _id : 61bc476f6299c8306bf65cec
	 * doctor_id : 61af0ec058c631738117ee0b
	 * Appointment_ID : 61bc45fe6299c8306bf65c28
	 * Treatment_Done_by :
	 * user_id :
	 * Prescription_type : PDF
	 * PDF_format :
	 * Prescription_img :
	 * Doctor_Comments : take medicine regularly
	 * diagnosis : Urinary System.
	 * sub_diagnosis :  Kidney stone
	 * Date : 17/12/2021 01:45 PM
	 * delete_status : false
	 * Prescription_id : PRE-1639729007416
	 * updatedAt : 2021-12-17T08:16:47.417Z
	 * createdAt : 2021-12-17T08:16:47.417Z
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
		private String doctor_id;
		private String Appointment_ID;
		private String Treatment_Done_by;
		private String user_id;
		private String Prescription_type;
		private String PDF_format;
		private String Prescription_img;
		private String Doctor_Comments;
		private String diagnosis;
		private String sub_diagnosis;
		private String Date;
		private boolean delete_status;
		private String Prescription_id;
		private String updatedAt;
		private String createdAt;
		private int __v;
		/**
		 * Quantity : 2
		 * Tablet_name : dolo 650
		 * consumption : {"evening":false,"morning":true,"night":false}
		 * intakeBean : {"afterfood":true,"beforefood":false}
		 */

		private List<PrescriptionDataBean> Prescription_data;

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

		public String getDoctor_Comments() {
			return Doctor_Comments;
		}

		public void setDoctor_Comments(String Doctor_Comments) {
			this.Doctor_Comments = Doctor_Comments;
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

		public String getPrescription_id() {
			return Prescription_id;
		}

		public void setPrescription_id(String Prescription_id) {
			this.Prescription_id = Prescription_id;
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

		public List<PrescriptionDataBean> getPrescription_data() {
			return Prescription_data;
		}

		public void setPrescription_data(List<PrescriptionDataBean> Prescription_data) {
			this.Prescription_data = Prescription_data;
		}

		public static class PrescriptionDataBean {
			private String Quantity;
			private String Tablet_name;
			/**
			 * evening : false
			 * morning : true
			 * night : false
			 */

			private ConsumptionBean consumption;
			/**
			 * afterfood : true
			 * beforefood : false
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

			public ConsumptionBean getConsumption() {
				return consumption;
			}

			public void setConsumption(ConsumptionBean consumption) {
				this.consumption = consumption;
			}

			public IntakeBeanBean getIntakeBean() {
				return intakeBean;
			}

			public void setIntakeBean(IntakeBeanBean intakeBean) {
				this.intakeBean = intakeBean;
			}

			public static class ConsumptionBean {
				private boolean evening;
				private boolean morning;
				private boolean night;

				public boolean isEvening() {
					return evening;
				}

				public void setEvening(boolean evening) {
					this.evening = evening;
				}

				public boolean isMorning() {
					return morning;
				}

				public void setMorning(boolean morning) {
					this.morning = morning;
				}

				public boolean isNight() {
					return night;
				}

				public void setNight(boolean night) {
					this.night = night;
				}
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
