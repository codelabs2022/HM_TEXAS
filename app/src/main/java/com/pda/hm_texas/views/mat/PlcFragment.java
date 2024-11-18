package com.pda.hm_texas.views.mat;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pda.hm_texas.R;
import com.pda.hm_texas.adapter.prod.PlcAdapter;
import com.pda.hm_texas.adapter.prod.RecipeAdapter;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.ProdCompsDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlcFragment extends Fragment implements OnItemClickLintner {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String itemNo;
    private PlcAdapter mAdapter;
    private Context mContext;

    public PlcFragment(String item) {
        // Required empty public constructor
        itemNo = item;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plc, container, false);

        mAdapter = new PlcAdapter();
        mAdapter.SetOnClickListiner(this);
        RecyclerView rvList = view.findViewById(R.id.rvPlc);
        rvList.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        rvList.setAdapter(mAdapter);

        mContext = view.getContext();
        LoadData();

        return view;
    }

    @Override
    public void onItemSelect(View v, int position) {
        try{
            if(ProdHelper.getInstance().getProdComps().getItemNo().equals(mAdapter.SelectItem.getStepMat())){
                ProdHelper.getInstance().setProdPlc(mAdapter.SelectItem);
            }
            else{
                Utility.getInstance().showDialog("Plc", "The selected material and other PLC information.", mContext);
                mAdapter.SelectClear();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void LoadData(){
        try {
            Call<List<PlcMatrailDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getPlcInfo(itemNo);
            data.enqueue(new Callback<List<PlcMatrailDTO>>() {
                @Override
                public void onResponse(Call<List<PlcMatrailDTO>> call, Response<List<PlcMatrailDTO>> response) {
                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Plc", "No Has Plc.", mContext);
                    } else {
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<PlcMatrailDTO>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {

            Utility.getInstance().showDialog("Search Order", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }
}