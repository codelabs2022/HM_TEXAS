package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ProdCompsDTO {

    @SerializedName("prodOrderNo")
    @Expose
    private String prodOrderNo;
    @SerializedName("prodOrderLineNo")
    @Expose
    private int prodOrderLineNo;
    @SerializedName("PRODCODE")
    @Expose
    private String prodCode;
    @SerializedName("compsLineNo")
    @Expose
    private int compsLineNo;
    @SerializedName("compsLocation")
    @Expose
    private String compsLocation;
    @SerializedName("ITEMNO")
    @Expose
    private String itemNo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("expectedQuantity")
    @Expose
    private BigDecimal expectedQuantity;
    @SerializedName("releaseQty")
    @Expose
    private BigDecimal releaseQty;

    @SerializedName("unit")
    @Expose
    private String unit;

    private boolean isSelect = false;

    public String getProdOrderNo() {
        return prodOrderNo;
    }

    public void setProdOrderNo(String prodOrderNo) {
        this.prodOrderNo = prodOrderNo;
    }

    public int getProdOrderLineNo() {
        return prodOrderLineNo;
    }

    public void setProdOrderLineNo(int prodOrderLineNo) {
        this.prodOrderLineNo = prodOrderLineNo;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public int getCompsLineNo() {
        return compsLineNo;
    }

    public void setCompsLineNo(int compsLineNo) {
        this.compsLineNo = compsLineNo;
    }

    public String getCompsLocation() {
        return compsLocation;
    }

    public void setCompsLocation(String compsLocation) {
        this.compsLocation = compsLocation;
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

    public BigDecimal getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(BigDecimal expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public BigDecimal getReleaseQty() {
        return releaseQty;
    }

    public void setReleaseQty(BigDecimal releaseQty) {
        this.releaseQty = releaseQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
