package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DbResult2VO {

    @SerializedName("ERR_CODE")
    @Expose
    private String err_CODE;
    @SerializedName("ERR_MSG")
    @Expose
    private String err_MSG;

    public String getERR_CODE() {
        return err_CODE;
    }

    public void setERR_CODE(String err_CODE) {
        this.err_CODE = err_CODE;
    }

    public String getERR_MSG() {
        return err_MSG;
    }

    public void setERR_MSG(String err_MSG) {
        this.err_MSG = err_MSG;
    }
}
