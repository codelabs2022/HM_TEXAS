package com.pda.hm_texas.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StockItemDTO implements Parcelable {
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
    @SerializedName("rackCode")
    @Expose
    private String rackCode;

    private BigDecimal originalRemainingQuantity = new BigDecimal(0);

    public BigDecimal getOriginalRemainingQuantity() {
        return originalRemainingQuantity;
    }

    public void setOriginalRemainingQuantity(BigDecimal qty) {
        this.originalRemainingQuantity = qty;
    }

    public String getCustLotNo() {
        return custLotNo;
    }

    public void setCustLotNo(String custLotNo) {
        this.custLotNo = custLotNo;
    }

    @SerializedName("custLotNo")
    @Expose
    private String custLotNo;


    private transient boolean isSelect = false;
    private transient BigDecimal emptyCaseQty;

    public BigDecimal getEmptyCaseQty() {
        return emptyCaseQty;
    }

    public void setEmptyCaseQty(BigDecimal emptyCaseQty) {
        this.emptyCaseQty = emptyCaseQty;
    }



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

    public String getRackCode() {
        return rackCode;
    }

    public void setRackCode(String rackCode) {
        this.rackCode = rackCode;
    }

    protected StockItemDTO(Parcel in) {
        itemNo = in.readString();
        description = in.readString();
        locationCode = in.readString();
        lotNo = in.readString();
        barCode = in.readString();
        manufacturingDate = in.readString();
        expirationDate = in.readString();

        // remainingQuantity (BigDecimal) 읽기 로직
        remainingQuantity = readBigDecimal(in);

        unitofMeasureCode = in.readString();
        rackCode = in.readString();

        // transient 필드 읽기 (boolean과 BigDecimal)
        isSelect = in.readInt() == 1; // boolean 읽기
        emptyCaseQty = readBigDecimal(in);
    }


    public static final Creator<StockItemDTO> CREATOR = new Creator<StockItemDTO>() {
        @Override
        public StockItemDTO createFromParcel(Parcel in) {
            return new StockItemDTO(in);
        }

        @Override
        public StockItemDTO[] newArray(int size) {
            return new StockItemDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(itemNo);
        parcel.writeString(description);
        parcel.writeString(locationCode);
        parcel.writeString(lotNo);
        parcel.writeString(barCode);
        parcel.writeString(manufacturingDate);
        parcel.writeString(expirationDate);

        // remainingQuantity (BigDecimal) 쓰기 로직
        writeBigDecimal(parcel, remainingQuantity);

        parcel.writeString(unitofMeasureCode);
        parcel.writeString(rackCode);

        // transient 필드 쓰기 (boolean과 BigDecimal)
        parcel.writeInt(isSelect ? 1 : 0); // boolean 쓰기
        writeBigDecimal(parcel, emptyCaseQty);
    }

    private void writeBigDecimal(Parcel dest, BigDecimal value) {
        if (value == null) {
            dest.writeInt(0); // Null 마커
        } else {
            dest.writeInt(1); // Not Null 마커
            dest.writeByteArray(value.unscaledValue().toByteArray());
            dest.writeInt(value.scale());
        }
    }

    // Parcel에서 BigDecimal을 읽는 도우미 메서드 (Null 처리 포함)
    private BigDecimal readBigDecimal(Parcel in) {
        int isNotNull = in.readInt();
        if (isNotNull == 0) {
            return null;
        } else {
            byte[] unscaledBytes = in.createByteArray();
            int scale = in.readInt();
            BigInteger unscaledValue = new BigInteger(unscaledBytes);
            return new BigDecimal(unscaledValue, scale);
        }
    }
}
