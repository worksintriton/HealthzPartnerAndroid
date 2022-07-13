package com.triton.healthzpartners.requestpojo;

import java.util.List;

public class FamilyMemberCreateRequest {


    /**
     * user_id : 618230269dcc2a290e5bae9a
     * name : Mohammed
     * gender : Male
     * relation_type : Son
     * health_issue : No issue
     * dateofbirth : 23-10-2021
     * anymedicalinfo : No Issue
     * covide_vac : Yes
     * weight : 70
     * pic : [{"image":"http://Google.com"}]
     */

    private String user_id;
    private String name;
    private String gender;
    private String relation_type;
    private String health_issue;
    private String dateofbirth;
    private String anymedicalinfo;
    private String covide_vac;
    private String weight;
    /**
     * image : http://Google.com
     */

    private List<PicBean> pic;

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

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public static class PicBean {
        private String image;

        public PicBean(String serverUrlImagePath) {

            this.image =serverUrlImagePath;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
