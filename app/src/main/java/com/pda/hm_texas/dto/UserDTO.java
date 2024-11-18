package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDTO {

    @SerializedName("COMPANYCD")
    @Expose
    private String COMPANYCD;
    @SerializedName("COMPANYNM")
    @Expose
    private String COMPANYNM;
    @SerializedName("PLANT")
    @Expose
    private String PLANT;
    @SerializedName("PLANTNM")
    @Expose
    private String PLANTNM;
    @SerializedName("USERID")
    @Expose
    private String USERID;
    @SerializedName("USERNM")
    @Expose
    private String USERNM;
    @SerializedName("USERPW")
    @Expose
    private String USERPW;
    @SerializedName("MEMBERTYPE")
    @Expose
    private String MEMBERTYPE;
    @SerializedName("RTN_MSG1")
    @Expose
    private String RTN_MSG1;
    @SerializedName("RTN_MSG2")
    @Expose
    private String RTN_MSG2;
    @SerializedName("RTN_MSGNO")
    @Expose
    private String RTN_MSGNO;

    public String getCOMPANYCD() {
        return COMPANYCD;
    }

    public void setCOMPANYCD(String COMPANYCD) {
        this.COMPANYCD = COMPANYCD;
    }

    public String getCOMPANYNM() {
        return COMPANYNM;
    }

    public void setCOMPANYNM(String COMPANYNM) {
        this.COMPANYNM = COMPANYNM;
    }

    public String getPLANT() {
        return PLANT;
    }

    public void setPLANT(String PLANT) {
        this.PLANT = PLANT;
    }

    public String getPLANTNM() {
        return PLANTNM;
    }

    public void setPLANTNM(String PLANTNM) {
        this.PLANTNM = PLANTNM;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNM() {
        return USERNM;
    }

    public void setUSERNM(String USERNM) {
        this.USERNM = USERNM;
    }

    public String getUSERPW() {
        return USERPW;
    }

    public void setUSERPW(String USERPW) {
        this.USERPW = USERPW;
    }

    public String getMEMBERTYPE() {
        return MEMBERTYPE;
    }

    public void setMEMBERTYPE(String MEMBERTYPE) {
        this.MEMBERTYPE = MEMBERTYPE;
    }

    public String getRTN_MSG1() {
        return RTN_MSG1;
    }

    public void setRTN_MSG1(String RTN_MSG1) {
        this.RTN_MSG1 = RTN_MSG1;
    }

    public String getRTN_MSG2() {
        return RTN_MSG2;
    }

    public void setRTN_MSG2(String RTN_MSG2) {
        this.RTN_MSG2 = RTN_MSG2;
    }

    public String getRTN_MSGNO() {
        return RTN_MSGNO;
    }

    public void setRTN_MSGNO(String RTN_MSGNO) {
        this.RTN_MSGNO = RTN_MSGNO;
    }
}