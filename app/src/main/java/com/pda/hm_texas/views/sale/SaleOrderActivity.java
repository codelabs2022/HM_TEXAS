package com.pda.hm_texas.views.sale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.enter.EnterOrderAdapter;
import com.pda.hm_texas.adapter.sale.SaleOrderAdapter;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.EnterOrderDTO;
import com.pda.hm_texas.dto.ProdOrderDTO;
import com.pda.hm_texas.dto.SaleOrderDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.SaleHelper;
import com.pda.hm_texas.helper.Utility;
import com.pda.hm_texas.views.enter.EnterOrderActivity;
import com.pda.hm_texas.views.mat.ProdActivity;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleOrderActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickLintner {

    private Context mContext;
    private TextView tvFrom, tvTo;
    private Button btnSearch, btnNext;
    private RecyclerView rvList;
    private DatePickerDialog datePickerDialog;
    private ProgressDialog progressDialog;
    private SaleOrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sale_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mContext = this;
        tvFrom = findViewById(R.id.tvSaleFrom);
        tvTo = findViewById(R.id.tvSaleTo);

        tvFrom.setOnClickListener(this);
        tvTo.setOnClickListener(this);

        btnSearch = findViewById(R.id.btnSaleSearch);
        btnNext = findViewById(R.id.btnSaleOrderSet);

        btnNext.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        mAdapter = new SaleOrderAdapter();
        mAdapter.SetOnClickListiner(this);
        rvList = findViewById(R.id.rvSaleOrderList);
        rvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvList.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(SaleOrderActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        tvFrom.setText(Utility.getInstance().getMonthOfFirstDay());
        tvTo.setText(Utility.getInstance().getToday());
    }

    @Override
    protected void onDestroy() {
        //ProdHelper.getInstance().setProdOrder(null);
        //SaleHelper.getInstance().setOrder(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();

        super.onBackPressed();

        // super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tvSaleFrom ){
            ShowCalender(tvFrom);
        }
        else if(view.getId() == R.id.tvSaleTo ){
            ShowCalender(tvTo);
        }
        else if(view.getId() == R.id.btnSaleSearch){
            if(TextUtils.isEmpty(tvFrom.getText())){
                Utility.getInstance().showDialog("Set From Date", "Please Enter From Date", this);
                return;
            }

            if(TextUtils.isEmpty(tvTo.getText())){
                Utility.getInstance().showDialog("Set To Date", "Please Enter From Date", this);
                return;
            }
            SearchOrder();
        }
        else if(view.getId() == R.id.btnSaleOrderSet ){
            if(SaleHelper.getInstance().getOrder() == null){
                Utility.getInstance().showDialog("SetOrder", "Please Select Order", this);
            }
            else{
                //ProdHelper.getInstance().setProdOrder(mAdapter.SelectItem);
                Intent i = new Intent(this, SalePickingActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    public void onItemSelect(View v, int position) {
        try{
            SaleOrderDTO order = mAdapter.mList.get(position);
            SaleHelper.getInstance().setOrder(order);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void SearchOrder(){
        SaleHelper.getInstance().setOrder(null);
        progressDialog.show();

        try {
            Call<List<SaleOrderDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getSaleOrder(tvFrom.getText().toString(),tvTo.getText().toString(), "");
            data.enqueue(new Callback<List<SaleOrderDTO>>() {
                @Override
                public void onResponse(Call<List<SaleOrderDTO>> call, Response<List<SaleOrderDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Order", "No Has Order.", mContext);
                    } else {
                        mAdapter.mList.clear();
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<SaleOrderDTO>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Order", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }
    private void ShowCalender(TextView tv){
        Calendar calendar = Calendar.getInstance();
        int pyear = calendar.get(Calendar.YEAR);
        int pmonth = calendar.get(Calendar.MONTH);
        int pday = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(SaleOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 +1;

                String month = i1 < 10 ? "0" + i1 : String.valueOf(i1);
                String day = i2 < 10 ? "0" + i2 : String.valueOf(i2);
                String date = i + "-" + month +"-" + day;

                tv.setText(date);
            }
        }, pyear, pmonth, pday);
        datePickerDialog.show();
    }
}