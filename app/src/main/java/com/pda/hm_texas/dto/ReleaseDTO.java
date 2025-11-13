package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ReleaseDTO {

    @SerializedName("P_WKDATE")
    @Expose
    private String p_WKDATE;
    @SerializedName("P_WORKORDER")
    @Expose
    private String p_WORKORDER;
    @SerializedName("P_WORKORDERLINE")
    @Expose
    private int p_WORKORDERLINE;
    @SerializedName("P_ROUTNO")
    @Expose
    private String p_ROUTNO;
    @SerializedName("P_BOMNO")
    @Expose
    private String p_BOMNO;
    @SerializedName("P_PRODITEMNO")
    @Expose
    private String p_PRODITEMNO;
    @SerializedName("P_COMPSLINENO")
    @Expose
    private int p_COMPSLINENO;
    @SerializedName("P_COMPSITEMNO")
    @Expose
    private String p_COMPSITEMNO;
    @SerializedName("P_COMPSLOCATION")
    @Expose
    private String p_COMPSLOCATION;
    @SerializedName("P_COMPSRACK")
    @Expose
    private String p_COMPSRACK;
    @SerializedName("P_COMPSQTY")
    @Expose
    private BigDecimal p_COMPSQTY;
    @SerializedName("P_COMPSEMPTYQTY")
    @Expose
    private BigDecimal p_COMPSEMPTYQTY;
    @SerializedName("P_COMPSLOTNO")
    @Expose
    private String p_COMPSLOTNO;
    @SerializedName("P_COMPSEXPDATE")
    @Expose
    private String p_COMPSEXPDATE;
    @SerializedName("P_CUSTLOTNO")
    @Expose
    private String p_CUSTLOTNO;
    @SerializedName("P_ERPLOCATION")
    @Expose
    private String p_ERPLOCATION;
    @SerializedName("P_MANUFACTURINGDATE")
    @Expose
    private String p_MANUFACTURINGDATE;

    public String getP_WKDATE() {
        return p_WKDATE;
    }

    public void setP_WKDATE(String p_WKDATE) {
        this.p_WKDATE = p_WKDATE;
    }

    public String getP_WORKORDER() {
        return p_WORKORDER;
    }

    public void setP_WORKORDER(String p_WORKORDER) {
        this.p_WORKORDER = p_WORKORDER;
    }

    public int getP_WORKORDERLINE() {
        return p_WORKORDERLINE;
    }

    public void setP_WORKORDERLINE(int p_WORKORDERLINE) {
        this.p_WORKORDERLINE = p_WORKORDERLINE;
    }

    public String getP_ROUTNO() {
        return p_ROUTNO;
    }

    public void setP_ROUTNO(String p_ROUTNO) {
        this.p_ROUTNO = p_ROUTNO;
    }

    public String getP_BOMNO() {
        return p_BOMNO;
    }

    public void setP_BOMNO(String p_BOMNO) {
        this.p_BOMNO = p_BOMNO;
    }

    public String getP_PRODITEMNO() {
        return p_PRODITEMNO;
    }

    public void setP_PRODITEMNO(String p_PRODITEMNO) {
        this.p_PRODITEMNO = p_PRODITEMNO;
    }

    public int getP_COMPSLINENO() {
        return p_COMPSLINENO;
    }

    public void setP_COMPSLINENO(int p_COMPSLINENO) {
        this.p_COMPSLINENO = p_COMPSLINENO;
    }

    public String getP_COMPSITEMNO() {
        return p_COMPSITEMNO;
    }

    public void setP_COMPSITEMNO(String p_COMPSITEMNO) {
        this.p_COMPSITEMNO = p_COMPSITEMNO;
    }

    public String getP_COMPSLOCATION() {
        return p_COMPSLOCATION;
    }

    public void setP_COMPSLOCATION(String p_COMPSLOCATION) {
        this.p_COMPSLOCATION = p_COMPSLOCATION;
    }

    public BigDecimal getP_COMPSQTY() {
        return p_COMPSQTY;
    }

    public void setP_COMPSQTY(BigDecimal p_COMPSQTY) {
        this.p_COMPSQTY = p_COMPSQTY;
    }

    public BigDecimal getP_COMPSEMPTYQTY() {
        return p_COMPSEMPTYQTY;
    }

    public void setP_COMPSEMPTYQTY(BigDecimal p_COMPSEMPTYQTY) {
        this.p_COMPSEMPTYQTY = p_COMPSEMPTYQTY;
    }

    public String getP_COMPSLOTNO() {
        return p_COMPSLOTNO;
    }

    public void setP_COMPSLOTNO(String p_COMPSLOTNO) {
        this.p_COMPSLOTNO = p_COMPSLOTNO;
    }

    public String getP_COMPSEXPDATE() {
        return p_COMPSEXPDATE;
    }

    public void setP_COMPSEXPDATE(String p_COMPSEXPDATE) {
        this.p_COMPSEXPDATE = p_COMPSEXPDATE;
    }

    public String getP_CUSTLOTNO() {
        return p_CUSTLOTNO;
    }

    public void setP_CUSTLOTNO(String p_CUSTLOTNO) {
        this.p_CUSTLOTNO = p_CUSTLOTNO;
    }

    public String getP_ERPLOCATION() {
        return p_ERPLOCATION;
    }

    public void setP_ERPLOCATION(String p_ERPLOCATION) {
        this.p_ERPLOCATION = p_ERPLOCATION;
    }

    public String getP_MANUFACTURINGDATE() {
        return p_MANUFACTURINGDATE;
    }

    public void setP_MANUFACTURINGDATE(String p_MANUFACTURINGDATE) {
        this.p_MANUFACTURINGDATE = p_MANUFACTURINGDATE;
    }

    public String getP_COMPSRACK() {
        return p_COMPSRACK;
    }

    public void setP_COMPSRACK(String p_COMPSRACK) {
        this.p_COMPSRACK = p_COMPSRACK;
    }
}
