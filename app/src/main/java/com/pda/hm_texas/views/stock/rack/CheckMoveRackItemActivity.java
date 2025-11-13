package com.pda.hm_texas.views.stock.rack;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.sale.SaleOrderItemAdapter;
import com.pda.hm_texas.adapter.stock.LocationSpinnerAdapter;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.LocationDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.event.OnScanListener;
import com.pda.hm_texas.event.ScanReceiver;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckMoveRackItemActivity extends AppCompatActivity implements View.OnClickListener, OnScanListener, View.OnKeyListener, OnItemLongClickListener {

    private Context mContext;
    private IntentFilter filter;
    private ScanReceiver mReciver = null;
    private InputMethodManager imm;
    private Button btnNext;
    private EditText etBarcode;
    private RecyclerView rvItemList;

    private SaleOrderItemAdapter mAdapter;

    private ProgressDialog progressDialog;

    private List<LocationDTO> mLocations = null;
    private Spinner spLoc = null;
    private LocationSpinnerAdapter locAdapter;
    private LocationDTO selectedLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_move_rack_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mContext = this;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mReciver = new ScanReceiver();
        filter = new IntentFilter();
        filter.addAction("device.scanner.EVENT");
        filter.addCategory("android.intent.category.DEFAULT");

        this.registerReceiver(mReciver, filter);
        mReciver.SetOnScanListener(this);

        progressDialog = new ProgressDialog(CheckMoveRackItemActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        etBarcode = findViewById(R.id.etRackItemBarcode);
        etBarcode.setOnKeyListener(this);

        btnNext = findViewById(R.id.btnRackMoveItemSet);
        btnNext.setOnClickListener(this::onClick);

        mAdapter = new SaleOrderItemAdapter();
        mAdapter.SetOnItemLongClickListiner(this);
        rvItemList = findViewById(R.id.rvSerchRackItemList);
        rvItemList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvItemList.setAdapter(mAdapter);

        spLoc = findViewById(R.id.spRackLoc);
        mLocations = new ArrayList<>();
        locAdapter = new LocationSpinnerAdapter(mContext, mLocations);
        spLoc.setAdapter(locAdapter);

        // 스피너 항목 선택 이벤트 처리
        spLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = mLocations.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택 해제 시 처리 (필요 시 구현)
            }
        });

        getLcoation();
        //findViewById(R.id.textView6).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mReciver);
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
        if(view.getId() == R.id.btnRackMoveItemSet){
            if(mAdapter.mList.isEmpty()){
                Utility.getInstance().showDialogWithBlinkingEffect("MOVEITEM", "No Choose in Stock.", mContext);
                return;
            }

            Intent i = new Intent(this, MoveRackActivity.class);
            i.putExtra("LOC", selectedLocation.getCode());
            i.putParcelableArrayListExtra("MOVEITEM", (ArrayList<? extends Parcelable>) mAdapter.mList);
            startActivity(i);
            finish();
        }
        else {
            OnScan("HMP20250506-00001");
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        // 1. 엔터 키 (KEYCODE_ENTER)를 확인합니다.
        if (i == KeyEvent.KEYCODE_ENTER) {

            // 2. 키에서 손을 떼는 이벤트(ACTION_UP)인지 확인합니다.
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {

                if (view.getId() == R.id.etRackItemBarcode) {
                    // etBarcode는 View 내부에서 접근 가능한 변수라고 가정합니다.
                    SearchItem(etBarcode.getText().toString().trim());
                    imm.hideSoftInputFromWindow(etBarcode.getWindowToken(), 0);
                }

                // 이벤트를 소비했으므로 true를 반환합니다.
                return true;
            }
        }

        return false;
    }

    @Override
    public void OnScan(String ScanData) {
        etBarcode.setText(ScanData);
        SearchItem(ScanData);
    }

    @Override
    public void onItemLongClick(int position) {
        try{
            mAdapter.mList.remove(position);
            mAdapter.notifyDataSetChanged();
        }
        catch (Exception ex){
            Utility.getInstance().showDialogWithBlinkingEffect("Search Stock", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

    private void getLcoation(){
        progressDialog.show();

        try {
            Call<List<LocationDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getLocation();
            data.enqueue(new Callback<List<LocationDTO>>() {
                @Override
                public void onResponse(Call<List<LocationDTO>> call, Response<List<LocationDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null ) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Stock", "No Has in Location.", mContext);
                    } else {

                        mLocations.clear();

                        mLocations.addAll(response.body());
                        locAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<LocationDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Stock", "Fail to GetData Server.", mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("Search Stock", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

    private void SearchItem(String barcode){
        progressDialog.show();

        try {
            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getBarcode("", barcode, selectedLocation.getCode());
            data.enqueue(new Callback<List<StockItemDTO>>() {
                @Override
                public void onResponse(Call<List<StockItemDTO>> call, Response<List<StockItemDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    //mAdapter.mList.clear();
                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Scan Lot", "No Has in Stock.", mContext);
                    } else {
                        //mAdapter.mList.clear();
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                        etBarcode.setText("");
                    }
                }

                @Override
                public void onFailure(Call<List<StockItemDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Scan Lot", "Fail to GetData Server.", mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("Search Scan Lot", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }


}