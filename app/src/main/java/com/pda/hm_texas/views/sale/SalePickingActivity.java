package com.pda.hm_texas.views.sale;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.ProdItemAdapter;
import com.pda.hm_texas.adapter.sale.SaleOrderAdapter;
import com.pda.hm_texas.adapter.sale.SaleOrderItemAdapter;
import com.pda.hm_texas.dig.PlcDialog;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dig.RecipeDialog;
import com.pda.hm_texas.dto.DbResultVO;
import com.pda.hm_texas.dto.PickingDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.event.OnScanListener;
import com.pda.hm_texas.event.ScanReceiver;
import com.pda.hm_texas.helper.LoginUser;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.SaleHelper;
import com.pda.hm_texas.helper.Utility;
import com.pda.hm_texas.views.mat.ProdActivity;

import java.math.BigDecimal;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalePickingActivity extends AppCompatActivity  implements View.OnClickListener, OnScanListener, OnItemLongClickListener {

    private Context mContext;
    private IntentFilter filter;
    private ScanReceiver mReciver = null;

    private TextView tvOrdeNo, tvItem, tvLoc, tvQty, tvPickingQty;
    private Button btnReg;
    private RecyclerView rvLotList, rvPickingList;
    private ProgressDialog progressDialog;
    private SaleOrderItemAdapter mAdapterLotinStock;
    private ProdItemAdapter mAdapterScanItem;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sale_picking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mContext = this;
        mReciver = new ScanReceiver();
        filter = new IntentFilter();
        filter.addAction("device.scanner.EVENT");
        filter.addCategory("android.intent.category.DEFAULT");

        this.registerReceiver(mReciver, filter);
        mReciver.SetOnScanListener(this);

        progressDialog = new ProgressDialog(SalePickingActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        tvOrdeNo = findViewById(R.id.tvSaleOrderNo);
        tvItem = findViewById(R.id.tvSaleRegItem);
        tvLoc = findViewById(R.id.tvSaleLoc);
        tvQty = findViewById(R.id.tvSaleOrderQty);
        tvPickingQty = findViewById(R.id.tvSalePickingQty);

        tvOrdeNo.setText(SaleHelper.getInstance().getOrder().getOrderNo());
        tvItem.setText(SaleHelper.getInstance().getOrder().getDescription());
        tvLoc.setText(SaleHelper.getInstance().getOrder().getLocationCode());
        tvQty.setText(SaleHelper.getInstance().getOrder().getOrderQty().stripTrailingZeros().toPlainString());
        tvPickingQty.setText("TOTAL PICKING : 0");

        btnReg = findViewById(R.id.btnSalePickingReg);
        btnReg.setOnClickListener(this::onClick);


        mAdapterLotinStock = new SaleOrderItemAdapter();
        rvLotList = findViewById(R.id.rvSaleLotInStock);
        rvLotList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvLotList.setAdapter(mAdapterLotinStock);

        mAdapterScanItem = new ProdItemAdapter(this);
        mAdapterScanItem.SetOnItemLongClickListiner(this);
        rvPickingList = findViewById(R.id.rvSalePickingItem);
        rvPickingList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvPickingList.setAdapter(mAdapterScanItem);

        LoadOrderLotInStock();
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

        if(view.getId() == R.id.btnSalePickingReg)
        {
            SetPicking();
        }

    }

    @Override
    public void OnScan(String ScanData) {
        if(mAdapterLotinStock.mList.size() == 0){
            Utility.getInstance().showDialog("Search Lot", "There are no items available for export.", mContext);
        }
        else{
            LoadBarcode(ScanData);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        try{
            mAdapterScanItem.mList.remove(position);
            mAdapterScanItem.notifyDataSetChanged();
        }
        catch (Exception ex){

        }
    }

    private void LoadOrderLotInStock(){
        progressDialog.show();

        try {
            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getSaleLotInStock(SaleHelper.getInstance().getOrder().getItemNo(), SaleHelper.getInstance().getOrder().getLocationCode());
            data.enqueue(new Callback<List<StockItemDTO>>() {
                @Override
                public void onResponse(Call<List<StockItemDTO>> call, Response<List<StockItemDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Lot", "No Has in Stock.", mContext);
                    } else {
                        mAdapterLotinStock.mList.clear();
                        mAdapterLotinStock.mList.addAll(response.body());
                        mAdapterLotinStock.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<StockItemDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialog("Search Lot", "Fail to GetData Server.", mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Lot", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

    private void LoadBarcode(String lotNo){
        progressDialog.show();

        try {
            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getSaleFindBarcode(SaleHelper.getInstance().getOrder().getItemNo(), lotNo, SaleHelper.getInstance().getOrder().getLocationCode());
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

                            boolean found  = mAdapterLotinStock.mList.stream().anyMatch(item -> item.getBarCode().equals(response.body().get(0).getBarCode()));


                            if(found){
                                mAdapterScanItem.mList.add(response.body().get(0));
                                mAdapterScanItem.notifyDataSetChanged();
                                BigDecimal totalQuantity = mAdapterScanItem.mList.stream()
                                                                                    .map(StockItemDTO::getRemainingQuantity)
                                                                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                                tvPickingQty.setText("TOTAL PICKING : "+totalQuantity.stripTrailingZeros().toPlainString());

                                OptionalInt index = IntStream.range(0, mAdapterLotinStock.mList.size())
                                        .filter(i -> mAdapterLotinStock.mList.get(i).getBarCode().equals(response.body().get(0).getBarCode()))
                                        .findFirst();
                                if (index.isPresent()) {
                                    mAdapterLotinStock.mList.get(index.getAsInt()).setSelect(true); // Index: 1
                                    mAdapterLotinStock.notifyDataSetChanged();
                                }

                            }
                            else{
                                Utility.getInstance().showDialog("Search Scan Lot", "The scanned item is not on the list of available items for shipment.", mContext);
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

    private void SetPicking(){
        progressDialog.show();

        try {

            PickingDTO pickings = new PickingDTO();
            pickings.setOrder(SaleHelper.getInstance().getOrder());
            pickings.setItems(mAdapterScanItem.mList);

            Call<DbResultVO> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).setPicking(pickings , LoginUser.getInstance().getUser().getUSERID());
            data.enqueue(new Callback<DbResultVO>() {
                @Override
                public void onResponse(Call<DbResultVO> call, Response<DbResultVO> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null ) {
                        Utility.getInstance().showDialog("Picking", "No processing result has been received.", mContext);
                    } else {
                        if(response.body().getERR_CODE().equals("S00")){
                            Utility.getInstance().showDialog("Picking", "Success.", mContext);
                            mAdapterScanItem.mList.clear();
                            mAdapterScanItem.notifyDataSetChanged();
                            LoadOrderLotInStock();
                        }
                        else{
                            Utility.getInstance().showDialog("Picking Fail", "Fail to Resister Picking", mContext);
                        }
                    }
                }

                @Override
                public void onFailure(Call<DbResultVO> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialog("Picking", "Fail to GetData Server.", mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Picking", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

}