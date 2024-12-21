package com.pda.hm_texas.adapter.prod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.ProdCompsDTO;
import com.pda.hm_texas.dto.ProdOrderDTO;
import com.pda.hm_texas.event.OnItemClickLintner;

import java.util.ArrayList;
import java.util.List;

public class PlcAdapter extends RecyclerView.Adapter<PlcViewHolder>{
    public List<PlcMatrailDTO> mList = null;
    public PlcMatrailDTO SelectItem;

    private OnItemClickLintner callback;
    public void SetOnClickListiner(OnItemClickLintner callback)
    {
        this.callback = callback;
    }

    public PlcAdapter(){
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_plc, parent, false);

        PlcViewHolder holder = new PlcViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlcViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            PlcMatrailDTO item = mList.get(position);

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


            holder.tvMat.setText(item.getMatCode());
            holder.tvStatus.setText(item.getProdCode());
            holder.tvInput.setText(item.getPlcQty().stripTrailingZeros().toPlainString());
            holder.tvApply.setText(item.getApplyQty().stripTrailingZeros().toPlainString());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void SelectClear(){
        for(int i=0; i<mList.size(); i++ ){
            mList.get(i).setSelect(false);
        }

        notifyDataSetChanged();
    }
}
