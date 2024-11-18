package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class EnterOrderDTO {

    @SerializedName("documentNo")
    @Expose
    private String documentNo;
    @SerializedName("lineNo")
    @Expose
    private int lineNo;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("buyfromVendorNo")
    @Expose
    private String buyfromVendorNo;
    @SerializedName("buyfromVendorName")
    @Expose
    private String buyfromVendorName;
    @SerializedName("itemNo")
    @Expose
    private String itemNo;
    @SerializedName("itemDescription")
    @Expose
    private String itemDescription;
    @SerializedName("baseUnitofMeasure")
    @Expose
    private String baseUnitofMeasure;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("orderQty")
    @Expose
    private BigDecimal orderQty;
    @SerializedName("qtyReceived")
    @Expose
    private BigDecimal qtyReceived;
    @SerializedName("qytoReceive")
    @Expose
    private BigDecimal qytoReceive;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect;

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getBuyfromVendorNo() {
        return buyfromVendorNo;
    }

    public void setBuyfromVendorNo(String buyfromVendorNo) {
        this.buyfromVendorNo = buyfromVendorNo;
    }

    public String getBuyfromVendorName() {
        return buyfromVendorName;
    }

    public void setBuyfromVendorName(String buyfromVendorName) {
        this.buyfromVendorName = buyfromVendorName;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getBaseUnitofMeasure() {
        return baseUnitofMeasure;
    }

    public void setBaseUnitofMeasure(String baseUnitofMeasure) {
        this.baseUnitofMeasure = baseUnitofMeasure;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getQtyReceived() {
        return qtyReceived;
    }

    public void setQtyReceived(BigDecimal qtyReceived) {
        this.qtyReceived = qtyReceived;
    }

    public BigDecimal getQytoReceive() {
        return qytoReceive;
    }

    public void setQytoReceive(BigDecimal qytoReceive) {
        this.qytoReceive = qytoReceive;
    }
}
