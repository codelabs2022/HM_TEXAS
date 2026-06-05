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
import com.pda.hm_texas.dig.ItemDialog;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        tvOrder.setText(ProdHelper.getInstance().getProdOrder().getDxkLotNo());
        tvItem.setText(ProdHelper.getInstance().getProdOrder().getDescription());
        tvLoc.setText(ProdHelper.getInstance().getProdOrder().getLocationCode());
        tvQty.setText(ProdHelper.getInstance().getProdOrder().getRemainingQuantity().stripTrailingZeros().toPlainString() + " "+ ProdHelper.getInstance().getProdOrder().getUnitOfMeasureCode());

        Button btnReg = findViewById(R.id.btnMatReg);
        btnReg.setOnClickListener(this::onClick);

        Button btnShowRecipe = findViewById(R.id.btnPopUpRecipe);
        btnShowRecipe.setOnClickListener(this::onClick);

        Button btnShowPlc = findViewById(R.id.btnPopupPlc);
        btnShowPlc.setOnClickListener(this::onClick);

        //26.03.14 재고선택 추가
        Button btnSearchItems = findViewById(R.id.btnPopUpItem);
        btnSearchItems.setOnClickListener(this::onClick);

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

        //findViewById(R.id.textView16).setOnClickListener(this);
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
                    if(ProdHelper.getInstance().getProdComps() == null)
                    {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Recipe", "There are no recipes to select.", mContext);
                    }
                    else
                    {
                        LoadPlcData(ProdHelper.getInstance().getProdOrder().getItemNo());
                        //aa.SelectItem
                        tvProdCode.setText(ProdHelper.getInstance().getProdComps().getProdCode());
                        tvItemNo.setText(ProdHelper.getInstance().getProdComps().getItemNo());
                        tvItemName.setText(ProdHelper.getInstance().getProdComps().getDescription());
                        tvloc.setText(ProdHelper.getInstance().getProdComps().getCompsLocation());
                        tvNeedQty.setText(ProdHelper.getInstance().getProdComps().getExpectedQuantity().stripTrailingZeros().toPlainString());
                        tvRemainQty.setText(ProdHelper.getInstance().getProdComps().getReleaseQty().stripTrailingZeros().toPlainString());


                    }

                }
            });

            aa.show();
        }
        else if(view.getId() == R.id.btnPopupPlc){
            if(ProdHelper.getInstance().getProdComps() == null){
                Utility.getInstance().showDialogWithBlinkingEffect("Release", "Please select the recipe you wish to input.", mContext);
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
        else if(view.getId() == R.id.textView16)
        {
            //테스트 코드
            OnScan("HMP20251027-00001");
        }
        else if(view.getId() == R.id.btnPopUpItem){
            if(ProdHelper.getInstance().getProdComps() == null)
            {
                Utility.getInstance().showDialogWithBlinkingEffect("Search Release Item", "There are no recipes to select.", mContext);
            }
            else{
                ItemDialog aa = new ItemDialog(this, tvItemNo.getText().toString(), tvloc.getText().toString());
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.dimAmount = 0.8f;
                aa.getWindow().setAttributes(layoutParams);

                Button btnComplete = aa.findViewById(R.id.btnSetProdStock);
                btnComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(aa.mAdapter.mSelectedItem == null)
                        {
                            Utility.getInstance().showDialogWithBlinkingEffect("Search Stock Release Item", "Please select the materials to input.", mContext);
                        }
                        else{
                                                        

                            boolean isSameBcr = false;
                            StockItemDTO temp = aa.mAdapter.mSelectedItem;

                            // 2026.04.21 자재등록시에 자재는 수입검사 반제품은 출하검사 체크 추가
                            String inventoryPostingGroup = temp.getInventoryPostingGroup();
                            if (inventoryPostingGroup != null) {
                                if (inventoryPostingGroup.equals("RAWMAT")) {
                                    // 자재: 수입검사 결과가 PASS가 아니면 에러
                                    if ("1".equals(temp.getInsInYN())) {
                                        if (!"PASS".equals(temp.getInsInRes())) {
                                            Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The incoming inspection result is not 'PASS'.", mContext);
                                            return;
                                        }
                                    }
                                } else if (inventoryPostingGroup.equals("HALF-PROD")) {
                                    // 반제품: 공정검사여부(InsProdYN)나 출하검사여부(InsWrYN)가 설정되어 있는 경우 체크
                                    boolean isTargetProd = "1".equals(temp.getInsProdYN());
                                    boolean isTargetWr = "1".equals(temp.getInsWrYN());

                                    boolean hasProdPass = isTargetProd && "PASS".equals(temp.getInsProdRes());
                                    boolean hasWrPass = isTargetWr && "PASS".equals(temp.getInsWrRes());

                                    // 검사 대상인 항목이 하나라도 있고, 그 중 하나라도 PASS가 있으면 통과
                                    if ((isTargetProd || isTargetWr) && !(hasProdPass || hasWrPass)) {
                                        Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The inspection result (Process/Shipment) is not 'PASS'.", mContext);
                                        return;
                                    }
                                }
                            }

                            for(int k=0; k<mAdapter.mList.size(); k++){
                                if(mAdapter.mList.get(k).getBarCode().equals(temp.getBarCode())){
                                    isSameBcr = true;
                                    break;
                                }
                            }

                            if(isSameBcr){
                                Utility.getInstance().showDialogWithBlinkingEffect("Search Scan Lot", "Alredy Scan Item Barcode.", mContext);
                            }
                            else{
                                temp.setEmptyCaseQty(new BigDecimal(etEmptyCase.getText().toString()));
                                temp.setOriginalRemainingQuantity(temp.getRemainingQuantity());
                                mAdapter.mList.add(temp);
                                mAdapter.notifyDataSetChanged();
                            }
                            aa.dismiss();
                        }
                    }
                });

                Button btnCancle = aa.findViewById(R.id.btnCancleProdStock);
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        aa.dismiss();
                    }
                });

                aa.show();
            }
        }
    }

    @Override
    public void OnScan(String ScanData) {
        if(ProdHelper.getInstance().getProdComps() == null){
            Utility.getInstance().showDialogWithBlinkingEffect("Search Recipe", "Please Select Recipe.", mContext);
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
            tvNeedQty.setText("");
            tvRemainQty.setText("");

            tvMat.setText("");
            tvStatus.setText("");
            tvInput.setText("");
            tvApply.setText("");

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

            Utility.getInstance().showDialogWithBlinkingEffect("Search Plc", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }
//    private void FindStockItem(String barcode){
//        progressDialog.show();
//
//        try {
//            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getStockItemInfo(barcode,  ProdHelper.getInstance().getProdComps().getCompsLocation());
//            data.enqueue(new Callback<List<StockItemDTO>>() {
//                @Override
//                public void onResponse(Call<List<StockItemDTO>> call, Response<List<StockItemDTO>> response) {
//                    if (progressDialog.isShowing()) progressDialog.dismiss();
//
//                    if (response.body() == null || response.body().size() == 0) {
//                        Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", "No Has in Stock.", mContext);
//                    } else {
//                        StockItemDTO selectStockItem = response.body().get(0);
//                        if(!selectStockItem.getItemNo().equals(tvItemNo.getText().toString())){
//                            Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", "The material is of a different product number than the selected recipe.", mContext);
//                        }
//                        else{
//                            // 2026.04.21 자재등록시에 자재는 수입검사 반제품은 출하검사 체크 추가
//                            String inventoryPostingGroup = selectStockItem.getInventoryPostingGroup();
//                            if (inventoryPostingGroup != null) {
//                                if (inventoryPostingGroup.equals("RAWMAT")) {
//                                    // 자재: 수입검사 결과가 PASS가 아니면 에러
//                                    if ("1".equals(selectStockItem.getInsInYN())) {
//                                        if (!"PASS".equals(selectStockItem.getInsInRes())) {
//                                            Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The incoming inspection result is not 'PASS'.", mContext);
//                                            return;
//                                        }
//                                    }
//                                } else if (inventoryPostingGroup.equals("HALF-PROD")) {
//                                    // 반제품: 공정검사여부(InsProdYN)나 출하검사여부(InsWrYN)가 설정되어 있는 경우 체크
//                                    boolean isTargetProd = "1".equals(selectStockItem.getInsProdYN());
//                                    boolean isTargetWr = "1".equals(selectStockItem.getInsWrYN());
//
//                                    boolean hasProdPass = isTargetProd && "PASS".equals(selectStockItem.getInsProdRes());
//                                    boolean hasWrPass = isTargetWr && "PASS".equals(selectStockItem.getInsWrRes());
//
//                                    // 검사 대상인 항목이 하나라도 있고, 그 중 하나라도 PASS가 있으면 통과
//                                    if ((isTargetProd || isTargetWr) && !(hasProdPass || hasWrPass)) {
//                                        Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The inspection result (Process/Shipment) is not 'PASS'.", mContext);
//                                        return;
//                                    }
//                                }
//                            }
//
//                            // 1. 문자열로 된 유효기간을 LocalDate로 변환 (예: "2026-05-29")
//                            // 데이터 형식에 맞춰 DateTimeFormatter를 수정하세요.
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//                            try {
//                                LocalDate expirationDate = LocalDate.parse(selectStockItem.getExpirationDate(), formatter);
//                                LocalDate today = LocalDate.now();
//
//                                // 2. 유효기간(expirationDate)이 오늘(today)보다 과거인지 비교
//                                if (expirationDate.isBefore(today)) {
//
//                                    // 3. 유효기간 만료 시 경고 팝업 호출
//                                    // (기존 Helper 클래스 호출 방식에 맞게 조정하세요)
//                                    Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The material cannot be inserted as its expiration date has passed.", mContext);
//
//                                    // 4. 로직 종료
//                                    return;
//                                }
//                            } catch (DateTimeParseException e) {
//                                // 날짜 형식이 잘못되었을 경우 처리
//                                Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The ExpirationDate data is incorrect.", mContext);
//                            }
//
//                            boolean isSameBcr = false;
//
//                            for(int k=0; k<mAdapter.mList.size(); k++){
//                                if(mAdapter.mList.get(k).getBarCode().equals(selectStockItem.getBarCode())){
//                                    isSameBcr = true;
//                                    break;
//                                }
//                            }
//
//                            if(isSameBcr){
//                                Utility.getInstance().showDialogWithBlinkingEffect("Search Scan Lot", "Alredy Scan Item Barcode.", mContext);
//                            }
//                            else{
//                                for(int i=0; i<response.body().size(); i++){
//                                    response.body().get(i).setEmptyCaseQty(new BigDecimal(etEmptyCase.getText().toString()));
//                                    response.body().get(i).setOriginalRemainingQuantity(response.body().get(i).getRemainingQuantity());
//                                }
//                                mAdapter.mList.addAll(response.body());
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<StockItemDTO>> call, Throwable t) {
//                    if (progressDialog.isShowing()) progressDialog.dismiss();
//                    Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", t.getMessage(), mContext);
//                }
//            });
//        } catch (Exception ex) {
//            if (progressDialog.isShowing()) progressDialog.dismiss();
//
//            Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", ex.getMessage(), mContext);
//            ex.printStackTrace();
//        }
//    }

    private void FindStockItem(String barcode) {
        progressDialog.show();

        try {
            Call<List<StockItemDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL)
                    .getStockItemInfo(barcode, ProdHelper.getInstance().getProdComps().getCompsLocation());

            data.enqueue(new Callback<List<StockItemDTO>>() {
                @Override
                public void onResponse(Call<List<StockItemDTO>> call, Response<List<StockItemDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().isEmpty()) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", "No stock found.", mContext);
                        return; // 더 이상 진행하지 않고 즉시 종료 (Early return)
                    }

                    // 1. 무조건 첫 번째 데이터만 참조 (사용자 업무 규칙 적용)
                    StockItemDTO selectStockItem = response.body().get(0);

                    // [품번 일치 여부 체크]
                    if (!selectStockItem.getItemNo().equals(tvItemNo.getText().toString())) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", "The material is of a different product number than the selected recipe.", mContext);
                        return;
                    }

                    // [수입 / 공정 / 출하 검사 결과 체크]
                    String inventoryPostingGroup = selectStockItem.getInventoryPostingGroup();
                    if (inventoryPostingGroup != null) {
                        if (inventoryPostingGroup.equals("RAWMAT")) {
                            // 자재: 수입검사 결과 PASS 체크
                            if ("1".equals(selectStockItem.getInsInYN()) && !"PASS".equals(selectStockItem.getInsInRes())) {
                                Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The incoming inspection result is not 'PASS'.", mContext);
                                return;
                            }
                        } else if (inventoryPostingGroup.equals("HALF-PROD")) {
                            // 반제품: 공정검사나 출하검사 중 하나라도 PASS면 통과 (기존 로직 유지)
                            boolean isTargetProd = "1".equals(selectStockItem.getInsProdYN());
                            boolean isTargetWr = "1".equals(selectStockItem.getInsWrYN());
                            boolean hasProdPass = isTargetProd && "PASS".equals(selectStockItem.getInsProdRes());
                            boolean hasWrPass = isTargetWr && "PASS".equals(selectStockItem.getInsWrRes());

                            if ((isTargetProd || isTargetWr) && !(hasProdPass || hasWrPass)) {
                                Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The inspection result (Process/Shipment) is not 'PASS'.", mContext);
                                return;
                            }
                        }
                    }

                    // [유효기간 만료 체크] - NullPointerException 방지 로직 포함
                    String expDateStr = selectStockItem.getExpirationDate();
                    if (expDateStr == null || expDateStr.trim().isEmpty()) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The ExpirationDate data is missing.", mContext);
                        return;
                    }

                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate expirationDate = LocalDate.parse(expDateStr, formatter);
                        if (expirationDate.isBefore(LocalDate.now())) {
                            Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The material cannot be inserted as its expiration date has passed.", mContext);
                            return;
                        }
                    } catch (DateTimeParseException e) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Inspection Result", "The ExpirationDate data is incorrect.", mContext);
                        return;
                    }

                    // [중복 스캔 체크]
                    for (StockItemDTO item : mAdapter.mList) {
                        if (item.getBarCode().equals(selectStockItem.getBarCode())) {
                            Utility.getInstance().showDialogWithBlinkingEffect("Search Scan Lot", "Already scanned item barcode.", mContext);
                            return;
                        }
                    }

                    // [어댑터 추가 작업] - NumberFormatException 방지 및 단일 객체 추가
                    try {
                        String emptyCaseStr = etEmptyCase.getText().toString();
                        // 값이 비어있으면 0으로 처리, 아니면 입력된 숫자 사용
                        BigDecimal emptyCaseQty = emptyCaseStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(emptyCaseStr);

                        selectStockItem.setEmptyCaseQty(emptyCaseQty);
                        selectStockItem.setOriginalRemainingQuantity(selectStockItem.getRemainingQuantity());

                        // 기존 addAll 대신 검증이 완료된 단일 첫 번째 데이터(selectStockItem)만 추가
                        mAdapter.mList.add(selectStockItem);
                        mAdapter.notifyDataSetChanged();

                    } catch (NumberFormatException e) {
                        Utility.getInstance().showDialogWithBlinkingEffect("Input Error", "Empty Case Quantity must be a valid number.", mContext);
                    }
                }

                @Override
                public void onFailure(Call<List<StockItemDTO>> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", t.getMessage(), mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();
            Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

    private void ReleaseMat(){
        try {

            boolean iValidation = true;
            if(mAdapter.mList.size() == 0){
                Utility.getInstance().showDialogWithBlinkingEffect("Release", "There are no materials to input.", mContext);
                iValidation = false;
            }

            if(ProdHelper.getInstance().getProdComps() == null){
                Utility.getInstance().showDialogWithBlinkingEffect("Release", "Please select the recipe you wish to input.", mContext);
                iValidation = false;
            }

            for(int i=0; i<mAdapter.mList.size(); i++){
                if(mAdapter.mList.get(i).getRemainingQuantity().floatValue() == 0)
                {
                    Utility.getInstance().showDialogWithBlinkingEffect("Release", "There are materials with a material registration quantity of 0.", mContext);
                    iValidation = false;
                    break;
                }
            }

            if(plcs.size() > 0)
            {
                if(ProdHelper.getInstance().getProdPlc() == null)
                {
                    Utility.getInstance().showDialogWithBlinkingEffect("Release", "There is PLC information that needs to be input..", mContext);
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
                temp.setP_COMPSRACK(mAdapter.mList.get(i).getRackCode());
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
                        Utility.getInstance().showDialogWithBlinkingEffect("Release", "No processing result has been received.", mContext);
                    } else {

                        if(response.body().getERR_CODE().equals("S00")){
                            Utility.getInstance().showDialog("Release", "Success.", mContext);
                            mAdapter.mList.clear();
                            mAdapter.notifyDataSetChanged();

                            etEmptyCase.setText("0");

                            init();
                        }
                        else{
                            Utility.getInstance().showDialogWithBlinkingEffect("Release Fail", response.body().getERR_MSG(), mContext);
                        }
                    }
                }

                @Override
                public void onFailure(Call<DbResultVO> call, Throwable t) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", t.getMessage(), mContext);
                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("Search Barcode", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }


}