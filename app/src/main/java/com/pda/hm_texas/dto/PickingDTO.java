package com.pda.hm_texas.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PickingDTO {

    @SerializedName("order")
    @Expose
    private SaleOrderDTO order;
    @SerializedName("items")
    @Expose
    private List<StockItemDTO> items;

    public SaleOrderDTO getOrder() {
        return order;
    }

    public void setOrder(SaleOrderDTO order) {
        this.order = order;
    }

    public List<StockItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StockItemDTO> items) {
        this.items = items;
    }
}
