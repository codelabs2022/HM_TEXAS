package com.pda.hm_texas.adapter.prod;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.helper.Utility;
//import com.pda.hm_texas.event.OnItemLongClickListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdItemAdapter extends RecyclerView.Adapter<ProdItemViewHolder>{
    public List<StockItemDTO> mList = null;
    private Context mContext;
//
    private OnItemLongClickListener longClickListener;
    public void SetOnItemLongClickListiner(OnItemLongClickListener callback)
    {
        this.longClickListener = callback;
    }

    public ProdItemAdapter(Context _context){
        mList = new ArrayList<>();
        mContext = _context;
    }
    @NonNull
    @Override
    public ProdItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_stockitem, parent, false);

        ProdItemViewHolder holder = new ProdItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdItemViewHolder holder, int position) {
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
            holder.tvBarcode.setText(item.getBarCode());
            holder.tvLot.setText(item.getLotNo());
            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
            holder.tvRemainQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    //mList.get(holder.getAdapterPosition()).setRemainingQuantity(new BigDecimal(charSequence.toString())); // 데이터셋에 값 반영
                    if (charSequence == null || charSequence.toString().trim().isEmpty()) {
                        mList.get(holder.getAdapterPosition()).setRemainingQuantity(BigDecimal.ZERO); // 0으로 설정
                        return;
                    }

                    try {
                        // BigDecimal 생성
                        BigDecimal value = new BigDecimal(charSequence.toString());

                        // 0보다 큰지 확인
                        if (value.compareTo(BigDecimal.ZERO) >= 0) {
                            mList.get(holder.getAdapterPosition()).setRemainingQuantity(value); // 값 반영
                        } else {
                            // 음수 입력 시 처리 (옵션)
                            mList.get(holder.getAdapterPosition()).setRemainingQuantity(BigDecimal.ZERO); // 0으로 설정
                        }
                    } catch (NumberFormatException e) {
                        // 숫자가 아닌 값 입력 시 처리
                        mList.get(holder.getAdapterPosition()).setRemainingQuantity(BigDecimal.ZERO); // 0으로 설정
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
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