package com.pda.hm_texas.adapter.sale;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.ProdItemAdapter;
import com.pda.hm_texas.adapter.prod.ProdItemViewHolder;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class SalePickingAdapter extends RecyclerView.Adapter<SalePickingViewHolder>{
    public List<StockItemDTO> mList = null;
    private Context mContext;
    private InputMethodManager imm;
    public boolean isSales = false;
    //
    private OnItemLongClickListener longClickListener;
    public void SetOnItemLongClickListiner(OnItemLongClickListener callback)
    {
        this.longClickListener = callback;
    }

    public SalePickingAdapter(Context _context){

        mList = new ArrayList<>();
        mList.clear();
        mContext = _context;
        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
    }

    @NonNull
    @Override
    public SalePickingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_pickingitem, parent, false);

        SalePickingViewHolder holder = new SalePickingViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SalePickingViewHolder holder, int position) {
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
            holder.tvBarcode.setText(item.getCustLotNo());
            holder.tvLot.setText(item.getLotNo());
            holder.tvMfc.setText(item.getManufacturingDate());
            holder.tvExp.setText(item.getExpirationDate());
            holder.tvUnit.setText(item.getUnitofMeasureCode());

            //holder.textWatcher.updateData(position, item, mList, qtyChangeListner);
            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return  mList.size();
    }
}
