package com.pda.hm_texas.adapter.prod;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;

public class ProdItemViewHolder extends RecyclerView.ViewHolder{
    public LinearLayout liItem;
    public TextView tvItemNo, tvItemName, tvBarcode, tvLot, tvMfc, tvExp, tvUnit ;
    public final EditText tvRemainQty;
    public final ProdItemAdapter.StockTextWatcher textWatcher; // TextWatcher 인스턴스 참조

    private InputMethodManager imm;

    public ProdItemViewHolder(@NonNull View itemView, ProdItemAdapter.StockTextWatcher watcher) {
        super(itemView);

        liItem = itemView.findViewById(R.id.llProdStockItem);
        tvItemNo = itemView.findViewById(R.id.tvStockItemNo);
        tvItemName = itemView.findViewById(R.id.tvStockItemName);
        tvRemainQty = itemView.findViewById(R.id.etProdQty);
        tvBarcode = itemView.findViewById(R.id.tvStockItemBarcode);
        tvLot = itemView.findViewById(R.id.tvStockItemLotNo);
        tvMfc = itemView.findViewById(R.id.tvStockItemMnf);
        tvExp = itemView.findViewById(R.id.tvStockItemExp);
        tvUnit = itemView.findViewById(R.id.tvStockItemUnit);

        this.textWatcher = watcher;
        tvRemainQty.addTextChangedListener(textWatcher);

        tvRemainQty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                // 이 로직은 onBindViewHolder가 아닌, ViewHolder 내부에서 처리되도록 코드를 옮겨야 합니다.
                // 다만, onKeyListener는 TextWatcher가 처리하지 않는 엔터 키에만 집중하도록 단순화하는 것이 좋습니다.
                if(i == KeyEvent.KEYCODE_ENTER) {
                    // Enter 키 로직 처리 (TextWatcher 로직을 통합하는 것이 더 좋음)
                    // 현재는 복잡하니, TextWatcher만 수정하는 것을 권장합니다.
                    // 임시: 엔터 키를 눌렀을 때만 로직을 실행하도록 onBindViewHolder의 코드를 여기에 복사하세요.

                    // Enter 키가 눌렸을 때만 수량 변경 콜백 호출
                    // 💡 여기서 qtyChangeListner를 사용하려면, 리스너를 ViewHolder로 전달해야 합니다.

                    // (생략) - TextWatcher가 실시간으로 처리하므로 onKey는 Enter 키 동작(키보드 숨김)에 집중합니다.
                    imm.hideSoftInputFromWindow(tvRemainQty.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }
}
