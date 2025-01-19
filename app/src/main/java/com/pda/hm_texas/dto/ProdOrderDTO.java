package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ProdOrderDTO {

    @SerializedName("workStatusCode")
    @Expose
    private String workStatusCode;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    @SerializedName("prodOrderNo")
    @Expose
    private String prodOrderNo;
    @SerializedName("prodOrderLineNo")
    @Expose
    private int prodOrderLineNo;
    @SerializedName("itemNo")
    @Expose
    private String itemNo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("routingNo")
    @Expose
    private String routingNo;
    @SerializedName("operationNo")
    @Expose
    private String operationNo;
    @SerializedName("productionBOMNo")
    @Expose
    private String productionBOMNo;
    @SerializedName("dxkLotNo")
    @Expose
    private String dxkLotNo;
    @SerializedName("quantity")
    @Expose
    private BigDecimal quantity;
    @SerializedName("finishedQuantity")
    @Expose
    private BigDecimal finishedQuantity;

    @SerializedName("remainingQuantity")
    @Expose
    private BigDecimal remainingQuantity;

    @SerializedName("unitOfMeasureCode")
    @Expose
    private String unitOfMeasureCode;

    public String getUnitOfMeasureCode() {
        return unitOfMeasureCode;
    }

    public void setUnitOfMeasureCode(String unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect;

    public String getWorkStatusCode() {
        return workStatusCode;
    }

    public void setWorkStatusCode(String workStatusCode) {
        this.workStatusCode = workStatusCode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

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

    public String getRoutingNo() {
        return routingNo;
    }

    public void setRoutingNo(String routingNo) {
        this.routingNo = routingNo;
    }

    public String getOperationNo() {
        return operationNo;
    }

    public void setOperationNo(String operationNo) {
        this.operationNo = operationNo;
    }

    public String getProductionBOMNo() {
        return productionBOMNo;
    }

    public void setProductionBOMNo(String productionBOMNo) {
        this.productionBOMNo = productionBOMNo;
    }

    public String getDxkLotNo() {
        return dxkLotNo;
    }

    public void setDxkLotNo(String dxkLotNo) {
        this.dxkLotNo = dxkLotNo;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getFinishedQuantity() {
        return finishedQuantity;
    }

    public void setFinishedQuantity(BigDecimal finishedQuantity) {
        this.finishedQuantity = finishedQuantity;
    }

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }
}
