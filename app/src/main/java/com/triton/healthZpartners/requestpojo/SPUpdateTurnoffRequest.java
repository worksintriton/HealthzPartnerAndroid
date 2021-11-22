package com.triton.healthZpartners.requestpojo;

import java.util.List;

public class SPUpdateTurnoffRequest {

    /**
     * user_id : 60b0b7b267f25056fe286c8f
     * booking_date : 21-08-2021
     * time : [{"booking_time":"03:45 PM","booking_date_time":"21-08-2021 03:45 PM"}]
     */

    private String user_id;
    private String booking_date;
    /**
     * booking_time : 03:45 PM
     * booking_date_time : 21-08-2021 03:45 PM
     */

    private List<TimeBean> time;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    public static class TimeBean {
        private String booking_time;
        private String booking_date_time;

        public String getBooking_time() {
            return booking_time;
        }

        public void setBooking_time(String booking_time) {
            this.booking_time = booking_time;
        }

        public String getBooking_date_time() {
            return booking_date_time;
        }

        public void setBooking_date_time(String booking_date_time) {
            this.booking_date_time = booking_date_time;
        }
    }
}
