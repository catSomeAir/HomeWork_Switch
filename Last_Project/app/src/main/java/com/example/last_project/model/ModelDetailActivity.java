
            package com.example.last_project.model;

    import android.Manifest;
    import android.app.DownloadManager;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.content.IntentFilter;
    import android.content.pm.PackageManager;
    import android.database.Cursor;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Environment;
    import android.os.Handler;
    import android.os.Message;
    import android.os.StrictMode;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;

    import com.example.last_project.CommonAlertActivity;
    import com.example.last_project.R;
    import com.example.last_project.common.CommonVal;
    import com.example.last_project.conn.CommonConn;
    import com.example.last_project.model.detail.ManualFragment;
    import com.example.last_project.model.detail.as.AfterServiceFragment;
    import com.example.last_project.model.detail.manual.ManualVO;
    import com.example.last_project.model.detail.writng.WritingFragment;
    import com.example.last_project.search.NotFoundAlertActivity;
    import com.example.last_project.search.category_search.CategorySearchVO;
    import com.google.android.material.tabs.TabLayout;
    import com.google.gson.Gson;

    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.MalformedURLException;
    import java.net.URL;
    import java.util.List;

    public class ModelDetailActivity extends AppCompatActivity implements View.OnClickListener {

        //?????????????????????
        String File_Name = "???????????? ????????? ?????????";
        String File_extend = "????????????";

        String fileURL = "http://121.147.215.12:3302/pj/download_manual"; // URL
        String Save_Path;
        String Save_folder = "/my_manual";

        ProgressBar loadingBar;
//    DownloadThread dThread;

        //--------------------------------------


        ImageView imgv_back;
        //    LinearLayout ln_model_detail_writing;
        TabLayout tabs;

        TextView tv_detail_cancel;
        CategorySearchVO model_info;
        ManualVO manual_info;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_model_detail);
            checkDangerousPermissions();

            model_info = (CategorySearchVO) getIntent().getSerializableExtra("model_info");
            manual_info = (ManualVO) getIntent().getSerializableExtra("manual_info");

