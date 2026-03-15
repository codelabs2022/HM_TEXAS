package com.pda.hm_texas.adapter.prod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.sale.SaleLotInStockViewHolder;
import com.pda.hm_texas.dto.StockItemDTO;

import java.util.ArrayList;
import java.util.List;

public class ProdReleaseItemAdpater  extends RecyclerView.Adapter<SaleLotInStockViewHolder>{

    public List<StockItemDTO> mList;
    public StockItemDTO mSelectedItem;

    public ProdReleaseItemAdpater(){
        mList = new ArrayList();
    }

    @NonNull
    @Override
    public SaleLotInStockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_view_stockitem, viewGroup, false);

        SaleLotInStockViewHolder holder = new SaleLotInStockViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaleLotInStockViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            StockItemDTO item = mList.get(position);

            holder.liItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item.isSelect())
                    {
                        item.setSelect(false);
                    }
                    else {
                        item.setSelect(true);
                        mSelectedItem = item;
                    }

                    for(int i=0; i<mList.size(); i++ ){
                        if(position != i)
                        {
                            mList.get(i).setSelect(false);
                        }
                    }

                    notifyDataSetChanged();
                }
            });

            if(item.isSelect())
            {
                holder.liItem.setBackgroundResource(R.drawable.setgroupbox);
            }
            else {
                holder.liItem.setBackgroundResource(R.drawable.groupbox);
            }


            holder.tvItemNo.setText(item.getItemNo());
            holder.tvItemName.setText(item.getDescription());
            holder.tvBarcode.setText(item.getBarCode());
            holder.tvLot.setText(item.getLotNo() );
            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString() + " "+item.getUnitofMeasureCode());
            holder.tvMnf.setText(item.getManufacturingDate());
            holder.tvExp.setText(item.getExpirationDate());
            holder.tvLoc.setText(item.getLocationCode()+ " ::> " + item.getRackCode());

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
