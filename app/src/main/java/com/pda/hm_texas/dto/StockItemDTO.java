package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class StockItemDTO {
    @SerializedName("itemNo")
    @Expose
    private String itemNo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("lotNo")
    @Expose
    private String lotNo;
    @SerializedName("barCode")
    @Expose
    private String barCode;
    @SerializedName("manufacturingDate")
    @Expose
    private String manufacturingDate;
    @SerializedName("expirationDate")
    @Expose
    private String expirationDate;
    @SerializedName("remainingQuantity")
    @Expose
    private BigDecimal remainingQuantity;
    @SerializedName("unitofMeasureCode")
    @Expose
    private String unitofMeasureCode;

    private transient BigDecimal emptyCaseQty;

    public BigDecimal getEmptyCaseQty() {
        return emptyCaseQty;
    }

    public void setEmptyCaseQty(BigDecimal emptyCaseQty) {
        this.emptyCaseQty = emptyCaseQty;
    }

    private transient boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getUnitofMeasureCode() {
        return unitofMeasureCode;
    }

    public void setUnitofMeasureCode(String unitofMeasureCode) {
        this.unitofMeasureCode = unitofMeasureCode;
    }
}
