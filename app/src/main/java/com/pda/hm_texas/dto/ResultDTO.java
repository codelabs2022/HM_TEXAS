package com.pda.hm_texas.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultDTO {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("errMsg")
    @Expose
    private String errMsg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}