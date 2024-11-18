package com.pda.hm_texas.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.pda.hm_texas.R;
import com.pda.hm_texas.event.OnButtonClickListener;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.views.mat.ProdOrderActivity;
import com.pda.hm_texas.views.mat.rout.Dispersantfragment;
import com.pda.hm_texas.views.mat.rout.InsulationFragment;

public class MatActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener ,OnButtonClickListener{

    private Fragment insulationFragment, dispersantfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        insulationFragment = new InsulationFragment();
        dispersantfragment = new Dispersantfragment();

        getSupportFragmentManager().beginTransaction().add(R.id.frame, insulationFragment).commit();
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(this);
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
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();

        Fragment selected = null;
        if(position == 0){
            selected = insulationFragment;
        }else if (position == 1){
            selected = dispersantfragment;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onButtonClicked(String Code) {
        ProdHelper.getInstance().setRout(Code);
        Intent i = new Intent(this, ProdOrderActivity.class);
        startActivity(i);
        finish();
    }
}