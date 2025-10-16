package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RackDTO {

    @SerializedName("rackCode")
    @Expose
    private String rackCode;

    @SerializedName("rackName")
    @Expose
    private String rackName;

    @SerializedName("locationCode")
    @Expose
    private String lcoationCode;

    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getRackCode() {
        return rackCode;
    }

    public void setRackCode(String rackCode) {
        this.rackCode = rackCode;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public String getLcoationCode() {
        return lcoationCode;
    }

    public void setLcoationCode(String lcoationCode) {
        this.lcoationCode = lcoationCode;
    }
}
