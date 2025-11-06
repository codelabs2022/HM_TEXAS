package com.pda.hm_texas.views.stock.rack;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.pda.hm_texas.adapter.stock.RackAdapter;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.DbResultVO;
import com.pda.hm_texas.dto.LocationDTO;
import com.pda.hm_texas.dto.RackDTO;
import com.pda.hm_texas.dto.RackMoveDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnScanListener;
import com.pda.hm_texas.event.ScanReceiver;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;
import com.pda.hm_texas.views.stock.ItemInfoActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoveRackActivity extends AppCompatActivity implements View.OnClickListener, OnScanListener, View.OnKeyListener{

    private Context mContext;
    private IntentFilter filter;
    private ScanReceiver mReciver = null;
    private InputMethodManager imm;
    private Button btnNext;
    private EditText etBarcode;
    private RecyclerView rvRackList;
    private RecyclerView rvItemList;

    private SaleOrderItemAdapter mMoveItemAdapter;
    private RackAdapter mRackListAdapter;

    private ProgressDialog progressDialog;

    private String locationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_move_rack);
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

        progressDialog = new ProgressDialog(MoveRackActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        etBarcode = findViewById(R.id.etRackBarcode);
        etBarcode.setOnKeyListener(this);

        btnNext = findViewById(R.id.btnRackMoveReg);
        btnNext.setOnClickListener(this::onClick);

        mRackListAdapter = new RackAdapter();
        rvRackList = findViewById(R.id.rvRackInfo);
        rvRackList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvRackList.setAdapter(mRackListAdapter);

        mMoveItemAdapter = new SaleOrderItemAdapter();
        rvItemList = findViewById(R.id.rvRackMoveItem);
        rvItemList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvItemList.setAdapter(mMoveItemAdapter);


        TextView tvRackTitle = findViewById(R.id.tvRackTitle);

        Intent intent = getIntent();

        locationCode = intent.getStringExtra("LOC");
        ArrayList<StockItemDTO> receivedCustomList =
                intent.getParcelableArrayListExtra("MOVEITEM");

        if (receivedCustomList != null) {
            mMoveItemAdapter.mList.clear();
            mMoveItemAdapter.mList.addAll(receivedCustomList);
            mMoveItemAdapter.notifyDataSetChanged();
        }
        else{
            finish();
        }

        if(TextUtils.isEmpty(locationCode)){
            finish();
        }

        tvRackTitle.setText(tvRackTitle.getText() + "-" + locationCode );
        getRackList(locationCode);
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
        if(view.getId() == R.id.btnRackMoveReg)
        {
            SetRack();
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == KeyEvent.KEYCODE_ENTER)
        {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                if(view.getId() == R.id.etRackBarcode){
                    SearchRack(etBarcode.getText().toString().trim());
                    imm.hideSoftInputFromWindow(etBarcode.getWindowToken(), 0);
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public void OnScan(String ScanData) {
        SearchRack(ScanData);
    }


    private void getRackList(String locationCode){
        progressDialog.show();

        try{
            Call<List<RackDTO>> call = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getRackList(locationCode);
            call.enqueue(new Callback<List<RackDTO>>() {
                @Override
                public void onResponse(Call<List<RackDTO>> call, Response<List<RackDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Rack", "No Has in Rack.", mContext);
                    }
                    else {
                        mRackListAdapter.mList.clear();
                        mRackListAdapter.mList.addAll(response.body());
                        mRackListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<RackDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Rack", "Fail to GetData Server.", mContext);
                }
            });
        }
        catch (Exception ex){
            if(progressDialog.isShowing()) progressDialog.dismiss();
            ex.printStackTrace();
        }

    }
    private void SearchRack(String rackCode){
        progressDialog.show();

        try{
            Call<RackDTO> call = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getRack(locationCode, rackCode);
            call.enqueue(new Callback<RackDTO>() {
                @Override
                public void onResponse(Call<RackDTO> call, Response<RackDTO> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().toString().equals("")) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Rack", "No Has in Rack.", mContext);
                    }
                    else {
                        for (RackDTO rack : mRackListAdapter.mList) {

                            if(rack.getRackCode().equals(response.body().getRackCode())){
                                rack.setSelect(true);
                                mRackListAdapter.setRack(rack);
                                mRackListAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }

                    etBarcode.setText("");
                }

                @Override
                public void onFailure(Call<RackDTO> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Rack", "Fail to GetData Server.", mContext);
                }
            });
        }
        catch (Exception ex){
            if(progressDialog.isShowing()) progressDialog.dismiss();
            ex.printStackTrace();
        }
    }

    private void SetRack() {

        if(mRackListAdapter.getSetRack() == null){
            Utility.getInstance().showDialogWithBlinkingEffect("RackMove", "Please Set Rack.", mContext);
            return;
        }
        if(mRackListAdapter.mList.isEmpty()){
            Utility.getInstance().showDialogWithBlinkingEffect("RackMove", "There is no stock to move.", mContext);
            return;
        }

        progressDialog.show();

        String toRack = mRackListAdapter.getSetRack().getRackCode();
        List<RackMoveDTO> sendItems = new ArrayList<>();

        for (StockItemDTO readyItem : mMoveItemAdapter.mList) {
            RackMoveDTO moveItem = new RackMoveDTO();

            moveItem.setItemNo(readyItem.getItemNo());
            moveItem.setLotNo(readyItem.getLotNo());
            moveItem.setBarcode(readyItem.getBarCode());
            moveItem.setLocationCode(readyItem.getLocationCode());
            moveItem.setFromRackCode(readyItem.getRackCode());
            moveItem.setToRackCode(toRack);

            sendItems.add(moveItem);
        }

        Call<DbResultVO> call = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).RackMove(sendItems);
        call.enqueue(new Callback<DbResultVO>() {
            @Override
            public void onResponse(Call<DbResultVO> call, Response<DbResultVO> response) {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                if (response.body() == null) {
                    Utility.getInstance().showDialogWithBlinkingEffect("Move Item", "No processing result has been received.", mContext);
                } else {
                    if(response.body().getERR_CODE().equals("0"))
                    {
                        mMoveItemAdapter.mList.clear();
                        mMoveItemAdapter.notifyDataSetChanged();
                        etBarcode.setText("");
                        Utility.getInstance().showDialog("Move Item", response.body().getERR_MSG(), mContext);
                    }
                    else{
                        Utility.getInstance().showDialogWithBlinkingEffect("Move Item", response.body().getERR_MSG(), mContext);
                    }
                }
            }

            @Override
            public void onFailure(Call<DbResultVO> call, Throwable t) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                Utility.getInstance().showDialogWithBlinkingEffect("Move Item", t.getMessage(), mContext);
            }
        });

    }
}