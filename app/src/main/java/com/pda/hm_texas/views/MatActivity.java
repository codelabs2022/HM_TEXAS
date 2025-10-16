package com.pda.hm_texas.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.FactoryAdaper;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.FactoryDTO;
import com.pda.hm_texas.event.OnButtonClickListener;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;
import com.pda.hm_texas.views.mat.ProdActivity;
import com.pda.hm_texas.views.mat.ProdOrderActivity;
import com.pda.hm_texas.views.mat.rout.Dispersantfragment;
import com.pda.hm_texas.views.mat.rout.InsulationFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener , OnItemClickLintner {//,OnButtonClickListener

    //private Fragment insulationFragment, dispersantfragment;
    private Context mContext;
    private RecyclerView rvFactory;
    private FactoryAdaper mAdapter;
    private ProgressDialog progressDialog;

    private  String fcode = "F001";
    private List<FactoryDTO> mFactoryList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mContext = this;
        mFactoryList = new ArrayList<>();
        progressDialog = new ProgressDialog(MatActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

//        insulationFragment = new InsulationFragment();
//        dispersantfragment = new Dispersantfragment();

        //getSupportFragmentManager().beginTransaction().add(R.id.frame, insulationFragment).commit();
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(this);

        rvFactory = findViewById(R.id.rvFactory);
        mAdapter = new FactoryAdaper();
        mAdapter.SetOnClickListiner(this);
        rvFactory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvFactory.setAdapter(mAdapter);

        loadFactory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

        // super.onBackPressed();
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();

//        Fragment selected = null;
//        if(position == 0){
//            selected = insulationFragment;
//        }else if (position == 1){
//            selected = dispersantfragment;
//        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();

        if(position == 0){
            fcode = "F001";
        }
        else{
            fcode = "F002";
        }
        setLoadFactory();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

//    @Override
//    public void onButtonClicked(String Code) {
//        ProdHelper.getInstance().setRout(Code);
//        Intent i = new Intent(this, ProdOrderActivity.class);
//        startActivity(i);
//        finish();
//    }

    @Override
    public void onItemSelect(View v, int position) {
        String Code = mAdapter.mList.get(position).getRoutNo();
        ProdHelper.getInstance().setRout(Code);
        Intent i = new Intent(this, ProdOrderActivity.class);
        startActivity(i);
        finish();
    }

    private void setLoadFactory(){
        mAdapter.mList.clear();

        for(FactoryDTO dto : mFactoryList){
            if(dto.getFcode().equals(fcode)){
                mAdapter.mList.add(dto);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void loadFactory(){
        progressDialog.show();

        try{

            Call<List<FactoryDTO>> call = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getFactory("");
            call.enqueue(new Callback<List<FactoryDTO>>() {
                @Override
                public void onResponse(Call<List<FactoryDTO>> call, Response<List<FactoryDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if(response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Factory", "No Has Factory.", mContext);
                    }
                    else{
                        mFactoryList.addAll(response.body());
                        setLoadFactory();
                    }
                }

                @Override
                public void onFailure(Call<List<FactoryDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialog("Search Factory", t.getMessage(), mContext);
                }
            });

        } catch (Exception e) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Factory", e.getMessage(), mContext);
            e.printStackTrace();
        }
    }
}