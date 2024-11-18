package com.pda.hm_texas.adapter.prod;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class PlcViewHolder extends RecyclerView.ViewHolder{
    public LinearLayout liItem;
    public TextView tvMat, tvStatus  , tvInput, tvApply;

    public PlcViewHolder(@NonNull View itemView) {
        super(itemView);

        liItem = itemView.findViewById(R.id.llPlc);
        tvMat = itemView.findViewById(R.id.tvPlcMat);
        tvStatus = itemView.findViewById(R.id.tvPlcStatus);
        tvInput = itemView.findViewById(R.id.tvPlcInput);
        tvApply = itemView.findViewById(R.id.tvPlcApply);
    }
}
