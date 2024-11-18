package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TransBarcodeItemDTO {

    @SerializedName("ItemNo")
    @Expose
    private String itemNo;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("LocationCode")
    @Expose
    private String locationCode;
    @SerializedName("LocationName")
    @Expose
    private String locationName;
    @SerializedName("Barcode")
    @Expose
    private String barcode;
    @SerializedName("LotNo")
    @Expose
    private String lotNo;
    @SerializedName("ManufacturingDate")
    @Expose
    private String manufacturingDate;
    @SerializedName("ExpirationDate")
    @Expose
    private String expirationDate;
    @SerializedName("UnitofMeasureCode")
    @Expose
    private String unitofMeasureCode;
    @SerializedName("RemainingQuantity")
    @Expose
    private BigDecimal remainingQuantity;
    @SerializedName("CustLotNo")
    @Expose
    private String custLotNo;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUnitofMeasureCode() {
        return unitofMeasureCode;
    }

    public void setUnitofMeasureCode(String unitofMeasureCode) {
        this.unitofMeasureCode = unitofMeasureCode;
    }

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getCustLotNo() {
        return custLotNo;
    }

    public void setCustLotNo(String custLotNo) {
        this.custLotNo = custLotNo;
    }
}
