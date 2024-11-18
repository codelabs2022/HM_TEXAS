package com.pda.hm_texas.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScanReceiver extends BroadcastReceiver {

    private OnScanListener mOnScanListener;
    public void SetOnScanListener(OnScanListener mListener)
    {
        mOnScanListener = mListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //String ScanData = intent.getExtras().getString("data");

        String ScanData = intent.getExtras().getString("com.symbol.datawedge.data_string");

        if(ScanData != null)
        {
            if(mOnScanListener != null)mOnScanListener.OnScan(ScanData);
        }
    }
}
