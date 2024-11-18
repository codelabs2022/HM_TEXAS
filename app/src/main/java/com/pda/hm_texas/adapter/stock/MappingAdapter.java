package com.pda.hm_texas.adapter.stock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.OrderViewHolder;
import com.pda.hm_texas.dto.SaleOrderDTO;
import com.pda.hm_texas.dto.TransBarcodeItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MappingAdapter extends RecyclerView.Adapter<MappingViewHolder>{

    public List<TransBarcodeItemDTO> mList;
    public TransBarcodeItemDTO SelectItem;
    private OnItemLongClickListener longClickListener;
    public void SetOnItemLongClickListiner(OnItemLongClickListener callback)
    {
        this.longClickListener = callback;
    }

    public MappingAdapter(){
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MappingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_barcodemap, parent, false);

        MappingViewHolder holder = new MappingViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MappingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            TransBarcodeItemDTO item = mList.get(position);

            holder.liItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(longClickListener != null){
                        int pos = position;
                        if (pos != RecyclerView.NO_POSITION) {
                            // 리스너 객체의 메서드 호출.
                            SelectItem = item;
                            longClickListener.onItemLongClick(pos);
                            return true;
                        }
                    }

                    return false;
                }
            });

            holder.tvBarcode.setText(item.getBarcode());
            holder.tvCustBarcode.setText(item.getCustLotNo());
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
