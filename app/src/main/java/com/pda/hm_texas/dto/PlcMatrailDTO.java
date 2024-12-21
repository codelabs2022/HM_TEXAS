package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class PlcMatrailDTO {



//    @SerializedName("RID")
//    @Expose
//    private long rid;
//    @SerializedName("PROD_CODE")
//    @Expose
//    private String prod_Code;
//    @SerializedName("STEP_NO")
//    @Expose
//    private String step_No;
//    @SerializedName("STEP_STATUS")
//    @Expose
//    private String step_Status;
//    @SerializedName("TANK_1")
//    @Expose
//    private BigDecimal tank1;
//    @SerializedName("TANK_2")
//    @Expose
//    private BigDecimal tank2;
//    @SerializedName("P_TIME")
//    @Expose
//    private int p_Time;
//    @SerializedName("SET_TIME")
//    @Expose
//    private int set_Time;
//    @SerializedName("INPUT_VALUE")
//    @Expose
//    private BigDecimal input_Value;
//    @SerializedName("APPLY_VALUE")
//    @Expose
//    private BigDecimal apply_Value;


    // @JsonProperty("prodOrderNo")
    // private BigDecimal input_1;
    // @JsonProperty("prodOrderNo")
    // private BigDecimal apply_1;
    // @JsonProperty("prodOrderNo")
    // private BigDecimal input_2;
    // @JsonProperty("prodOrderNo")
    // private BigDecimal apply_2;
    // @JsonProperty("prodOrderNo")
    // private BigDecimal input_3;
    // @JsonProperty("prodOrderNo")
    // private BigDecimal apply_3;
    // @JsonProperty("prodOrderNo")
    // private BigDecimal input_4;
    // @JsonProperty("prodOrderNo")
    // private BigDecimal apply_4;
//    @SerializedName("TRANSFER_ERROR_RATE")
//    @Expose
//    private BigDecimal transfer_Error_Rate;
//    @SerializedName("STATUS")
//    @Expose
//    private String status;
//    @SerializedName("STEPMAT")
//    @Expose
//    private String stepMat;
//    @SerializedName("STEPGROUP")
//    @Expose
//    private int stepGroup;
//

//
//    public long getRid() {
//        return rid;
//    }
//
//    public void setRid(long rid) {
//        this.rid = rid;
//    }
//
//    public String getProd_Code() {
//        return prod_Code;
//    }
//
//    public void setProd_Code(String prod_Code) {
//        this.prod_Code = prod_Code;
//    }
//
//    public String getStep_No() {
//        return step_No;
//    }
//
//    public void setStep_No(String step_No) {
//        this.step_No = step_No;
//    }
//
//    public String getStep_Status() {
//        return step_Status;
//    }
//
//    public void setStep_Status(String step_Status) {
//        this.step_Status = step_Status;
//    }
//
//    public BigDecimal getTank1() {
//        return tank1;
//    }
//
//    public void setTank1(BigDecimal tank1) {
//        this.tank1 = tank1;
//    }
//
//    public BigDecimal getTank2() {
//        return tank2;
//    }
//
//    public void setTank2(BigDecimal tank2) {
//        this.tank2 = tank2;
//    }
//
//    public int getP_Time() {
//        return p_Time;
//    }
//
//    public void setP_Time(int p_Time) {
//        this.p_Time = p_Time;
//    }
//
//    public int getSet_Time() {
//        return set_Time;
//    }
//
//    public void setSet_Time(int set_Time) {
//        this.set_Time = set_Time;
//    }
//
//    public BigDecimal getInput_Value() {
//        return input_Value;
//    }
//
//    public void setInput_Value(BigDecimal input_Value) {
//        this.input_Value = input_Value;
//    }
//
//    public BigDecimal getApply_Value() {
//        return apply_Value;
//    }
//
//    public void setApply_Value(BigDecimal apply_Value) {
//        this.apply_Value = apply_Value;
//    }
//
//    public BigDecimal getTransfer_Error_Rate() {
//        return transfer_Error_Rate;
//    }
//
//    public void setTransfer_Error_Rate(BigDecimal transfer_Error_Rate) {
//        this.transfer_Error_Rate = transfer_Error_Rate;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getStepMat() {
//        return stepMat;
//    }
//
//    public void setStepMat(String stepMat) {
//        this.stepMat = stepMat;
//    }
//
//    public int getStepGroup() {
//        return stepGroup;
//    }
//
//    public void setStepGroup(int stepGroup) {
//        this.stepGroup = stepGroup;
//    }

    @SerializedName("RID")
    @Expose
    private long rid;

    @SerializedName("PROD_CODE")
    @Expose
    private String prodCode;

    @SerializedName("MAT_CODE")
    @Expose
    private String matCode;

    @SerializedName("PLC_QTY")
    @Expose
    private BigDecimal plcQty;

    @SerializedName("APPLY_QTY")
    @Expose
    private BigDecimal applyQty;

    @SerializedName("ProdCodeToItem")
    @Expose
    private String prodCodeToItem;

    @SerializedName("MatCodeToItem")
    @Expose
    private String matCodeToItem;

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public BigDecimal getPlcQty() {
        return plcQty;
    }

    public void setPlcQty(BigDecimal plcQty) {
        this.plcQty = plcQty;
    }

    public BigDecimal getApplyQty() {
        return applyQty;
    }

    public void setApplyQty(BigDecimal applyQty) {
        this.applyQty = applyQty;
    }

    public String getProdCodeToItem() {
        return prodCodeToItem;
    }

    public void setProdCodeToItem(String prodCodeToItem) {
        this.prodCodeToItem = prodCodeToItem;
    }

    public String getMatCodeToItem() {
        return matCodeToItem;
    }

    public void setMatCodeToItem(String matCodeToItem) {
        this.matCodeToItem = matCodeToItem;
    }



    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
