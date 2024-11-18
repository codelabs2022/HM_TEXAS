package com.pda.hm_texas.dig;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pda.hm_texas.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(@NonNull Context context, String State) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dig_progress);
        TextView tvSTATE = (TextView) findViewById(R.id.tvState);
        tvSTATE.setText(State);
    }
}
