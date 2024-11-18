package com.pda.hm_texas.adapter.enter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.OrderViewHolder;
import com.pda.hm_texas.dto.EnterOrderDTO;
import com.pda.hm_texas.dto.ProdOrderDTO;
import com.pda.hm_texas.event.OnItemClickLintner;

import java.util.ArrayList;
import java.util.List;

public class EnterOrderAdapter extends RecyclerView.Adapter<OrderViewHolder>{

    public List<EnterOrderDTO> mList = null;
    public EnterOrderDTO SelectItem;
    private OnItemClickLintner callback;
    public void SetOnClickListiner(OnItemClickLintner callback)
    {
        this.callback = callback;
    }

    public EnterOrderAdapter(){
        mList = new ArrayList<>();
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_order, parent, false);

        OrderViewHolder holder = new OrderViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            EnterOrderDTO item = mList.get(position);

            holder.liItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item.isSelect())
                    {
                        item.setSelect(false);
                    }
                    else {
                        item.setSelect(true);

                        if(callback != null){
                            int pos = position;
                            if (pos != RecyclerView.NO_POSITION) {
                                // 리스너 객체의 메서드 호출.
                                SelectItem = item;
                                callback.onItemSelect(view, pos);
                            }
                        }
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

            holder.tvOrerDate.setText(item.getOrderDate());
            holder.tvOrerNo.setText(item.getDocumentNo());
            holder.tvOrerLineNo.setText(item.getBuyfromVendorName());
            holder.tvItemNo.setText(item.getItemNo());
            holder.tvItemName.setText(item.getItemDescription());
            holder.tvOrderQty.setText(item.getOrderQty().stripTrailingZeros().toPlainString());
            holder.tvOrderProdQty.setText(item.getQtyReceived().stripTrailingZeros().toPlainString());
            holder.tvOrderRemainQty.setText(item.getQytoReceive().stripTrailingZeros().toPlainString());
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
