package com.pda.hm_texas.adapter.stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.dto.RackDTO;

import java.util.ArrayList;
import java.util.List;

public class RackAdapter extends RecyclerView.Adapter<RackViewHolder>{

    public List<RackDTO> mList;
    private RackDTO setRack;
    public RackAdapter(){
        mList = new ArrayList<>();
    }
    @NonNull
    @Override
    public RackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_rack, parent, false);

        RackViewHolder holder = new RackViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RackViewHolder holder, int position) {
        try{
            RackDTO item = mList.get(position);

            holder.liRackItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item.isSelect())
                    {
                        item.setSelect(false);
                        setRack = null;
                    }
                    else
                    {
                        item.setSelect(true);
                        setRack = item;

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
                holder.liRackItem.setBackgroundResource(R.drawable.setgroupbox);
            }
            else {
                holder.liRackItem.setBackgroundResource(R.drawable.groupbox);
            }

            holder.tvRackCode.setText(item.getRackCode());
            holder.tvRackName.setText(item.getRackName());

        }
        catch (Exception ex){

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRack(RackDTO rack) {
        if(rack == null)
        {
            for(int i=0; i<mList.size(); i++ ){
                mList.get(i).setSelect(false);
            }

            notifyDataSetChanged();
        }
        this.setRack = rack;
    }

    public RackDTO getSetRack() {
        return setRack;
    }
}
