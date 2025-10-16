package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RackMoveDTO {

    @SerializedName("locationCode")
    @Expose
    private String locationCode;

    @SerializedName("fromRackCode")
    @Expose
    private String fromRackCode;

    @SerializedName("toRackCode")
    @Expose
    private String toRackCode;

    @SerializedName("itemNo")
    @Expose
    private String itemNo;

    @SerializedName("lotNo")
    @Expose
    private String lotNo;

    @SerializedName("barcode")
    @Expose
    private String barcode;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getFromRackCode() {
        return fromRackCode;
    }

    public void setFromRackCode(String fromRackCode) {
        this.fromRackCode = fromRackCode;
    }

    public String getToRackCode() {
        return toRackCode;
    }

    public void setToRackCode(String toRackCode) {
        this.toRackCode = toRackCode;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