//
//        CommonConn conn = new CommonConn(ModelDetailActivity.this, "manual_info");
//        conn.addParams("model_code", model_info.getModel_code());
//        conn.executeConn_no_dialog(new CommonConn.ConnCallback() {
//            @Override
//            public void onResult(boolean isResult, String data) {
//                manualVO = new Gson().fromJson(data, ManualVO.class);
//                CommonVal.manualInfo = manualVO;
//                File_Name = manualVO.getFilename();
//                File_extend = manualVO.getFilename().substring(manualVO.getFilename().indexOf(".")+1);
//                fileURL = fileURL+"?filename="+manualVO.getFilename() + "&filepath="+manualVO.getFilepath().replace("http://","");
//            }
//        });

            File_Name = manual_info.getFilename();
            File_extend = manual_info.getFilename().substring(manual_info.getFilename().indexOf(".")+1);
            fileURL ="http://121.147.215.12:3302/pj/upload/manual/test.pdf";//"http://172.24.96.1/mid/download_manual?filename=test.pdf&filepath=D:/app/file/test.pdf";

            //????????????????????? ???????????? ????????? ????????????


            //??????????????? ????????????
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());



            loadingBar = (ProgressBar) findViewById(R.id.Loading);  //?????????

            // ???????????? ????????? ??????????????? ????????? ?????? ????????? ???.
            String ext = Environment.getExternalStorageState();
            if (ext.equals(Environment.MEDIA_MOUNTED)) {
                Save_Path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + Save_folder;
            }

            imgv_back = findViewById(R.id.imgv_back);

            imgv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            tabs = findViewById(R.id.tabs);
            tabs.addTab(tabs.newTab().setText("???????????????").setTag(1));
            tabs.addTab(tabs.newTab().setText("????????????").setTag(2));
            tabs.addTab(tabs.newTab().setText("A/S??????").setTag(3));
            tabs.getTabAt(0).select();


            //????????? ?????? ????????? Fragment?????????
            getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new ManualFragment(ModelDetailActivity.this, model_info, manual_info)).commit();

            //??????????????? ?????? ?????????
            tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if (tab.getPosition() == 0) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new ManualFragment(ModelDetailActivity.this, model_info, manual_info)).commit();
                    } else if (tab.getPosition() == 1) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new WritingFragment(model_info)).commit();
                    } else if (tab.getPosition() == 2) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new AfterServiceFragment(model_info)).commit();

                        //* ???????????????
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new UnrelatedASFragment()).commit();//* ????????? AS?????? ?????? ?????? ???????????? ?????? ????????????

                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        }

        @Override
        protected void onResume() {
            super.onResume();

            IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            registerReceiver(downloadCompleteReceiver, completeFilter);

            if (tabs.getSelectedTabPosition() == 0) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new ManualFragment(ModelDetailActivity.this, model_info, manual_info)).commit();
            } else if (tabs.getSelectedTabPosition() == 1) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new WritingFragment(model_info)).commit();
            } else if (tabs.getSelectedTabPosition() == 2) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new AfterServiceFragment(model_info)).commit();

                //* ???????????????
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container_model_detail, new UnrelatedASFragment()).commit();//* ????????? AS?????? ?????? ?????? ???????????? ?????? ????????????

            }

        }

        @Override
        protected void onPause() {
            super.onPause();
            unregisterReceiver(downloadCompleteReceiver);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgv_manual_download) {

                if (CommonVal.userInfo != null) {
                    //????????? ??????, ????????? ???????????? ????????? ????????? ????????????
                    CommonConn conn = new CommonConn(ModelDetailActivity.this, "download_manual_check");
                    conn.addParams("email", CommonVal.userInfo.getEmail());
                    conn.addParams("model_code", model_info.getModel_code());
                    conn.executeConn(new CommonConn.ConnCallback() {
                        @Override
                        public void onResult(boolean isResult, String data) {
                            if (isResult) {
                                if (data.equals("1")) {//?????????????????? ?????????
                                    Intent intent = new Intent(ModelDetailActivity.this, CommonAlertActivity.class);
                                    intent.putExtra("page", "ManualFragment_download_exist");
                                    startActivityForResult(intent,0);
                                } else {//?????????????????? ?????????
                                    Intent intent = new Intent(ModelDetailActivity.this, CommonAlertActivity.class);
                                    intent.putExtra("page", "ManualFragment_download_not_exist");
                                    startActivityForResult(intent,0);
                                }

                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(ModelDetailActivity.this, NotFoundAlertActivity.class);   //????????? ????????? ?????????????????? ????????? ????????????????????????
                    intent.putExtra("intent_type", "write"); //??????????????? ?????? alert
                    startActivity(intent);
                }

            }

        }

 /*   //???????????? ??????
    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL imgurl;
            int Read;
            try {
                imgurl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) imgurl
                        .openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file);
                for (; ; ) {
                    Read = is.read(tmpByte);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }
                is.close();
                fos.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);
        }
    }

    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            loadingBar.setVisibility(View.GONE);
            // ?????? ???????????? ?????? ??? ???????????? ????????? ???????????????.
            showDownloadFile();
        }

    };*/

        private void showDownloadFile() {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            File file = new File(Save_Path + "/" + File_Name);

            // ?????? ????????? ?????? mime type ????????? ??????.
            if (File_extend.equals("mp3")) {
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
            } else if (File_extend.equals("mp4")) {
                intent.setDataAndType(Uri.fromFile(file), "vidio/*");
            } else if (File_extend.equals("jpg") || File_extend.equals("jpeg")
                    || File_extend.equals("JPG") || File_extend.equals("gif")
                    || File_extend.equals("png") || File_extend.equals("bmp")) {
                intent.setDataAndType(Uri.fromFile(file), "image/*");
            } else if (File_extend.equals("txt")) {
                intent.setDataAndType(Uri.fromFile(file), "text/*");
            } else if (File_extend.equals("doc") || File_extend.equals("docx")) {
                intent.setDataAndType(Uri.fromFile(file), "application/msword");
            } else if (File_extend.equals("xls") || File_extend.equals("xlsx")) {
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.ms-excel");
            } else if (File_extend.equals("ppt") || File_extend.equals("pptx")) {
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.ms-powerpoint");
            } else if (File_extend.equals("pdf")) {
//            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setAction(Intent.ACTION_VIEW).setData(Uri.parse(fileURL));
            }
            startActivity(intent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == 2000) { //??????????????? ???????????????????????????
                FileDownTask task = new FileDownTask(this , Uri.parse(fileURL));
                task.execute();
                // URLDownloading(Uri.parse(fileURL));
                //            String File_Name = "???????????? ????????? ?????????";
//            String File_extend = "????????????";
//
//            String fileURL = "????????? ??? ????????? ?????? ??????"; // URL
//            String Save_Path;
//            String Save_folder = "/mydown";

          /*  File dir = new File(Save_Path);
            // ????????? ???????????? ?????? ?????? ????????? ??????
            if (!dir.exists()) {
                dir.mkdir();
            }

            // ???????????? ????????? ????????? ???????????? ??????????????? ????????????
            // ????????? ???????????? ????????? ?????? ?????? ????????????.
            if (new File(Save_Path + "/" + File_Name).exists() == false) {
                loadingBar.setVisibility(View.VISIBLE);
                dThread = new DownloadThread(fileURL*//* + "/" + File_Name*//*,
                        Save_Path + "/" + File_Name);
                dThread.start();
            } else {
                showDownloadFile();
            }*/

            }
        }
        /*
            public void manual_Info(){
                //????????? ?????? filename, filepath, ????????? ??? ????????????
                CommonConn conn = new CommonConn(ModelDetailActivity.this, "manual_info");
                conn.addParams("model_code", model_info.getModel_code());
                conn.executeConn_no_dialog(new CommonConn.ConnCallback() {
                    @Override
                    public void onResult(boolean isResult, String data) {
                        manual_info = new Gson().fromJson(data, ManualVO.class);
                        CommonVal.manualInfo = manual_info;
                        File_Name = manual_info.getFilename();
                        File_extend = manual_info.getFilename().substring(manual_info.getFilename().indexOf(".")+1);
                       // fileURL = fileURL+"?filename="+manual_info.getFilename() + "&filepath="+manual_info.getFilepath().replace("http://","");
                    }
                });
            }*/
        private DownloadManager mDownloadManager;
        private Long mDownloadQueueId;
        private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if(mDownloadQueueId == reference){
                    DownloadManager.Query query = new DownloadManager.Query();  // ???????????? ?????? ????????? ????????? ?????? ??????
                    query.setFilterById(reference);
                    Cursor cursor = mDownloadManager.query(query);

                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);

                    int status = cursor.getInt(columnIndex);
                    int reason = cursor.getInt(columnReason);

                    cursor.close();

                    switch (status) {
                        case DownloadManager.STATUS_SUCCESSFUL :
                            Toast.makeText(ModelDetailActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            break;

                        case DownloadManager.STATUS_PAUSED :
                            Toast.makeText(ModelDetailActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            break;

                        case DownloadManager.STATUS_FAILED :
                            Toast.makeText(ModelDetailActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        };
        public class FileDownTask extends AsyncTask<String , String , String>{
            Context context ;
            Uri uri;
            public FileDownTask(Context context , Uri uri) {
                this.context = context;
                this.uri = uri ;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected String doInBackground(String... strings) {
                URLDownloading(uri);
                return null;
            }





            @RequiresApi(api = Build.VERSION_CODES.N)
            private void URLDownloading(Uri url) {
                if (mDownloadManager == null) {
                    mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                }

                String outputFilePath = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS + "/myManual/") + manual_info.getFilename();
                File outputFile = new File(outputFilePath);
                // if (!outputFile.getParentFile().exists()) {
                //     outputFile.getParentFile().mkdirs();
                // }

                DownloadManager.Request request = new DownloadManager.Request(url);

                // List<String> pathSegmentList = downloadUri.getPathSegments();
                request.setTitle(manual_info.getFilename());
                request.setDescription("test");
                request.setDestinationUri(Uri.fromFile(outputFile));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setAllowedOverMetered(true);
                request.setAllowedOverRoaming(true);
                request.setRequiresCharging(false);
                mDownloadQueueId = mDownloadManager.enqueue(request);
            }


        }

        private void checkDangerousPermissions() {
            String[] permissions = {
                    Manifest.permission.ACCESS_MEDIA_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            int permissionCheck = PackageManager.PERMISSION_GRANTED;
            for (int i = 0; i < permissions.length; i++) {
                permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {  //????????? ?????? ->????????? ??????
                    break;
                }
            }

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "?????? ??????", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "?????? ??????", Toast.LENGTH_LONG).show();

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    Toast.makeText(this, "?????? ?????? ?????????.", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 1);

                }
            }
        }

    }