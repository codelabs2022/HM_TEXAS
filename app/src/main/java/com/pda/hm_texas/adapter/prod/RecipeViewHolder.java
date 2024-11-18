package com.pda.hm_texas.adapter.prod;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout liItem;
    public TextView tvItemNo, tvItemName, tvProdCode
            , tvloc, tvNeedQty, tvRemainQty;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        liItem = itemView.findViewById(R.id.llRecipe);
        tvItemNo = itemView.findViewById(R.id.tvRecipeItemNo);
        tvItemName = itemView.findViewById(R.id.tvRecipeItemName);
        tvProdCode = itemView.findViewById(R.id.tvRecipeProdCode);
        tvloc = itemView.findViewById(R.id.tvRecipeLoc);
        tvNeedQty = itemView.findViewById(R.id.tvRecipeNeedQty);
        tvRemainQty = itemView.findViewById(R.id.tvRecipeRQty);
    }
}
