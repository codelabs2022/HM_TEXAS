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
import com.pda.hm_texas.adapter.prod.PlcAdapter;
import com.pda.hm_texas.adapter.prod.RecipeAdapter;
import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.ProdCompsDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlcDialog extends Dialog implements OnItemClickLintner, View.OnClickListener {

    private PlcAdapter mAdapter;
    private ProgressDialog progressDialog;
    private Context mContext;

    public PlcDialog(@NonNull Context context, String item) {
        super(context);
        setContentView(R.layout.dig_plc);

        mContext = context;

        progressDialog = new ProgressDialog(mContext, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mAdapter = new PlcAdapter();
        mAdapter.SetOnClickListiner(this);
        RecyclerView rvList = findViewById(R.id.rvPlc);
        rvList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        rvList.setAdapter(mAdapter);

        Button btnSelect = findViewById(R.id.btnSetPlc);

        LoadData(item);
    }

    @Override
    public void onItemSelect(View v, int position) {
        try{
            ProdHelper.getInstance().setProdPlc(mAdapter.SelectItem);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSetRecipe){
            try{
                ProdHelper.getInstance().setProdPlc(mAdapter.SelectItem);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void LoadData(String itemNo){
        try {
            Call<List<PlcMatrailDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getPlcInfo(itemNo);
            data.enqueue(new Callback<List<PlcMatrailDTO>>() {
                @Override
                public void onResponse(Call<List<PlcMatrailDTO>> call, Response<List<PlcMatrailDTO>> response) {
                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Plc", "No Has Plc.", mContext);
                    } else {
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<PlcMatrailDTO>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {

            Utility.getInstance().showDialog("Search Plc", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }
}
