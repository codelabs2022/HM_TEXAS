package com.pda.hm_texas.views.mat;

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

import com.pda.hm_texas.adapter.prod.OrderAdapter;
import com.pda.hm_texas.dig.ProgressDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.dto.ProdOrderDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdOrderActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickLintner {

    private Context mContext;
    private TextView tvFrom, tvTo;
    private Button btnSearch, btnNext;
    private RecyclerView rvList;
    private DatePickerDialog datePickerDialog;
    private ProgressDialog progressDialog;
    private OrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prod_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mContext = this;
        tvFrom = findViewById(R.id.tvMatFrom);
        tvTo = findViewById(R.id.tvMatTo);

        tvFrom.setOnClickListener(this);
        tvTo.setOnClickListener(this);

        btnSearch = findViewById(R.id.btnProdSearch);
        btnNext = findViewById(R.id.btnMatOrderSet);

        btnNext.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        mAdapter = new OrderAdapter();
        mAdapter.SetOnClickListiner(this);
        rvList = findViewById(R.id.rvProdOrderList);
        rvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvList.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(ProdOrderActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        tvFrom.setText(Utility.getInstance().getMonthOfFirstDay());
        tvTo.setText(Utility.getInstance().getToday());
    }
    @Override
    protected void onDestroy() {
        //ProdHelper.getInstance().setProdOrder(null);
        Utility.getInstance().stopBlinkingAnimation();
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
        if(view.getId() == R.id.tvMatFrom ){
            ShowCalender(tvFrom);
        }
        else if(view.getId() == R.id.tvMatTo ){
            ShowCalender(tvTo);
        }
        else if(view.getId() == R.id.btnProdSearch){
            if(TextUtils.isEmpty(tvFrom.getText())){
                Utility.getInstance().showDialogWithBlinkingEffect("Set From Date", "Please Enter From Date", this);
                return;
            }

            if(TextUtils.isEmpty(tvTo.getText())){
                Utility.getInstance().showDialogWithBlinkingEffect("Set To Date", "Please Enter From Date", this);
                return;
            }
            SearchOrder();
        }
        else if(view.getId() == R.id.btnMatOrderSet ){
            if(ProdHelper.getInstance().getProdOrder() == null){
                Utility.getInstance().showDialogWithBlinkingEffect("SetOrder", "Please Select Order", this);
            }
            else{
                //ProdHelper.getInstance().setProdOrder(mAdapter.SelectItem);
                Intent i = new Intent(this, ProdActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    public void onItemSelect(View v, int position) {
        try{
            ProdOrderDTO order = mAdapter.mList.get(position);
            ProdHelper.getInstance().setProdOrder(order);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void SearchOrder(){
        progressDialog.show();

        try {
            Call<List<ProdOrderDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getProdOrder(tvFrom.getText().toString(),tvTo.getText().toString(), ProdHelper.getInstance().getRout());
            data.enqueue(new Callback<List<ProdOrderDTO>>() {
                @Override
                public void onResponse(Call<List<ProdOrderDTO>> call, Response<List<ProdOrderDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Order", "No Has Order.", mContext);
                    } else {
                        mAdapter.mList.clear();
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<ProdOrderDTO>> call, Throwable t) {
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Order", "Fail to GetData Server.", mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("Search Order", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }
    private void ShowCalender(TextView tv){
        Calendar calendar = Calendar.getInstance();
        int pyear = calendar.get(Calendar.YEAR);
        int pmonth = calendar.get(Calendar.MONTH);
        int pday = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(ProdOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
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