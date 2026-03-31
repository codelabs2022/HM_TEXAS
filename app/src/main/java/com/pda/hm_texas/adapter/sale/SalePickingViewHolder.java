package com.pda.hm_texas.adapter.sale;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class SalePickingViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout liItem;
    public TextView tvItemNo, tvItemName, tvBarcode, tvLot, tvMfc, tvExp, tvUnit ;
    public TextView tvRemainQty;

    private InputMethodManager imm;

    public SalePickingViewHolder(@NonNull View itemView) {
        super(itemView);

        liItem = itemView.findViewById(R.id.llProdStockItem);
        tvItemNo = itemView.findViewById(R.id.tvStockItemNo);
        tvItemName = itemView.findViewById(R.id.tvStockItemName);
        tvRemainQty = itemView.findViewById(R.id.etProdQty);
        tvBarcode = itemView.findViewById(R.id.tvStockItemBarcode);
        tvLot = itemView.findViewById(R.id.tvStockItemLotNo);
        tvMfc = itemView.findViewById(R.id.tvStockItemMnf);
        tvExp = itemView.findViewById(R.id.tvStockItemExp);
        tvUnit = itemView.findViewById(R.id.tvStockItemUnit);
    }
}
