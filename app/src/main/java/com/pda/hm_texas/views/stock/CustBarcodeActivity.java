package com.pda.hm_texas.views.stock;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.sale.SaleOrderAdapter;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.DbResultVO;
import com.pda.hm_texas.dto.TransBarcodeItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.event.OnScanListener;
import com.pda.hm_texas.event.ScanReceiver;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;
import com.pda.hm_texas.adapter.stock.MappingAdapter;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustBarcodeActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, OnScanListener, OnItemLongClickListener, View.OnKeyListener {

    private Context mContext;
    private IntentFilter filter;
    private ScanReceiver mReciver = null;
    private InputMethodManager imm;

    private EditText etBarcode, etCustBarcode;
    private Button btnReg;
    private RecyclerView rvMappingList;

    private ProgressDialog progressDialog;
    private MappingAdapter mAdapter;
    private TransBarcodeItemDTO nowScanItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cust_barcode);
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

        etBarcode = findViewById(R.id.etItemBarcode);
        etCustBarcode = findViewById(R.id.etCustBarcode);
        etBarcode.setLongClickable(true);
        etCustBarcode.setLongClickable(true);
        etBarcode.setOnKeyListener(this);
        etCustBarcode.setOnKeyListener(this);
        etBarcode.setOnLongClickListener(this);
        etCustBarcode.setOnLongClickListener(this);

        btnReg = findViewById(R.id.btnCustBarcodeSet);
        btnReg.setOnClickListener(this::onClick);

        mAdapter = new MappingAdapter();
        mAdapter.SetOnItemLongClickListiner(this);
        rvMappingList = findViewById(R.id.rvMappingList);
        rvMappingList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvMappingList.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(CustBarcodeActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
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
        if(view.getId() == R.id.btnCustBarcodeSet){
            SetCustBarcode();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(view.getId() == R.id.etItemBarcode){
            etBarcode.setText("");
            return true;
        }
        else if(view.getId() == R.id.etCustBarcode){
            etCustBarcode.setText("");
            return true;
        }
        return false;
    }

    @Override
    public void onItemLongClick(int position) {
        try{
            mAdapter.mList.remove(position);
            mAdapter.notifyDataSetChanged();
        }
        catch (Exception ex){

        }
    }

    @Override
    public void OnScan(String ScanData) {
        try{
            String input = etBarcode.getText().toString().trim(); // trim()으로 앞뒤 공백 제거

            if (input.isEmpty()) {
                // 값이 없거나 공백인 경우
                getBarcodeinfo(ScanData);
                //etBarcode.setText(ScanData);
            } else {
                // 값이 존재하는 경우
                etCustBarcode.setText(ScanData);
                CheckCustBarcode(ScanData);
            }
        }
        catch (Exception ex){
            Utility.getInstance().showDialogWithBlinkingEffect("Barcode", "Fail to Scan Barcode", mContext);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == KeyEvent.KEYCODE_ENTER)
        {
            if(view.getId() == R.id.etItemBarcode){
                etBarcode.requestFocus();
            }
            else if(view.getId() == R.id.etCustBarcode){
                imm.hideSoftInputFromWindow(etCustBarcode.getWindowToken(), 0);
            }

            return true;
        }

        return false;
    }

    private void getBarcodeinfo(String barcode){
        progressDialog.show();

        try {
            OptionalInt index = IntStream.range(0, mAdapter.mList.size())
                    .filter(i -> mAdapter.mList.get(i).getBarcode().equals(barcode))
                    .findFirst();

            if (index.isPresent()) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                Utility.getInstance().showDialogWithBlinkingEffect("Mapping", "This is a barcode that has already been used.", mContext);
            } else {
                Call<List<TransBarcodeItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getCustStockItemInfo(barcode);
                data.enqueue(new Callback<List<TransBarcodeItemDTO>>() {
                    @Override
                    public void onResponse(Call<List<TransBarcodeItemDTO>> call, Response<List<TransBarcodeItemDTO>> response) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();

                        if (response.body() == null || response.body().size() == 0) {
                            Utility.getInstance().showDialogWithBlinkingEffect("Search Lot", "No Has in Stock.", mContext);
                        } else {
                            if(response.body().size() > 1)
                            {
                                Utility.getInstance().showDialogWithBlinkingEffect("Search Lot", "One or more inventory information was found.", mContext);
                                etBarcode.setText("");
                            }
                            else
                            {
                                nowScanItem = response.body().get(0);
                                etBarcode.setText(barcode);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TransBarcodeItemDTO>> call, Throwable t) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Lot", "Fail to GetData Server.", mContext);
                        etBarcode.setText("");
                    }
                });
            }
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("Search Lot", ex.getMessage(), mContext);
            ex.printStackTrace();
            etBarcode.setText("");
        }
    }

    private void CheckCustBarcode(String custBarcode){
        progressDialog.show();

        try {
            OptionalInt index = IntStream.range(0, mAdapter.mList.size())
                    .filter(i -> mAdapter.mList.get(i).getCustLotNo().equals(custBarcode))
                    .findFirst();

            if (index.isPresent()) {
                Utility.getInstance().showDialogWithBlinkingEffect("Mapping", "This is a customer barcode that has already been used.", mContext);
                etCustBarcode.setText("");
            } else {
                if(nowScanItem != null){
                    nowScanItem.setCustLotNo(custBarcode);
                    mAdapter.mList.add(nowScanItem);
                    mAdapter.notifyDataSetChanged();

                    etBarcode.setText("");
                    etCustBarcode.setText("");
                    nowScanItem = null;
                }
            }

            if (progressDialog.isShowing()) progressDialog.dismiss();
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("Search Lot", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

    private void SetCustBarcode(){
        if(mAdapter.mList.size() > 0){
            progressDialog.show();

            try {
                Call<DbResultVO> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).setCustBarcode(mAdapter.mList);
                data.enqueue(new Callback<DbResultVO>() {
                    @Override
                    public void onResponse(Call<DbResultVO> call, Response<DbResultVO> response) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();

                        if (response.body() == null ) {
                            Utility.getInstance().showDialogWithBlinkingEffect("Mapping", "No Has in Stock.", mContext);
                        } else {
                            if(response.body().getERR_CODE().equals("S00"))
                            {
                                Utility.getInstance().showDialog("Mapping", "Success.", mContext);
                                etBarcode.setText("");
                                etCustBarcode.setText("");
                                nowScanItem = null;

                                mAdapter.mList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Utility.getInstance().showDialogWithBlinkingEffect("Mapping", "Customer barcode mapping failed.", mContext);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DbResultVO> call, Throwable t) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Utility.getInstance().showDialogWithBlinkingEffect("Mapping", "Fail to GetData Server.", mContext);
                    }
                });
            } catch (Exception ex) {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                Utility.getInstance().showDialogWithBlinkingEffect("Mapping", ex.getMessage(), mContext);
                ex.printStackTrace();
            }
        }
    }
}