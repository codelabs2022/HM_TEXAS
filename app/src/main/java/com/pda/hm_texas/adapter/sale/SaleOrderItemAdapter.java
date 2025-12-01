package com.pda.hm_texas.adapter.sale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.ProdItemViewHolder;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaleOrderItemAdapter  extends RecyclerView.Adapter<SaleLotInStockViewHolder>{
    public List<StockItemDTO> mList;

    private OnItemLongClickListener longClickListener;
    public void SetOnItemLongClickListiner(OnItemLongClickListener callback)
    {
        this.longClickListener = callback;
    }

    public boolean isSales = false;

    public SaleOrderItemAdapter(){
        mList = new ArrayList();
    }
    @NonNull
    @Override
    public SaleLotInStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_view_stockitem, parent, false);

        SaleLotInStockViewHolder holder = new SaleLotInStockViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaleLotInStockViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            StockItemDTO item = mList.get(position);

            holder.liItem.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(position);
                }
                return true;
            });

//            holder.liItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(item.isSelect())
//                    {
//                        item.setSelect(false);
//                    }
//                    else {
//                        item.setSelect(true);
//                    }
//
//                    for(int i=0; i<mList.size(); i++ ){
//                        if(position != i)
//                        {
//                            mList.get(i).setSelect(false);
//                        }
//                    }
//
//                    notifyDataSetChanged();
//                }
//            });

            if(item.isSelect())
            {
                holder.liItem.setBackgroundResource(R.drawable.setgroupbox);
            }
            else {
                holder.liItem.setBackgroundResource(R.drawable.groupbox);
            }


            holder.tvItemNo.setText(item.getItemNo());
            holder.tvItemName.setText(item.getDescription());
            if(isSales) holder.tvBarcode.setText(item.getCustLotNo());
            else holder.tvBarcode.setText(item.getBarCode());
            holder.tvLot.setText(item.getLotNo() );
            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString() + " "+item.getUnitofMeasureCode());
            holder.tvMnf.setText(item.getManufacturingDate());
            holder.tvExp.setText(item.getExpirationDate());
            holder.tvLoc.setText(item.getLocationCode()+ " ::> " + item.getRackCode());
//            holder.tvRemainQty.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    mList.get(holder.getAdapterPosition()).setRemainingQuantity(new BigDecimal(charSequence.toString())); // 데이터셋에 값 반영
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
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
