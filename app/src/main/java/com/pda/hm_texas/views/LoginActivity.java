package com.pda.hm_texas.views;

import static com.pda.hm_texas.helper.FileDownloader.downloadFile;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.pda.hm_texas.R;
import com.pda.hm_texas.dig.ProgressDialog;
import com.pda.hm_texas.dto.AppVersionDTO;
import com.pda.hm_texas.dto.UserDTO;
import com.pda.hm_texas.helper.LoginUser;
import com.pda.hm_texas.helper.RetorfitHelper;
import com.pda.hm_texas.helper.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    private Context mContext;
    private InputMethodManager imm;
    private ProgressDialog progressDialog;
    private String NowAppVersion = "";

    private TextInputEditText etLoginID, etLoginPW;
    private Button btnLogin;
    private CheckBox chkSaveID, chkTest;

    private static final int REQUEST_CODE_PERMISSION = 1001;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mContext = this;
        chkSaveID = findViewById(R.id.chkSaveID);
        chkTest = findViewById(R.id.chkTest);
        btnLogin = findViewById(R.id.btnLogin);

        chkSaveID.setOnClickListener(this::onClick);
        chkTest.setOnClickListener(this::onClick);
        btnLogin.setOnClickListener(this);

        etLoginID = findViewById(R.id.edLoginID);
        etLoginPW = findViewById(R.id.etLoginPW);

        etLoginID.setOnKeyListener(this);
        etLoginPW.setOnKeyListener(this);

        SharedPreferences sharedPreferences= getSharedPreferences("HOIMYUNGAPI", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        String saveid = sharedPreferences.getString("SAVE_ID", "");
        String isTest = sharedPreferences.getString("IS_TEST", "N");

        if(isTest.equals("N"))
        {
            chkTest.setChecked(false);
            RetorfitHelper.USE_URL =  RetorfitHelper.LIVE_URL;
        }
        else {
            chkTest.setChecked(true);
            RetorfitHelper.USE_URL =  RetorfitHelper.TEST_URL;
        }

        if(!saveid.isEmpty())
        {
            chkSaveID.setChecked(true);
            etLoginID.setText(saveid);
        }

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        progressDialog = new ProgressDialog(LoginActivity.this, "PROCESSING....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        NowAppVersion = Utility.getInstance().getVersion(this);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        CheckVersion();
    }
    private void CheckVersion()
    {
        progressDialog = new ProgressDialog(this, "Check Version.....");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        try
        {
            Call<AppVersionDTO> appversion = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).getVersion("TEXAS");

            appversion.enqueue(new Callback<AppVersionDTO>() {
                @Override
                public void onResponse(Call<AppVersionDTO> call, Response<AppVersionDTO> response) {
                    //if(progressDialog.isShowing())progressDialog.dismiss();
                    if(!NowAppVersion.equals(response.body().getVERSION()))
                    {
                        //RequestPermission();
                        if(chkTest.isChecked())downloadFileViaBrowser(mContext, RetorfitHelper.USE_URL +"pda/getTESTAPK?pgtype=TEXAS_TEST");
                        else downloadFileViaBrowser(mContext, RetorfitHelper.USE_URL +"pda/getAPK?pgtype=TEXAS");
                    }
                    else{
                        if(progressDialog.isShowing())progressDialog.dismiss();
//                        Intent i = new Intent(mContext, MainActivity.class);
//                        startActivity(i);
//                        finish();
                        Toast.makeText(LoginActivity.this, "This is the latest version.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<AppVersionDTO> call, Throwable t) {
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    Toast.makeText(mContext, "Version check failed.", Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch (Exception ex){
            if(progressDialog.isShowing())progressDialog.dismiss();
            Utility.getInstance().showDialogWithBlinkingEffect("Check Version", ex.getMessage(), mContext);
        }
    }

    public void downloadFileViaBrowser(Context context, String url) {

        try {
            // 1. URL 문자열을 Uri 객체로 파싱합니다.
            Uri uri = Uri.parse(url);

            // 2. ACTION_VIEW 액션을 가진 Intent를 생성합니다.
            // ACTION_VIEW는 시스템에게 해당 데이터를 '본다'는 행위를 요청하며,
            // URL의 경우 기본적으로 브라우저가 이 Intent를 처리합니다.
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // 3. Intent를 실행합니다.
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            // 사용자의 기기에 URL을 처리할 수 있는 앱(브라우저)이 없을 경우
            Toast.makeText(context, "브라우저를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // 기타 오류 처리
            Toast.makeText(context, "파일 다운로드 요청 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void RequestPermission(){


        if (shouldShowRequestPermissionRationale(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
            // 권한을 요청해야 하는 경우 처리
            requestPermission();
        } else {
            // 권한이 이미 부여된 경우 다운로드 실행
            startDownload();
        }
    }

    private void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_PERMISSION);
    }

    private void startDownload() {
        // 다운로드 시작 함수 호출
        downloadFile(this, RetorfitHelper.USE_URL +"pda/getAPK?pgtype=TEXAS", progressDialog);
    }

    public void CheckSaveState(View view) {
        if (etLoginID.getText().toString().isEmpty()) { // 공백 또는 size=0이면
            Toast.makeText(this, "Please Input ID", Toast.LENGTH_SHORT).show();
        } else {
            if (chkSaveID.isChecked()) {
                SharedPreferences sharedPreferences = getSharedPreferences("HOIMYUNGAPI", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SAVE_ID", etLoginID.getText().toString());
                editor.apply();
                //Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("HOIMYUNGAPI", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("SAVE_ID");
                editor.apply();
            }
        }
    }

    public void CheckTest(){

        SharedPreferences sharedPreferences = getSharedPreferences("HOIMYUNGAPI", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(chkTest.isChecked())
        {
            RetorfitHelper.USE_URL =  RetorfitHelper.TEST_URL;
            editor.putString("IS_TEST", "Y");
        }
        else{
            RetorfitHelper.USE_URL =  RetorfitHelper.LIVE_URL;
            editor.putString("IS_TEST", "N");

        }
        editor.apply();
    }

    private void LoginProc() throws Exception {
        progressDialog.show();
        if(etLoginID.getText().toString().equals("") || etLoginID.getText() == null){
            if( progressDialog.isShowing() == true) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("LOGIN", "Please enter your ID.", mContext);
            return;
        }

        if(etLoginPW.getText().toString().equals("") || etLoginPW.getText() == null){
            if( progressDialog.isShowing() == true) progressDialog.dismiss();

            Utility.getInstance().showDialogWithBlinkingEffect("LOGIN", "Please enter your PASSWORD.", mContext);
            return;
        }


        String id = etLoginID.getText().toString();
        String pw = etLoginPW.getText().toString();
        String ip = Utility.getInstance().getLocalIpAddress();

        if(!id.toUpperCase().equals("ADMIN")) pw = Utility.getInstance().encrypt(pw);


        if(progressDialog.isShowing()) progressDialog.dismiss();


        try{

            Call<UserDTO> login = RetorfitHelper.getApiService(RetorfitHelper.USE_URL).Login(id, pw);

            login.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                    if(response.body() != null)
                    {
                        if(TextUtils.isEmpty(response.body().getRTN_MSGNO()))
                        {
                            LoginUser.getInstance().setUser(response.body());
                            Intent i = new Intent(mContext, MenuActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Utility.getInstance().showDialogWithBlinkingEffect("Login", "Contact your administrator", mContext);
                        }
                    }
                    else{
                        Utility.getInstance().showDialogWithBlinkingEffect("Login", "Communication with the server failed.", mContext);
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialogWithBlinkingEffect("Login", "Communication with the server failed.", mContext);
                }
            });
        }catch (Exception e){
            if(progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.getInstance().stopBlinkingAnimation();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

        // super.onBackPressed();
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLogin){
            try {
                LoginProc();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(v.getId() == R.id.chkSaveID){
            CheckSaveState(v);
        }
        else if(v.getId() == R.id.chkTest){
            CheckTest();
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == KeyEvent.KEYCODE_ENTER)
        {
            if(view.getId() == R.id.edLoginID){
                etLoginPW.requestFocus();
            }
            else if(view.getId() == R.id.etLoginPW){
                imm.hideSoftInputFromWindow(etLoginPW.getWindowToken(), 0);
            }

            return true;
        }

        return false;
    }
}