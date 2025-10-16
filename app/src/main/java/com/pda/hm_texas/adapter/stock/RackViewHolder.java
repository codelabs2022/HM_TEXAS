package com.pda.hm_texas.adapter.stock;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class RackViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout liRackItem;
    public TextView tvRackCode, tvRackName;

    public RackViewHolder(@NonNull View itemView) {
        super(itemView);

        liRackItem = itemView.findViewById(R.id.llRack);
        tvRackCode = itemView.findViewById(R.id.tvRackCode);
        tvRackName = itemView.findViewById(R.id.tvRackName);
    }
}
