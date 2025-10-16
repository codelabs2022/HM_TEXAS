package com.pda.hm_texas.adapter.prod;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class FactoryViewHolder extends RecyclerView.ViewHolder{

    public Button btnFactory;
    public FactoryViewHolder(@NonNull View itemView) {
        super(itemView);

        btnFactory = itemView.findViewById(R.id.btnItemFactory);
    }
}
