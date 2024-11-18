package com.pda.hm_texas.components;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import com.pda.hm_texas.views.MainActivity;

import java.io.File;

public class FileDownloader {
    private static long downloadId;
    private static DownloadManager downloadManager;
    private static final String FILE_NAME = "STOCK.apk"; // 다운로
    private static final String PACKAGE_NAME = "com.example.houimyungstock"; // 설치된 앱의 패키지명

    public static void downloadFile(Context context, String fileUrl, ProgressDialog progressDialog) {
        // 기존 앱 설치 여부 확인 후 삭제 요청

        deleteExistingFile();
        // 다운로드 매니저 초기화
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // 다운로드 요청 생성
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle("Sample File Download")
                .setDescription("Downloading file...")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FILE_NAME)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        // 파일 다운로드 요청 실행
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request);
            Toast.makeText(context, "다운로드를 시작했습니다.", Toast.LENGTH_SHORT).show();

            // 다운로드 상태를 업데이트하기 위해 브로드캐스트 리시버 등록
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        Toast.makeText(context, "다운로드 완료", Toast.LENGTH_SHORT).show();
//                        statusTextView.setText("다운로드 완료");
//                        progressBar.setProgress(100);

                        if(progressDialog.isShowing())progressDialog.dismiss();
                        if (isAppInstalled(context, PACKAGE_NAME)) {
                            uninstallApp(context, PACKAGE_NAME);
                            return;
                        }

                        context.unregisterReceiver(this);
                    }
                }
            }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            // 다운로드 상태 업데이트를 위해 새로운 스레드 실행
            new Thread(() -> updateDownloadStatus(context)).start();
        } else {
            Toast.makeText(context, "다운로드 관리자 접근 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true; // 앱이 설치되어 있음
        } catch (PackageManager.NameNotFoundException e) {
            return false; // 앱이 설치되어 있지 않음
        }
    }

    private static void uninstallApp(Context context, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
        uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }

    private static void deleteExistingFile() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
    private static void installApk(Context context) {
        Uri apkUri = Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+FILE_NAME);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            apkUri = androidx.core.content.FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new java.io.File(apkUri.getPath()));
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean canInstall = context.getPackageManager().canRequestPackageInstalls();
            if (!canInstall) {
                Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + context.getPackageName()));
                context.startActivity(permissionIntent);
                return;
            }
        }

        context.startActivity(intent);
    }

    private static void updateDownloadStatus(Context context) {
        boolean downloading = true;

        while (downloading) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = downloadManager.query(query);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                @SuppressLint("Range") long bytesDownloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                @SuppressLint("Range") long bytesTotal = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                } else if (status == DownloadManager.STATUS_FAILED) {
                    downloading = false;
                    ((MainActivity) context).runOnUiThread(() -> {
                        //statusTextView.setText("다운로드 실패");
                        Toast.makeText(context, "다운로드 실패", Toast.LENGTH_SHORT).show();
                    });
                }

                // 진행률 업데이트
                final int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                ((MainActivity) context).runOnUiThread(() -> {
                    //progressBar.setProgress(progress);
                    //statusTextView.setText("다운로드 중: " + progress + "%");
                });
                cursor.close();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

