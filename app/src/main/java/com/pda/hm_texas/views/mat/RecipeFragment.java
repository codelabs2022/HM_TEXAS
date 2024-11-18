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
import com.pda.hm_texas.adapter.prod.OrderAdapter;
import com.pda.hm_texas.adapter.prod.RecipeAdapter;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.ProdCompsDTO;
import com.pda.hm_texas.dto.ProdOrderDTO;
import com.pda.hm_texas.event.OnItemClickLintner;
import com.pda.hm_texas.helper.ProdHelper;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeFragment extends Fragment implements OnItemClickLintner {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecipeAdapter mAdapter;
    private ProgressDialog progressDialog;
    private Context mContext;

    private String ordNo;
    private int lineNo;
    private OnItemClickLintner callback2;

    public RecipeFragment(String ordno, int lineno, OnItemClickLintner mainCallBack) {
        // Required empty public constructor
        ordNo = ordno;
        lineNo = lineno;
        callback2 = mainCallBack;
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
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mAdapter = new RecipeAdapter();
        mAdapter.SetOnClickListiner(this);
        RecyclerView rvList = view.findViewById(R.id.rvRecipe);
        rvList.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        rvList.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(view.getContext(), "Processing....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mContext = view.getContext();
        if(mAdapter.mList.size() == 0)LoadData();
        return view;
    }

    public void LoadData(){
        try {
            Call<List<ProdCompsDTO>> data = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getPdaProdRecipe(ordNo, lineNo);
            data.enqueue(new Callback<List<ProdCompsDTO>>() {
                @Override
                public void onResponse(Call<List<ProdCompsDTO>> call, Response<List<ProdCompsDTO>> response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() == null || response.body().size() == 0) {
                        Utility.getInstance().showDialog("Search Recipe", "No Has Recipe.", mContext);
                    } else {
                        mAdapter.mList.addAll(response.body());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<ProdCompsDTO>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            if (progressDialog.isShowing()) progressDialog.dismiss();

            Utility.getInstance().showDialog("Search Order", ex.getMessage(), mContext);
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemSelect(View v, int position) {
        try{
            ProdHelper.getInstance().setProdComps(mAdapter.SelectItem);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}