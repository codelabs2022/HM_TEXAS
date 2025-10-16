package com.pda.hm_texas.adapter.prod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.dto.FactoryDTO;
import com.pda.hm_texas.event.OnItemClickLintner;

import java.util.ArrayList;
import java.util.List;

public class FactoryAdaper extends RecyclerView.Adapter<FactoryViewHolder>{

    public List<FactoryDTO> mList = null;
    private OnItemClickLintner callback;
    public void SetOnClickListiner(OnItemClickLintner callback)
    {
        this.callback = callback;
    }

    public FactoryAdaper(){
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FactoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_factory, parent, false);

        FactoryViewHolder holder = new FactoryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FactoryViewHolder holder, int position) {
        try{

            FactoryDTO item = mList.get(position);

            holder.btnFactory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onItemSelect(view, position);
                }
            });

            holder.btnFactory.setText(item.getRoutNo());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
