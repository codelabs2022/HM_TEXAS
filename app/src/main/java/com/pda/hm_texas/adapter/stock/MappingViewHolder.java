package com.pda.hm_texas.adapter.stock;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class MappingViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout liItem;
    public TextView tvBarcode, tvCustBarcode;

    public MappingViewHolder(@NonNull View itemView) {
        super(itemView);

        liItem = itemView.findViewById(R.id.llMappingItem);
        tvBarcode = itemView.findViewById(R.id.tvTransBarcode);
        tvCustBarcode = itemView.findViewById(R.id.tvTransCustBarcode);
    }
}
