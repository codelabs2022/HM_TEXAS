package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FactoryDTO {

    @SerializedName("fcode")
    @Expose
    private String fcode;

    @SerializedName("fname")
    @Expose
    private String fname;

    @SerializedName("routNo")
    @Expose
    private String routNo;

    @SerializedName("fnameEng")
    @Expose
    private String fnameEng;

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getRoutNo() {
        return routNo;
    }

    public void setRoutNo(String routNo) {
        this.routNo = routNo;
    }

    public String getFnameEng() {
        return fnameEng;
    }

    public void setFnameEng(String fnameEng) {
        this.fnameEng = fnameEng;
    }
}
