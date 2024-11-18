package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersionDTO {

    @SerializedName("NO")
    @Expose
    private int NO;
    @SerializedName("PG_TYPE")
    @Expose
    private String PG_TYPE;
    @SerializedName("VERSION")
    @Expose
    private String VERSION;
    @SerializedName("FILENAME")
    @Expose
    private String FILENAME;
    @SerializedName("FILEPATH")
    @Expose
    private String FILEPATH;

    public int getNO() {
        return NO;
    }

    public void setNO(int NO) {
        this.NO = NO;
    }

    public String getPG_TYPE() {
        return PG_TYPE;
    }

    public void setPG_TYPE(String PG_TYPE) {
        this.PG_TYPE = PG_TYPE;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getFILEPATH() {
        return FILEPATH;
    }

    public void setFILEPATH(String FILEPATH) {
        this.FILEPATH = FILEPATH;
    }
}
