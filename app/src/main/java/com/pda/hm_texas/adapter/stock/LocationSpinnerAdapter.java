package com.pda.hm_texas.adapter.stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pda.hm_texas.R;
import com.pda.hm_texas.dto.LocationDTO;

import java.util.List;

public class LocationSpinnerAdapter extends ArrayAdapter<LocationDTO> {

    private Context context;
    private List<LocationDTO> locationList;

    public LocationSpinnerAdapter(Context context, List<LocationDTO> locationList) {
        super(context, android.R.layout.simple_spinner_item, locationList);
        this.context = context;
        this.locationList = locationList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sploc_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tvlocName);
        textView.setText(locationList.get(position).getName()); // DTO의 name 필드를 스피너 항목에 표시
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sploc_dropitem, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tvSpDropItem);
        textView.setText(locationList.get(position).getName());
        return convertView;
    }
}
