package com.pda.hm_texas.dig;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.ProdReleaseItemAdpater;
import com.pda.hm_texas.adapter.prod.RecipeAdapter;
import com.pda.hm_texas.adapter.sale.SaleOrderItemAdapter;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDialog extends Dialog implements OnItemClickLintner, View.OnClickListener{

    private Context mContext;
    private ProgressDialog progressDialog;
    private RecyclerView rvItemList;

    public ProdReleaseItemAdpater mAdapter;

    public ItemDialog(@NonNull Context context, String itemNo, String loc) {
        super(context);
        setContentView(R.layout.dig_item);

        mContext = context;

        progressDialog = new ProgressDialog(mContext, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        Button btnSelect = findViewById(R.id.btnSetProdStock);
        btnSelect.setOnClickListener(this::onClick);

        Button btnCancle = findViewById(R.id.btnCancleProdStock);
        btnCancle.setOnClickListener(this::onClick);


        mAdapter = new ProdReleaseItemAdpater();
        rvItemList = findViewById(R.id.rvProdStock);
        rvItemList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvItemList.setAdapter(mAdapter);

        LoadStockData(itemNo, loc);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSetProdStock)
        {

        }
        else if(view.getId() == R.id.btnCancleProdStock){

        }
    }

    @Override
    public void onItemSelect(View v, int position) {

    }

    private void LoadStockData(String itemNo, String loc){
        progressDialog.show();
        try{

            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getReleaseStockItems(itemNo, loc);
            data.enqueue(new Callback<List<StockItemDTO>>() {
                @Override
                public void onResponse(Call<List<StockItemDTO>> call, Response<List<StockItemDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    mAdapter.mList.clear();
                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Stock Release Item", "No Has in Stock.", mContext);
                    } else {
                        mAdapter.mList.clear();
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<StockItemDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Stock Release Item", "Fail to GetData Server.", mContext);
                }
            });

        }catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Stock Release Item", ex.getMessage(), mContext);
            ex.printStackTrace();
        }

    }
}
