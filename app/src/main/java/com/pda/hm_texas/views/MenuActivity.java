package com.pda.hm_texas.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pda.hm_texas.R;
import com.pda.hm_texas.helper.Utility;
import com.pda.hm_texas.views.enter.EnterOrderActivity;
import com.pda.hm_texas.views.sale.SaleOrderActivity;
import com.pda.hm_texas.views.stock.CustBarcodeActivity;
import com.pda.hm_texas.views.stock.ItemInfoActivity;
import com.pda.hm_texas.views.stock.MoveItemActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout llStockIn = findViewById(R.id.llStockIn);
        LinearLayout llStockMove = findViewById(R.id.llStockMove);
        LinearLayout llPicking = findViewById(R.id.llPicking);
        LinearLayout llMat = findViewById(R.id.llMat);
        LinearLayout llStockList = findViewById(R.id.llStockList);
        LinearLayout llBarcode = findViewById(R.id.llMapping);

        llStockIn.setOnClickListener(this);
        llStockMove.setOnClickListener(this);
        llPicking.setOnClickListener(this);
        llMat.setOnClickListener(this);
        llStockList.setOnClickListener(this);
        llBarcode.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

        // super.onBackPressed();
    }
    @Override
    public void onClick(View v) {
        Intent i = null;

        if(v.getId() == R.id.llStockIn){
            i = new Intent(this, EnterOrderActivity.class);
        }
        else if(v.getId() == R.id.llStockMove){
            i = new Intent(this, MoveItemActivity.class);
        }
        else if(v.getId() == R.id.llPicking){
            i = new Intent(this, SaleOrderActivity.class);
        }
        else if(v.getId() == R.id.llMat){
            i = new Intent(this, MatActivity.class);
        }
        else if(v.getId() == R.id.llStockList){
            i = new Intent(this, ItemInfoActivity.class);
        }
        else if(v.getId() == R.id.llMapping){
            i = new Intent(this, CustBarcodeActivity.class);
        }

        if(i != null)startActivity(i);
        else Utility.getInstance().showDialog("MENU", "It's being prepared.", this);
    }
}