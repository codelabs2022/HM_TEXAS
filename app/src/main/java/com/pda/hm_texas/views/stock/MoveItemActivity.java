package com.pda.hm_texas.views.stock;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.pda.hm_texas.dto.DbResultVO;
import com.pda.hm_texas.dto.LocationDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.event.OnScanListener;
import com.pda.hm_texas.event.ScanReceiver;
import com.pda.hm_texas.helper.LoginUser;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoveItemActivity extends AppCompatActivity implements View.OnClickListener, OnScanListener, View.OnKeyListener, OnItemLongClickListener {

    private Context mContext;
    private IntentFilter filter;
    private ScanReceiver mReciver = null;
    private InputMethodManager imm;

    private EditText etBarcode;
    private Button btnMove;
    private RecyclerView rvItemList;

    private SaleOrderItemAdapter mAdapter;
    private ProgressDialog progressDialog;

    private List<LocationDTO> mLocations = null;
    private Spinner spLocFrom = null;
    private Spinner spLocTo = null;
    private LocationSpinnerAdapter locFromAdapter;
    private LocationSpinnerAdapter locToAdapter;
    private LocationDTO selectedFromLocation;
    private LocationDTO selectedToLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_move_item);
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

        progressDialog = new ProgressDialog(MoveItemActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        etBarcode = findViewById(R.id.etMoveBarcode);
        etBarcode.setOnKeyListener(this);

        btnMove = findViewById(R.id.btnMove);
        btnMove.setOnClickListener(this);

        mAdapter = new SaleOrderItemAdapter();
        mAdapter.SetOnItemLongClickListiner(this);
        rvItemList = findViewById(R.id.rvMoveItemList);
        rvItemList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvItemList.setAdapter(mAdapter);

        mLocations = new ArrayList<>();

        spLocFrom = findViewById(R.id.spFromLoc);
        locFromAdapter = new LocationSpinnerAdapter(mContext, mLocations);
        spLocFrom.setAdapter(locFromAdapter);

        // 스피너 항목 선택 이벤트 처리
        spLocFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFromLocation = mLocations.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택 해제 시 처리 (필요 시 구현)
            }
        });

        spLocTo = findViewById(R.id.spToLoc);
        locToAdapter = new LocationSpinnerAdapter(mContext, mLocations);
        spLocTo.setAdapter(locFromAdapter);

        // 스피너 항목 선택 이벤트 처리
        spLocTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedToLocation = mLocations.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택 해제 시 처리 (필요 시 구현)
            }
        });

        getLcoation();
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mReciver);
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
        if(view.getId() == R.id.btnMove){
            setMove();
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(view.getId() == R.id.etMoveBarcode){
            if((i == KeyEvent.KEYCODE_ENTER) && (keyEvent.getAction() ==  KeyEvent.ACTION_DOWN))
            {
                SearchItem(etBarcode.getText().toString());
                return true;
            }
        }
        return false;
    }

    @Override
    public void OnScan(String ScanData) {

        SearchItem(ScanData);
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

    private void getLcoation(){
        progressDialog.show();

        try {
            Call<List<LocationDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getLocation();
            data.enqueue(new Callback<List<LocationDTO>>() {
                @Override
                public void onResponse(Call<List<LocationDTO>> call, Response<List<LocationDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null ) {
                        Utility.getInstance().showDialog("Search Stock", "No Has in Location.", mContext);
                    } else {

                        mLocations.clear();

                        mLocations.addAll(response.body());
                        locFromAdapter.notifyDataSetChanged();
                        locToAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<LocationDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialog("Search Stock", "Fail to GetData Server.", mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Stock", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }
    private void SearchItem(String barcode){
        if(selectedFromLocation == null)
        {
            Utility.getInstance().showDialog("Location", "Please select a From location.", mContext);
        }
        else{
            progressDialog.show();

            try {
                Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getSaleFindBarcode("", barcode, selectedFromLocation.getCode());
                data.enqueue(new Callback<List<StockItemDTO>>() {
                    @Override
                    public void onResponse(Call<List<StockItemDTO>> call, Response<List<StockItemDTO>> response) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();

                        if (response.body() == null || response.body().size() == 0) {
                            Utility.getInstance().showDialog("Search Scan Lot", "No Has in Stock.", mContext);
                        } else {
                            if(response.body().size() > 1){
                                Utility.getInstance().showDialog("Search Scan Lot", "Two or more items were found.", mContext);
                            }
                            else{
                                boolean isSameBcr = false;
                                for(int a=0; a<mAdapter.mList.size(); a++){
                                    if(response.body().get(0).getBarCode().equals(mAdapter.mList.get(a).getBarCode())){
                                        isSameBcr = true;
                                    }
                                }

                                if(isSameBcr == false){
                                    mAdapter.mList.add(response.body().get(0));
                                    mAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Utility.getInstance().showDialog("Search Scan Lot", "Alredy Scan Item Barcode.", mContext);
                                    etBarcode.setText("");
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<StockItemDTO>> call, Throwable t) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Utility.getInstance().showDialog("Search Scan Lot", "Fail to GetData Server.", mContext);
                    }
                });
            } catch (Exception ex) {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                Utility.getInstance().showDialog("Search Scan Lot", ex.getMessage(), mContext);
                ex.printStackTrace();
            }
        }
    }

    private void setMove(){
        boolean isCheck = true;

        if(mAdapter.mList.size() == 0){
            Utility.getInstance().showDialog("Item", "There are no items to move.", mContext);
            isCheck = false;
        }

        if(selectedToLocation.getCode().equals(selectedFromLocation.getCode())){
            Utility.getInstance().showDialog("Location", "The warehouse before and after moving are the same.", mContext);
            isCheck = false;
        }

        if(!isCheck) return;

        if(selectedToLocation == null)
        {
            Utility.getInstance().showDialog("Location", "Please select a To location.", mContext);
        }
        else{
            progressDialog.show();

            try {

                Call<DbResultVO> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).MoveItemStock(mAdapter.mList, selectedToLocation.getCode(), LoginUser.getInstance().getUser().getUSERID(), Utility.getInstance().getToday());
                data.enqueue(new Callback<DbResultVO>() {
                    @Override
                    public void onResponse(Call<DbResultVO> call, Response<DbResultVO> response) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();

                        if (response.body() == null) {
                            Utility.getInstance().showDialog("Move Item", "No processing result has been received.", mContext);
                        } else {
                            if(response.body().getERR_CODE().equals("1"))
                            {
                                mAdapter.mList.clear();
                                mAdapter.notifyDataSetChanged();
                                Utility.getInstance().showDialog("Move Item", "Success.", mContext);
                            }
                            else{
                                Utility.getInstance().showDialog("Move Item", response.body().getERR_MSG(), mContext);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DbResultVO> call, Throwable t) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Utility.getInstance().showDialog("Move Item", t.getMessage(), mContext);

                        mAdapter.mList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception ex) {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                Utility.getInstance().showDialog("Move Item", ex.getMessage(), mContext);
                ex.printStackTrace();

                mAdapter.mList.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }


}