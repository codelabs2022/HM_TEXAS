package com.pda.hm_texas.adapter.prod;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.helper.Utility;
//import com.pda.hm_texas.event.OnItemLongClickListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdItemAdapter extends RecyclerView.Adapter<ProdItemViewHolder>{
    public List<StockItemDTO> mList = null;
    private Context mContext;
    private InputMethodManager imm;
//
    private OnItemLongClickListener longClickListener;
    public void SetOnItemLongClickListiner(OnItemLongClickListener callback)
    {
        this.longClickListener = callback;
    }

    private OnItemClickLintner qtyChangeListner;
    public void SetOnQtyChangeListener(OnItemClickLintner callback){
        this.qtyChangeListner = callback;
    }

    public ProdItemAdapter(Context _context){
        mList = new ArrayList<>();
        mList.clear();
        mContext = _context;
        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
    }
    @NonNull
    @Override
    public ProdItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_stockitem, parent, false);

        StockTextWatcher watcher = new StockTextWatcher(view.findViewById(R.id.etProdQty));

        ProdItemViewHolder holder = new ProdItemViewHolder(view, watcher);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
            holder.tvMfc.setText(item.getManufacturingDate());
            holder.tvExp.setText(item.getExpirationDate());
            holder.tvUnit.setText(item.getUnitofMeasureCode());
            holder.textWatcher.updateData(position, item, mList, qtyChangeListner);
            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());


