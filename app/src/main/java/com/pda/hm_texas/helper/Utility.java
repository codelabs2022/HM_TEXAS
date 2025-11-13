package com.pda.hm_texas.helper;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.pda.hm_texas.R;
import com.pda.hm_texas.event.OnMsgBoxClickListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utility {

    private static Utility utility;

    private static Utility Instance = null;

    public static synchronized Utility getInstance(){
        if(Instance == null)
        {
            Instance = new Utility();
        }

        return Instance;
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ValueAnimator colorAnimator;
    private AlertDialog alertDialog;

    public void showDialog(String title, String msg , Context context) {

        ContextThemeWrapper cw = new ContextThemeWrapper( context, androidx.appcompat.R.style.AlertDialog_AppCompat );
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                    }
                });

        AlertDialog msgDlg = msgBuilder.show();
        TextView tvmsg = msgDlg.findViewById(android.R.id.message);
        tvmsg.setTextSize(25.0f);
        msgDlg.show();
    }

    public void showDialogCallBack(String title, String msg , Context context, OnMsgBoxClickListener listener) {

        ContextThemeWrapper cw = new ContextThemeWrapper( context, androidx.appcompat.R.style.AlertDialog_AppCompat );
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.OnConfirm();
                    }
                });

        AlertDialog msgDlg = msgBuilder.show();
        TextView tvmsg = msgDlg.findViewById(android.R.id.message);
        tvmsg.setTextSize(25.0f);
        msgDlg.show();
    }

    public void showDialogByConfirm(String title, String msg , Context context, OnMsgBoxClickListener listener) {

        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.OnCancle();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.OnConfirm();
                    }
                });

        AlertDialog msgDlg = msgBuilder.create();
//        TextView tvmsg = msgDlg.findViewById(android.R.id.message);
//        assert tvmsg != null;
//        tvmsg.setTextSize(20.0f);
        msgDlg.show();
    }

    public void stopBlinkingAnimation() {
        if (colorAnimator != null && colorAnimator.isRunning()) {
            colorAnimator.cancel();
        }
    }

    public void showDialogWithBlinkingEffect(String title, String message, Context context) {

        // 애니메이션이 이미 실행 중이라면 중지
        stopBlinkingAnimation();

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dig_warning, null);

        LinearLayout blinkingArea = dialogView.findViewById(R.id.blinking_area);
        Button closeButton = dialogView.findViewById(R.id.btn_close);
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);

        // 내용 설정
        titleTextView.setText("🚨 " +title);
        messageTextView.setText(message);

        // AlertDialog 생성 및 설정 (생략된 세부 설정은 이전 답변 참고)
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog currentDialog = builder.create();

        if (currentDialog.getWindow() != null) {
            currentDialog.setCanceledOnTouchOutside(false);
        }

        // 깜빡임 애니메이션 설정 및 시작
        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                Color.parseColor("#FFDADA"), Color.parseColor("#FFFFFF"));
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
                currentDialog.dismiss();

            }
        });

        currentDialog.setOnDismissListener(dialog -> stopBlinkingAnimation());

        colorAnimator.start();
        currentDialog.show();
    }

    public String getToday(){
        LocalDate now = LocalDate.now();         // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatedNow = now.format(formatter);

        return formatedNow;
    }

    public String getMonthOfFirstDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = simpleDateFormat.format(calendar.getTime());

        return startDate;
    }

    public String encrypt(String text) throws Exception {

        String key = "chaeumit2021";
        // MD5 해시 생성
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5Key = md5.digest(key.getBytes("UTF-8"));

        // Triple DES 키 설정
        SecretKey secretKey = new SecretKeySpec(md5Key, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

        // 암호화 모드 설정
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // 텍스트를 바이트로 변환하고 암호화
        byte[] textBytes = text.getBytes("UTF-8");
        byte[] encryptedBytes = cipher.doFinal(textBytes);

        // Base64로 인코딩하여 반환
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    public boolean CheckNetWork(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }
    public static String getVersion(Context context) {
        String versionName = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName + "";
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isNumber(String strValue) {
        return strValue != null && strValue.matches("[-+]?\\d*\\.?\\d+");
    }

    public int isPastDate(String oldDate, String newDate){
        int res = 0;
        try {
            Date date1 = new Date(dateFormat.parse(oldDate).getTime());
            Date date2 = new Date(dateFormat.parse(newDate).getTime());

            //compareTo메서드를 통한 날짜비교
            int compare = date1.compareTo(date2);

            //조건문
            if(compare > 0) {
                System.out.println("oldDate가 newDate보다 큽니다.(oldDate > newDate)");
                res = -1;
            }else if(compare < 0) {
                System.out.println("oldDate가 newDate보다 작습니다.(oldDate < newDate)");
                res = 1;
            }else {
                System.out.println("oldDate와 newDate가 같습니다.(oldDate = newDate)");
                res = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }
}
