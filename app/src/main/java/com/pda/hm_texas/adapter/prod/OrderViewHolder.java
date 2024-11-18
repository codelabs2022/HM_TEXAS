package com.pda.hm_texas.adapter.prod;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class OrderViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout liItem;
    public TextView tvOrerDate, tvOrerNo, tvOrerLineNo
                    , tvItemNo, tvItemName
                    , tvOrderQty, tvOrderProdQty, tvOrderRemainQty;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        liItem = itemView.findViewById(R.id.llProdOrder);
        tvOrerDate = itemView.findViewById(R.id.tvItemOrderDate);
        tvOrerNo = itemView.findViewById(R.id.tvItemOrderNo);
        tvOrerLineNo = itemView.findViewById(R.id.tvItemOrderLineNo);
        tvItemNo = itemView.findViewById(R.id.tvOrderItemNo);
        tvItemName = itemView.findViewById(R.id.tvItemName);
        tvOrderQty = itemView.findViewById(R.id.tvItemOrderQty);
        tvOrderProdQty = itemView.findViewById(R.id.tvItemOrderProdQty);
        tvOrderRemainQty = itemView.findViewById(R.id.tvItemOrderRemainQty);
    }
}
