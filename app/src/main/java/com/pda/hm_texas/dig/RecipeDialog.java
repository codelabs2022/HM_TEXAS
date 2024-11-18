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
import com.pda.hm_texas.adapter.prod.RecipeAdapter;
import com.pda.hm_texas.dto.ProdCompsDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDialog extends Dialog implements OnItemClickLintner, View.OnClickListener {

    private RecipeAdapter mAdapter;
    private ProgressDialog progressDialog;
    private Context mContext;
    public ProdCompsDTO SelectItem;

    public RecipeDialog(@NonNull Context context, String ordNo, int lineNo) {
        super(context);
        setContentView(R.layout.dig_recipe);

        mContext = context;

        progressDialog = new ProgressDialog(mContext, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mAdapter = new RecipeAdapter();
        mAdapter.SetOnClickListiner(this);
        RecyclerView rvList = findViewById(R.id.rvRecipe);
        rvList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvList.setAdapter(mAdapter);

        Button btnSelect = findViewById(R.id.btnSetRecipe);

        LoadData(ordNo, lineNo);
    }

    @Override
    public void onItemSelect(View v, int position) {
        try{
            ProdHelper.getInstance().setProdComps(mAdapter.SelectItem);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSetRecipe){
            try{
                ProdHelper.getInstance().setProdComps(mAdapter.SelectItem);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void LoadData(String ordNo, int lineNo){
        try {
            Call<List<ProdCompsDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getPdaProdRecipe(ordNo, lineNo);
            data.enqueue(new Callback<List<ProdCompsDTO>>() {
                @Override
                public void onResponse(Call<List<ProdCompsDTO>> call, Response<List<ProdCompsDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Recipe", "No Has Recipe.", mContext);
                    } else {
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<ProdCompsDTO>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Order", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }
}
