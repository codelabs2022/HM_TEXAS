package com.pda.hm_texas.dig;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView; // TextView 사용을 위해 import

import com.pda.hm_texas.R;

public class WarningDialogManager {
    private ValueAnimator colorAnimator;
    private AlertDialog alertDialog;

    public void showBlinkingWarningDialog(Context context, String title, String message) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dig_warning, null);

        LinearLayout blinkingArea = dialogView.findViewById(R.id.blinking_area);
        Button closeButton = dialogView.findViewById(R.id.btn_close);

        // **새로 추가된 로직: 뷰를 찾아서 내용 설정**
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);

        titleTextView.setText("🚨" + title); // 전달받은 타이틀 설정
        messageTextView.setText(message); // 전달받은 메시지 설정
        // ------------------------------------

        // AlertDialog 생성 및 설정 (이하 기존 코드와 동일)
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        alertDialog = builder.create();

        if (alertDialog.getWindow() != null) {
            alertDialog.setCanceledOnTouchOutside(false);
        }

        // 깜빡임 애니메이션 설정 및 시작
        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                Color.parseColor("#FFDADA"),
                Color.parseColor("#FFFFFF"));

        colorAnimator.setDuration(500);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);

        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                blinkingArea.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBlinkingAnimation();
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(dialog -> stopBlinkingAnimation());

        colorAnimator.start();
        alertDialog.show();
    }

    public void stopBlinkingAnimation() {
        if (colorAnimator != null && colorAnimator.isRunning()) {
            colorAnimator.cancel();
        }
    }
}
