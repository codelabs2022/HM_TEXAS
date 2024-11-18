package com.pda.hm_texas.adapter.stock;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.adapter.sale.SaleLotInStockViewHolder;
import com.pda.hm_texas.dto.StockItemDTO;

import java.util.List;

public class SearchItemAdaper extends RecyclerView.Adapter<SaleLotInStockViewHolder>{

    public List<StockItemDTO> mList;

    @NonNull
    @Override
    public SaleLotInStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SaleLotInStockViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