//            holder.tvRemainQty.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//
//                    if(i == KeyEvent.KEYCODE_ENTER)
//                    {
//                        if (TextUtils.isEmpty(holder.tvRemainQty.getText())) {
////                            holder.tvRemainQty.setText("0");
////                            item.setRemainingQuantity(BigDecimal.ZERO);
//                            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                            mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//                            //mList.get(holder.getAdapterPosition()).setRemainingQuantity(BigDecimal.ZERO); // 0으로 설정
//                        }
//                        try {
//                            // BigDecimal 생성
//                            BigDecimal value = new BigDecimal(holder.tvRemainQty.getText().toString());
//
//                            // 0보다 큰지 확인
//                            if (value.floatValue() >= 0) {
//                                if(item.getRemainingQuantity().floatValue() < value.floatValue()){
//                                    holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                                    //mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//                                }
//                                else if(value.floatValue() == 0){
//                                    holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                                }
//                                else{
//                                    //holder.tvRemainQty.setText(value);
//                                    mList.get(holder.getAdapterPosition()).setRemainingQuantity(value); // 값 반영
//                                }
//                            }
//                            else {
//                                // 음수 입력 시 처리 (옵션)
//                                //item.setRemainingQuantity(BigDecimal.ZERO);
//                                //mList.get(holder.getAdapterPosition()).setRemainingQuantity(BigDecimal.ZERO); // 0으로 설정
//                                holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                                mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//                            }
//                        } catch (NumberFormatException e) {
//                            // 숫자가 아닌 값 입력 시 처리
//                            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                            mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//                        }
//                        if (qtyChangeListner != null) {
//                            qtyChangeListner.onItemSelect(holder.tvRemainQty, position);
//                        }
//                        imm.hideSoftInputFromWindow(holder.tvRemainQty.getWindowToken(), 0);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            holder.tvRemainQty.addTextChangedListener(new TextWatcher() {
//                // 텍스트가 변경되기 직전
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    // 특별한 처리가 없으면 비워둡니다.
//                }
//
//                // 텍스트가 변경되는 동안
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    // 특별한 처리가 없으면 비워둡니다.
//                }
//
//                // 텍스트 변경 후 (여기에 OnKeyListener의 핵심 로직을 적용합니다)
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                    // 1. 빈 값 처리: 빈 값이면 초기 재고 수량으로 복원 (Enter 로직에서 제외된 부분)
//                    if (TextUtils.isEmpty(editable)) {
//                        // 빈 문자열이 입력되면 초기 수량으로 복원하거나 0으로 설정하는 것이 일반적이지만,
//                        // onKey 로직처럼 빈 입력 후 엔터 시 값을 복원하는 로직을 따릅니다.
//                        // *주의: onKey 로직은 빈 값일 때 바로 초기화했지만, 실시간 TextWatcher에서는
//                        // 사용자가 입력 중일 때(예: 기존 값 지우기)도 발동하므로,
//                        // 여기서는 유효성 검사를 통과하지 못했을 때만 복원하는 것이 자연스럽습니다.
//                        return;
//                    }
//
//                    try {
//                        // BigDecimal 생성
//                        BigDecimal value = new BigDecimal(editable.toString());
//
//                        // 2. 0보다 크거나 같은지 확인
//                        if (value.floatValue() >= 0) {
//
//                            // 3. 재고 수량(item.getRemainingQuantity())보다 큰지 확인
//                            if (item.getRemainingQuantity().floatValue() < value.floatValue()) {
//
//                                // 입력값이 재고보다 크면, 재고 수량으로 텍스트를 되돌리고 목록에 재고 수량 반영
//                                holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                                // 커서 위치를 맨 끝으로 이동 (사용자 경험 개선)
//                                holder.tvRemainQty.setSelection(holder.tvRemainQty.getText().length());
//
//                                // TextWatcher 내부에서 setText()를 호출하면 무한 루프가 발생할 수 있으므로,
//                                // 리스너를 잠시 해제하는 것이 안전하지만, 여기서는 복잡성 때문에 생략하고
//                                // 재귀 방지 로직이 없다고 가정하고 진행합니다.
//
//                                mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//
//                            } else if (value.floatValue() == 0) {
//                                // 0인 경우에도 원본 OnKeyListener 로직을 따라 복원
//                                holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                                holder.tvRemainQty.setSelection(holder.tvRemainQty.getText().length());
//                                mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//                            } else {
//                                // 유효한 값일 때만 목록에 반영
//                                mList.get(holder.getAdapterPosition()).setRemainingQuantity(value); // 값 반영
//                            }
//
//                        } else {
//                            // 4. 음수 입력 시 처리: 초기 재고 수량으로 복원
//                            holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                            holder.tvRemainQty.setSelection(holder.tvRemainQty.getText().length());
//                            mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//                        }
//
//                        // 5. 콜백 호출 (값이 변경되었으므로 호출)
//                        if (qtyChangeListner != null) {
//                            qtyChangeListner.onItemSelect(holder.tvRemainQty, holder.getAdapterPosition());
//                        }
//
//                    } catch (NumberFormatException e) {
//                        // 6. 숫자가 아닌 값 입력 시 처리: 초기 재고 수량으로 복원
//                        holder.tvRemainQty.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
//                        holder.tvRemainQty.setSelection(holder.tvRemainQty.getText().length());
//                        mList.get(holder.getAdapterPosition()).setRemainingQuantity(item.getRemainingQuantity());
//                    }
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

    public class StockTextWatcher implements TextWatcher {
        private final EditText editText;
        private int position;
        private StockItemDTO item;
        private List<StockItemDTO> mList;
        private OnItemClickLintner qtyChangeListner;

        public StockTextWatcher(EditText editText) {
            this.editText = editText;
        }
        public void updateData(int position, StockItemDTO item, List<StockItemDTO> list, OnItemClickLintner listener) {
            this.position = position;
            this.item = item;
            this.mList = list;
            this.qtyChangeListner = listener;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable editable) {

            if (item == null || position == -1 || mList == null) return;

            try {
                BigDecimal value = new BigDecimal(editable.toString());

                if (item.getRemainingQuantity().floatValue() < value.floatValue()) {
                    // 재고보다 크면, 원래 값으로 복원
                    this.editText.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
                    this.editText.setSelection(this.editText.getText().length());
                    mList.get(this.position).setRemainingQuantity(item.getRemainingQuantity());
                }
                else if(value.floatValue() == 0){
                    this.editText.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
                    this.editText.setSelection(this.editText.getText().length());
                    mList.get(this.position).setRemainingQuantity(item.getRemainingQuantity());
                }
                else {
                    // 유효한 값일 때만 목록에 반영
                    mList.get(this.position).setRemainingQuantity(value);
                }
                // ... (나머지 유효성 검사 로직) ...

            } catch (NumberFormatException e) {
                // ... (숫자가 아닌 값 처리 로직) ...
                this.editText.setText(item.getRemainingQuantity().stripTrailingZeros().toPlainString());
                this.editText.setSelection(this.editText.getText().length());
                mList.get(position).setRemainingQuantity(item.getRemainingQuantity());
            }

            // 💡 콜백 호출 시에도 현재 position 사용
            if (qtyChangeListner != null) {
                qtyChangeListner.onItemSelect(this.editText, this.position);
            }
        }
    }
}
