package com.pda.hm_texas.views;

import static com.pda.hm_texas.helper.FileDownloader.downloadFile;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
    private CheckBox chkSaveID;

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
        btnLogin = findViewById(R.id.btnLogin);

        chkSaveID.setOnClickListener(this::onClick);
        btnLogin.setOnClickListener(this);

        etLoginID = findViewById(R.id.edLoginID);
        etLoginPW = findViewById(R.id.etLoginPW);

        etLoginID.setOnKeyListener(this);
        etLoginPW.setOnKeyListener(this);

        SharedPreferences sharedPreferences= getSharedPreferences("HOIMYUNGAPI", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        String saveid = sharedPreferences.getString("SAVE_ID", "");

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
                        RequestPermission();
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
            Utility.getInstance().showDialog("Check Version", ex.getMessage(), mContext);
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
    private void LoginProc() throws Exception {
        progressDialog.show();
        if(etLoginID.getText().toString().equals("") || etLoginID.getText() == null){
            if( progressDialog.isShowing() == true) progressDialog.dismiss();

            Utility.getInstance().showDialog("LOGIN", "Please enter your ID.", mContext);
            return;
        }

        if(etLoginPW.getText().toString().equals("") || etLoginPW.getText() == null){
            if( progressDialog.isShowing() == true) progressDialog.dismiss();

            Utility.getInstance().showDialog("LOGIN", "Please enter your PASSWORD.", mContext);
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
                            Utility.getInstance().showDialog("Login", "Contact your administrator", mContext);
                        }
                    }
                    else{
                        Utility.getInstance().showDialog("Login", "Communication with the server failed.", mContext);
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                    Utility.getInstance().showDialog("Login", "Communication with the server failed.", mContext);
                }
            });
        }catch (Exception e){
            if(progressDialog.isShowing()) progressDialog.dismiss();
        }
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