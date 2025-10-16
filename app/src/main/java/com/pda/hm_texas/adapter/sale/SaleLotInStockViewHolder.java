package com.pda.hm_texas.adapter.sale;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class SaleLotInStockViewHolder extends RecyclerView.ViewHolder{
    public LinearLayout liItem;
    public TextView tvItemNo, tvItemName, tvBarcode, tvLot,  tvRemainQty, tvLoc, tvMnf, tvExp, tvRack;

    public SaleLotInStockViewHolder(@NonNull View itemView) {
        super(itemView);

        liItem = itemView.findViewById(R.id.llSaleLotinItem);
        tvItemNo = itemView.findViewById(R.id.tvSaleItemNo);
        tvItemName = itemView.findViewById(R.id.tvSaleItemName);
        tvRemainQty = itemView.findViewById(R.id.tvSaleItemQty);
        tvBarcode = itemView.findViewById(R.id.tvSaleItemBarcode);
        tvLot = itemView.findViewById(R.id.tvSaleItemLotNo);
        tvLoc = itemView.findViewById(R.id.tvSaleItemLoc);
        tvMnf = itemView.findViewById(R.id.tvSaleItemMnfDt);
        tvExp = itemView.findViewById(R.id.tvSaleItemExpDt);
        tvRack = itemView.findViewById(R.id.tvSaleItemLoc);
    }
}
