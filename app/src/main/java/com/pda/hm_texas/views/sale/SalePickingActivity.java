package com.pda.hm_texas.views.sale;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.event.OnMsgBoxClickListener;
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

public class SalePickingActivity extends AppCompatActivity  implements View.OnClickListener, OnScanListener, OnItemLongClickListener, OnItemClickLintner {

    private Context mContext;
    private IntentFilter filter;
    private ScanReceiver mReciver = null;
    private InputMethodManager imm;

    private TextView tvOrdeNo, tvItem, tvLoc, tvQty, tvPickingQty;
    private Button btnReg;
    private RecyclerView rvLotList, rvPickingList;
    private ProgressDialog progressDialog;
    private SaleOrderItemAdapter mAdapterLotinStock;
    private ProdItemAdapter mAdapterScanItem;

    private boolean isCheck = true;
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

        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);

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
        mAdapterScanItem.SetOnQtyChangeListener(this);
        rvPickingList = findViewById(R.id.rvSalePickingItem);
        rvPickingList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvPickingList.setAdapter(mAdapterScanItem);

        LoadOrderLotInStock();
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mReciver);
        SaleHelper.getInstance().setOrder(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        SaleHelper.getInstance().setOrder(null);
        super.onBackPressed();

        // super.onBackPressed();
    }
    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnSalePickingReg)
        {
            CheckPicking();
        }

    }

    @Override
    public void OnScan(String ScanData) {
        if(mAdapterLotinStock.mList.size() == 0){
            Utility.getInstance().showDialog("Search Lot", "There are no items available for export.", mContext);
        }
        else{
            imm.hideSoftInputFromWindow(rvLotList.getWindowToken(), 0);
            LoadBarcode(ScanData);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        try{

            for(StockItemDTO stockitem : mAdapterLotinStock.mList){
                if(stockitem.getBarCode().equals(mAdapterScanItem.mList.get(position).getBarCode())){
                    stockitem.setSelect(false);
                    mAdapterLotinStock.notifyDataSetChanged();
                    break;
                }
            }

            mAdapterScanItem.mList.remove(position);
            mAdapterScanItem.notifyDataSetChanged();
        }
        catch (Exception ex){

        }
    }

    @Override
    public void onItemSelect(View v, int position) {
        try{
            BigDecimal totalQuantity = mAdapterScanItem.mList.stream()
                    .map(StockItemDTO::getRemainingQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            tvPickingQty.setText("TOTAL PICKING : "+totalQuantity.stripTrailingZeros().toPlainString());
        }
        catch (Exception ex){

        }
    }
    private void LoadOrderLotInStock(){
        progressDialog.show();

        try {
            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getSaleLotInStock(
                                                                                                                    SaleHelper.getInstance().getOrder().getOrderNo(),
                                                                                                                    SaleHelper.getInstance().getOrder().getLineNo(),
                                                                                                                    SaleHelper.getInstance().getOrder().getItemNo(),
                                                                                                                    SaleHelper.getInstance().getOrder().getLocationCode());
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
                                boolean isSameBcr = false;
                                boolean isNotFirstMat = false;
                                boolean isNotFindMat = true;

                                for(int k=0; k<mAdapterScanItem.mList.size(); k++){
                                    if(mAdapterScanItem.mList.get(k).getBarCode().equals(response.body().get(0).getBarCode())){
                                        isSameBcr = true;
                                        break;
                                    }
                                }

                                for(int k=0; k<mAdapterLotinStock.mList.size(); k++){
                                    if(mAdapterLotinStock.mList.get(k).getBarCode().equals(response.body().get(0).getBarCode())){
                                        isNotFindMat = false;
                                        break;
                                    }
                                }

                                for(int k=0; k<mAdapterLotinStock.mList.size(); k++){
                                    String ordMfcDate = mAdapterLotinStock.mList.get(k).getManufacturingDate();
                                    String scanMfcDate = response.body().get(0).getManufacturingDate();

                                    if(TextUtils.isEmpty(ordMfcDate))ordMfcDate = Utility.getInstance().getToday();
                                    if(TextUtils.isEmpty(scanMfcDate))scanMfcDate = Utility.getInstance().getToday();

                                    if(Utility.getInstance().isPastDate(ordMfcDate, scanMfcDate) == 1){
                                        if(mAdapterLotinStock.mList.get(k).isSelect() == false){
                                            isNotFirstMat = true;
                                            break;
                                        }
                                    }
                                }

                                if(isSameBcr == true ){
                                    Utility.getInstance().showDialog("Search Scan Lot", "Alredy Scan Item Barcode.", mContext);
                                }
                                else if(isNotFirstMat == true){
                                    Utility.getInstance().showDialog("Search Scan Lot", "There are LOT products that were produced previously.", mContext);
                                }
                                else if(isNotFindMat == true){
                                    Utility.getInstance().showDialog("Search Scan Lot", "This inventory cannot be picked.", mContext);
                                }
                                else{
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
    private void CheckPicking(){
        isCheck = true;

        if(mAdapterScanItem.mList.size() == 0){
            Utility.getInstance().showDialog("Item", "There are no items to Picking.", mContext);
            isCheck = false;
        }

        for(StockItemDTO dto : mAdapterScanItem.mList){
            if(dto.getRemainingQuantity().floatValue() == 0){
                Utility.getInstance().showDialog("Item", "There are items with quantities of 0 or not entered.", mContext);
                isCheck = false;
                break;
            }
            if(dto.getRemainingQuantity() == null){
                Utility.getInstance().showDialog("Item", "There are items with quantities of 0 or not entered.", mContext);
                isCheck = false;
                break;
            }
        }
        //String txtTotalPickingQty = tvPickingQty.getText().toString().split(":")[1].trim();

        if(!isCheck)return;

        isCheck = true;
        if(SaleHelper.getInstance().getOrder().getOrderQty().floatValue() < new BigDecimal(tvPickingQty.getText().toString().split(":")[1].trim()).floatValue()){
            Utility.getInstance().showDialogByConfirm("PICKING", "quantity greater than the specified quantity has been picked. Would you like to register?", mContext, new OnMsgBoxClickListener() {
                @Override
                public void OnConfirm() {
                    isCheck = true;
                    SetPicking();
                }

                @Override
                public void OnCancle() {
                    isCheck = false;
                }
            });
        }
        else{
            SetPicking();
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
                            finish();
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