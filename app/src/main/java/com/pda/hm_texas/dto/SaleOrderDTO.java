package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class SaleOrderDTO {

    @SerializedName("orderNo")
    @Expose
    private String orderNo;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("selltoCustomerNo")
    @Expose
    private String selltoCustomerNo;
    @SerializedName("selltoCustomerName")
    @Expose
    private String selltoCustomerName;
    @SerializedName("lineNo")
    @Expose
    private String lineNo;
    @SerializedName("itemNo")
    @Expose
    private String itemNo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("unitofMeasureCode")
    @Expose
    private String unitofMeasureCode;
    @SerializedName("orderQty")
    @Expose
    private BigDecimal orderQty;
    @SerializedName("qtyToShip")
    @Expose
    private BigDecimal qtyToShip;
    @SerializedName("qtyShipped")
    @Expose
    private BigDecimal qtyShipped;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private transient boolean isSelect;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getSelltoCustomerNo() {
        return selltoCustomerNo;
    }

    public void setSelltoCustomerNo(String selltoCustomerNo) {
        this.selltoCustomerNo = selltoCustomerNo;
    }

    public String getSelltoCustomerName() {
        return selltoCustomerName;
    }

    public void setSelltoCustomerName(String selltoCustomerName) {
        this.selltoCustomerName = selltoCustomerName;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public String getUnitofMeasureCode() {
        return unitofMeasureCode;
    }

    public void setUnitofMeasureCode(String unitofMeasureCode) {
        this.unitofMeasureCode = unitofMeasureCode;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getQtyToShip() {
        return qtyToShip;
    }

    public void setQtyToShip(BigDecimal qtyToShip) {
        this.qtyToShip = qtyToShip;
    }

    public BigDecimal getQtyShipped() {
        return qtyShipped;
    }

    public void setQtyShipped(BigDecimal qtyShipped) {
        this.qtyShipped = qtyShipped;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
}
