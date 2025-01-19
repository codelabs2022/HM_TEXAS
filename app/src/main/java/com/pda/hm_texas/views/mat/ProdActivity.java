package com.pda.hm_texas.views.mat;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.OrderAdapter;
import com.pda.hm_texas.adapter.prod.ProdItemAdapter;
import com.pda.hm_texas.dig.PlcDialog;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dig.RecipeDialog;
import com.pda.hm_texas.dto.DbResultVO;
import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.ProdOrderDTO;
import com.pda.hm_texas.dto.ReleaseDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.event.OnItemLongClickListener;
import com.pda.hm_texas.event.OnScanListener;
import com.pda.hm_texas.event.ScanReceiver;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;
import com.pda.hm_texas.views.mat.rout.Dispersantfragment;
import com.pda.hm_texas.views.mat.rout.InsulationFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdActivity extends AppCompatActivity implements View.OnClickListener, OnScanListener, OnItemLongClickListener, OnItemClickLintner {

    private Context mContext;
    private String ProdItemNo, ProdOrderNo ;
    private int ProdLineNo;
    public TextView tvItemNo, tvItemName, tvProdCode
            , tvloc, tvNeedQty, tvRemainQty;
    public TextView tvMat, tvStatus  , tvInput, tvApply;
    public EditText etEmptyCase;
    private ProdItemAdapter mAdapter;
    private ProgressDialog progressDialog;

    private IntentFilter filter;
    private ScanReceiver mReciver = null;

    private List<PlcMatrailDTO> plcs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prod);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mContext = this;
        mReciver = new ScanReceiver();
        ProdItemNo = ProdHelper.getInstance().getProdOrder().getItemNo();
        ProdOrderNo = ProdHelper.getInstance().getProdOrder().getProdOrderNo();
        ProdLineNo =  ProdHelper.getInstance().getProdOrder().getProdOrderLineNo();

        TextView tvOrder = findViewById(R.id.tvMatRegOrderNo);
        TextView tvItem =findViewById(R.id.tvMatRegItem);
        TextView tvLoc = findViewById(R.id.tvMatRegLoc);
        TextView tvQty = findViewById(R.id.tvMatRegQty);

        tvOrder.setText(ProdHelper.getInstance().getProdOrder().getProdOrderNo());
        tvItem.setText(ProdHelper.getInstance().getProdOrder().getDescription());
        tvLoc.setText(ProdHelper.getInstance().getProdOrder().getLocationCode());
        tvQty.setText(ProdHelper.getInstance().getProdOrder().getRemainingQuantity().stripTrailingZeros().toPlainString() + " "+ ProdHelper.getInstance().getProdOrder().getUnitOfMeasureCode());

        Button btnReg = findViewById(R.id.btnMatReg);
        btnReg.setOnClickListener(this::onClick);

        Button btnShowRecipe = findViewById(R.id.btnPopUpRecipe);
        btnShowRecipe.setOnClickListener(this::onClick);

        Button btnShowPlc = findViewById(R.id.btnPopupPlc);
        btnShowPlc.setOnClickListener(this::onClick);

        tvItemNo = findViewById(R.id.tvProdRecipeItemNo);
        tvItemName = findViewById(R.id.tvProdRecipeItemName);
        tvProdCode = findViewById(R.id.tvProdRecipeProdCode);
        tvloc = findViewById(R.id.tvProdRecipeLoc);
        tvNeedQty = findViewById(R.id.tvProdRecipeNeedQty);
        tvRemainQty = findViewById(R.id.tvProdRecipeRQty);

        tvMat = findViewById(R.id.tvProdPlcMat);
        tvStatus = findViewById(R.id.tvProdPlcStatus);
        tvInput = findViewById(R.id.tvProdPlcInput);
        tvApply = findViewById(R.id.tvProdPlcApply);

        etEmptyCase = findViewById(R.id.etProdEmptyCase);

        filter = new IntentFilter();
        filter.addAction("device.scanner.EVENT");
        filter.addCategory("android.intent.category.DEFAULT");

        plcs = new ArrayList<>();

        this.registerReceiver(mReciver, filter);
        mReciver.SetOnScanListener(this);

        mAdapter = new ProdItemAdapter(this);
        mAdapter.SetOnItemLongClickListiner(this);
        mAdapter.SetOnQtyChangeListener(this);
        RecyclerView rvList = findViewById(R.id.rvProdItem);
        rvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvList.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(ProdActivity.this, "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }
    @Override
    protected void onDestroy() {
        ProdHelper.getInstance().setProdOrder(null);
        this.unregisterReceiver(mReciver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        ProdHelper.getInstance().setProdOrder(null);
        finish();
        super.onBackPressed();

        // super.onBackPressed();
    }

    @Override
    public void onItemSelect(View v, int position) {

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
    public void onClick(View view) {
        if(view.getId() == R.id.btnMatReg)
        {
            ReleaseMat();
        }
        else if(view.getId() == R.id.btnPopUpRecipe){
            RecipeDialog aa = new RecipeDialog(this, ProdOrderNo, ProdLineNo);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.8f;
            aa.getWindow().setAttributes(layoutParams);

            Button btnComplete = aa.findViewById(R.id.btnSetRecipe);
            btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aa.dismiss();
                    LoadPlcData(ProdHelper.getInstance().getProdOrder().getItemNo());
                    //aa.SelectItem
                    tvProdCode.setText(ProdHelper.getInstance().getProdComps().getProdCode());
                    tvItemNo.setText(ProdHelper.getInstance().getProdComps().getItemNo());
                    tvItemName.setText(ProdHelper.getInstance().getProdComps().getDescription());
                    tvloc.setText(ProdHelper.getInstance().getProdComps().getCompsLocation());
                    tvNeedQty.setText(ProdHelper.getInstance().getProdComps().getExpectedQuantity().stripTrailingZeros().toPlainString());
                    tvRemainQty.setText(ProdHelper.getInstance().getProdComps().getReleaseQty().stripTrailingZeros().toPlainString());
                }
            });

            aa.show();
        }
        else if(view.getId() == R.id.btnPopupPlc){
            if(ProdHelper.getInstance().getProdComps() == null){
                Utility.getInstance().showDialog("Release", "Please select the recipe you wish to input.", mContext);
            }
            else{

                PlcDialog bb = new PlcDialog(this, plcs);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.dimAmount = 0.8f;
                bb.getWindow().setAttributes(layoutParams);

                Button btnComplete = bb.findViewById(R.id.btnSetPlc);
                btnComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bb.dismiss();

                        if(ProdHelper.getInstance().getProdPlc() != null){
                            tvMat.setText(ProdHelper.getInstance().getProdPlc().getMatCode());
                            tvStatus.setText(ProdHelper.getInstance().getProdPlc().getProdCode());
                            tvInput.setText(ProdHelper.getInstance().getProdPlc().getPlcQty().stripTrailingZeros().toPlainString());
                            tvApply.setText(ProdHelper.getInstance().getProdPlc().getApplyQty().stripTrailingZeros().toPlainString());
                        }

                    }
                });

                bb.show();
            }
        }
    }

    @Override
    public void OnScan(String ScanData) {
        if(ProdHelper.getInstance().getProdComps() == null){
            Utility.getInstance().showDialog("Search Recipe", "Please Select Recipe.", mContext);
        }
//        else if(ProdHelper.getInstance().getProdPlc() == null){
//            Utility.getInstance().showDialog("Search Plc", "Please Select Plc.", mContext);
//        }
        else{
            FindStockItem(ScanData);
        }
    }
    private void init(){
        try{
            mAdapter.mList.clear();
            mAdapter.notifyDataSetChanged();

            ProdHelper.getInstance().setProdPlc(null);
            ProdHelper.getInstance().setProdComps(null);

            tvProdCode.setText("");
            tvItemNo.setText("");
            tvItemName.setText("");
            tvloc.setText("");
            tvNeedQty.setText(0);
            tvRemainQty.setText(0);

            tvMat.setText("");
            tvStatus.setText("");
            tvInput.setText(0);
            tvApply.setText(0);
        }
        catch (Exception ex){

        }
    }

    public void LoadPlcData(String itemNo){
        try {
            Call<List<PlcMatrailDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getPlcInfo(itemNo);
            data.enqueue(new Callback<List<PlcMatrailDTO>>() {
                @Override
                public void onResponse(Call<List<PlcMatrailDTO>> call, Response<List<PlcMatrailDTO>> response) {
                    if (response.body() == null || response.body().size() == 0) {
                       // Utility.getInstance().showDialog("Search Plc", "No Has Plc.", mContext);
                        ProdHelper.getInstance().setProdPlc(null);
                    } else {
                        plcs.clear();

                        for(PlcMatrailDTO dto : response.body()){
                            if(dto.getMatCodeToItem().equals(ProdHelper.getInstance().getProdComps().getItemNo())){
                                plcs.add(dto);
                            }
                        }
                        //mAdapter.mList.addAll(response.body());
                        //mAdapter.notifyDataSetChanged();
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
    private void FindStockItem(String barcode){
        progressDialog.show();

        try {
            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getStockItemInfo(barcode,  ProdHelper.getInstance().getProdComps().getCompsLocation());
            data.enqueue(new Callback<List<StockItemDTO>>() {
                @Override
                public void onResponse(Call<List<StockItemDTO>> call, Response<List<StockItemDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Barcode", "No Has in Stock.", mContext);
                    } else {

                        if(!response.body().get(0).getItemNo().equals(tvItemNo.getText().toString())){
                            Utility.getInstance().showDialog("Search Barcode", "The material is of a different product number than the selected recipe.", mContext);
                        }
                        else{
                            boolean isSameBcr = false;

                            for(int k=0; k<mAdapter.mList.size(); k++){
                                if(mAdapter.mList.get(k).getBarCode().equals(response.body().get(0).getBarCode())){
                                    isSameBcr = true;
                                    break;
                                }
                            }

                            if(isSameBcr){
                                Utility.getInstance().showDialog("Search Scan Lot", "Alredy Scan Item Barcode.", mContext);
                            }
                            else{
                                for(int i=0; i<response.body().size(); i++){
                                    response.body().get(i).setEmptyCaseQty(new BigDecimal(etEmptyCase.getText().toString()));
                                }
                                mAdapter.mList.addAll(response.body());
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<List<StockItemDTO>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Barcode", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

    private void ReleaseMat(){
        try {

            boolean iValidation = true;
            if(mAdapter.mList.size() == 0){
                Utility.getInstance().showDialog("Release", "There are no materials to input.", mContext);
                iValidation = false;
            }

            if(ProdHelper.getInstance().getProdComps() == null){
                Utility.getInstance().showDialog("Release", "Please select the recipe you wish to input.", mContext);
                iValidation = false;
            }

            for(int i=0; i<mAdapter.mList.size(); i++){
                if(mAdapter.mList.get(i).getRemainingQuantity().floatValue() == 0)
                {
                    Utility.getInstance().showDialog("Release", "There are materials with a material registration quantity of 0.", mContext);
                    iValidation = false;
                    break;
                }
            }

            if(plcs.size() > 0)
            {
                if(ProdHelper.getInstance().getProdPlc() == null)
                {
                    Utility.getInstance().showDialog("Release", "There is PLC information that needs to be input..", mContext);
                    iValidation = false;
                }
            }

            if(iValidation == false) return;

            progressDialog.show();
            List<ReleaseDTO> sendDatas = new ArrayList<>();

            for(int i=0; i<mAdapter.mList.size(); i++){

                ReleaseDTO temp = new ReleaseDTO();

                temp.setP_WKDATE(Utility.getInstance().getToday());
                temp.setP_WORKORDER(ProdHelper.getInstance().getProdComps().getProdOrderNo());
                temp.setP_WORKORDERLINE(ProdHelper.getInstance().getProdComps().getProdOrderLineNo());
                temp.setP_ROUTNO(ProdHelper.getInstance().getProdOrder().getRoutingNo());
                temp.setP_BOMNO(ProdHelper.getInstance().getProdOrder().getProductionBOMNo());
                temp.setP_PRODITEMNO(ProdHelper.getInstance().getProdOrder().getItemNo());
                temp.setP_COMPSLINENO(ProdHelper.getInstance().getProdComps().getCompsLineNo());
                temp.setP_COMPSITEMNO(ProdHelper.getInstance().getProdComps().getItemNo());
                temp.setP_COMPSLOCATION(ProdHelper.getInstance().getProdComps().getCompsLocation());
                temp.setP_COMPSEMPTYQTY(mAdapter.mList.get(i).getEmptyCaseQty());
                temp.setP_COMPSQTY(mAdapter.mList.get(i).getRemainingQuantity());
                temp.setP_COMPSLOTNO(mAdapter.mList.get(i).getBarCode());
                temp.setP_COMPSEXPDATE(mAdapter.mList.get(i).getExpirationDate());
                temp.setP_CUSTLOTNO(mAdapter.mList.get(i).getLotNo());
                temp.setP_ERPLOCATION(ProdHelper.getInstance().getProdComps().getProdCode());
                temp.setP_MANUFACTURINGDATE(mAdapter.mList.get(i).getManufacturingDate());

                sendDatas.add(temp);
            }
            int plcStep = 0;
            long plcId = 0;
            if(ProdHelper.getInstance().getProdPlc() != null)
            {
                plcStep = 0;//ProdHelper.getInstance().getProdPlc().getStepGroup();
                plcId = ProdHelper.getInstance().getProdPlc().getRid();
            }

            Call<DbResultVO> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).addWorkRelease(sendDatas,  "PDA", plcStep, plcId);
            data.enqueue(new Callback<DbResultVO>() {
                @Override
                public void onResponse(Call<DbResultVO> call, Response<DbResultVO> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null ) {
                        Utility.getInstance().showDialog("Release", "No processing result has been received.", mContext);
                    } else {

                        if(response.body().getERR_CODE().equals("S00")){
                            Utility.getInstance().showDialog("Release", "Success.", mContext);
                            mAdapter.mList.clear();
                            mAdapter.notifyDataSetChanged();

                            etEmptyCase.setText("0");

                            init();
                        }
                        else{
                            Utility.getInstance().showDialog("Release Fail", response.body().getERR_MSG(), mContext);
                        }
                    }
                }

                @Override
                public void onFailure(Call<DbResultVO> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Barcode", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }


}