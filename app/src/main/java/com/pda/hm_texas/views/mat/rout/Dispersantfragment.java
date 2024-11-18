package com.pda.hm_texas.views.mat.rout;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pda.hm_texas.R;
import com.pda.hm_texas.event.OnButtonClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dispersantfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dispersantfragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnButtonClickListener mListener = null ;

    public Dispersantfragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Activity가 인터페이스를 구현하는지 확인 후 리스너 할당
        if (context instanceof OnButtonClickListener) {
            mListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnButtonClickListener");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dispersantfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Dispersantfragment newInstance(String param1, String param2) {
        Dispersantfragment fragment = new Dispersantfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_dispersantfragment, container, false);

        Button btnDis = view.findViewById(R.id.btnDis);
        btnDis.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        String routCode = "";
        if(view.getId() == R.id.btnDis)routCode = "HPD-01";

        if (mListener != null) {
            mListener.onButtonClicked(routCode);
        }
    }
}