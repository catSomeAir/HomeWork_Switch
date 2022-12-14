package com.example.last_project.model.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.last_project.CommonAlertActivity;
import com.example.last_project.R;
import com.example.last_project.WebviewActivity;
import com.example.last_project.common.CommonVal;
import com.example.last_project.conn.CommonConn;
import com.example.last_project.model.detail.manual.ManualVO;
import com.example.last_project.search.category_search.CategorySearchVO;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ManualFragment extends Fragment implements OnPageChangeListener, OnLoadCompleteListener {
    CircularProgressIndicator progress_circular;

    GestureDetector detector;
    Context context;
    PDFView pdfView;
    String SAMPLE_FILE = "test.pdf";
    Integer pageNumber = 0;
    String pdfFileName;


    LinearLayout ln_model_detail_writing, ln_model_detail_as;
    ImageView imgv_manual_zzim, imgv_manual_link, imgv_manual_download; //* ???????????? ?????? ????????????

    //????????????
    ImageView imgv_manual_photo;
    TextView tv_manual_brand, tv_manual_model_name, tv_manual_model_code, tv_manual_catg;


    ImageView imgv_manual_help; //????????????
    TextView tv_manual_help; //????????????
    LinearLayout ln_manual_help; //????????????
    TextView tv_manual_help_cnt;    //?????????


    TextView tv_manual_name;
    boolean  help_chk;
    boolean  zzim = true;
    NestedScrollView scrollView;

    //DB??????????????? ????????????
    CategorySearchVO model_info;

    //???????????? ????????? ????????? ??????
    String help = "";

    //?????????
    WebView webView_manual;
    WebViewClient webViewClient;
    ManualVO manual_info;
    public ManualFragment() {
    }

    public ManualFragment(Context context, CategorySearchVO model_info, ManualVO manual_info) {
        this.context = context;
        this.model_info = model_info;
        this.manual_info = manual_info;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manual, container, false);

        //???????????? ??????
//        imgv_manual_download = v.findViewById(R.id.imgv_manual_download);
//        imgv_manual_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(CommonVal.userInfo != null){
//                    //????????? ??????, ????????? ???????????? ????????? ????????? ????????????
//                    CommonConn conn = new CommonConn(getContext(), "download_manual_check");
//                    conn.addParams("email", CommonVal.userInfo.getEmail());
//                    conn.addParams("model_code", model_info.getModel_code());
//                    conn.executeConn(new CommonConn.ConnCallback() {
//                        @Override
//                        public void onResult(boolean isResult, String data) {
//                            if(isResult){
//                                if(data.equals("1")) {//?????????????????? ?????????
//                                    Intent intent = new Intent(getContext(), CommonAlertActivity.class);
//                                    intent.putExtra("page", "ManualFragment_download_exist");
//                                    startActivity(intent);
//                                }else{//?????????????????? ?????????
//                                    Intent intent = new Intent(getContext(), CommonAlertActivity.class);
//                                    intent.putExtra("page", "ManualFragment_download_not_exist");
//                                    startActivity(intent);
//                                }
//
//                            }
//                        }
//                    });
//                }else{
//                    Intent intent = new Intent(getContext(), NotFoundAlertActivity.class);   //????????? ????????? ?????????????????? ????????? ????????????????????????
//                    intent.putExtra("intent_type", "write"); //??????????????? ?????? alert
//                    startActivity(intent);
//                }
//
//            }
//        });
//        webViewClient = new WebViewClient();
//        webView_manual = v.findViewById(R.id.webView_manual);
//
//        webView_manual.setWebViewClient(webViewClient);
//        webView_manual.getSettings().setJavaScriptEnabled(true);
////        webView_manual.loadUrl("http://www.naver.com");
//        webView_manual.loadUrl("http://121.147.215.12:3302/pj/upload/manaulpdf/test.pdf");

        //http://121.147.215.12:3302/pj/model_info?model_code=T18MTH

        //?????? ????????????
        scrollView = v.findViewById(R.id.scrollView);

        //????????? ????????????
        imgv_manual_help = v.findViewById(R.id.imgv_manual_help);
        tv_manual_help = v.findViewById(R.id.tv_manual_help);
        ln_manual_help = v.findViewById(R.id.ln_manual_help);
        tv_manual_help_cnt = v.findViewById(R.id.tv_manual_help_cnt);   //?????? ???
        tv_manual_help_cnt.setText(manual_info.getHelp_cnt());
        //????????????????????? ?????????
        ln_manual_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (help_chk) {   //????????? ???????????? ??????
                    imgv_manual_help.setImageResource(R.drawable.icon_thumbs);
                    tv_manual_help.setText("?????????\n?????????????");
                    tv_manual_help.setTextColor(Color.parseColor("#4D4d4D"));
                    ln_manual_help.setBackgroundResource(R.drawable.shape_navy_border8);
                    tv_manual_help_cnt.setText(Integer.parseInt(tv_manual_help_cnt.getText().toString()) - 1 + "");
                    help_chk = false;
                } else { //??????????????? ??? ??????
                    imgv_manual_help.setImageResource(R.drawable.icon_thumbs_fill_white);
                    tv_manual_help.setText("?????????\n????????????!");
                    tv_manual_help.setTextColor(Color.parseColor("#e9e9e9"));
                    ln_manual_help.setBackgroundResource(R.drawable.shape_navy_fill);
                    tv_manual_help_cnt.setText(Integer.parseInt(tv_manual_help_cnt.getText().toString()) + 1 + "");
                    help_chk = true;
                }
            }
        });

        pdfView = (PDFView) v.findViewById(R.id.pdfView);
        progress_circular = v.findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);
//        new loadpdffromUrl().execute("https://downloadcenter.samsung.com/content/UM/202203/20220303101157093/OSNATSCB-3.2.0_EM_Oscar_Pontus_Nike_Kant-SU2e_KOR_KOR_220216.0.pdf");
        new loadpdffromUrl().execute(manual_info.getFilepath());

//        displayFromAsset(SAMPLE_FILE);

        //????????????, AS?????? -> ????????????????????????
        ln_model_detail_writing = v.findViewById(R.id.ln_model_detail_writing);
        ln_model_detail_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
                tabs.selectTab(tabs.getTabAt(1));
            }
        });
        ln_model_detail_as = v.findViewById(R.id.ln_model_detail_as);
        ln_model_detail_as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
                tabs.selectTab(tabs.getTabAt(2));
            }
        });

        //???????????? ??????????????????, ??????????????? ???????????? n?????? y?????? ??????.
        if (CommonVal.userInfo != null) {
            CommonConn conn = new CommonConn(getContext(), "my_manual_select");
            conn.addParams("email", CommonVal.userInfo.getEmail());
            conn.addParams("model_code", model_info.getModel_code());
            conn.executeConn_no_dialog(new CommonConn.ConnCallback() {
                @Override
                public void onResult(boolean isResult, String data) {
                    if(data.equals("1")){
                        imgv_manual_zzim.setImageResource(R.drawable.icon_heart_fill);
                        zzim = true;
                    }else{
                        imgv_manual_zzim.setImageResource(R.drawable.icon_heart);
                        zzim = false;
                    }

                }
            });

        }


        imgv_manual_zzim = v.findViewById(R.id.imgv_manual_zzim);
        imgv_manual_zzim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zzim) {
                    imgv_manual_zzim.setImageResource(R.drawable.icon_heart);
                    zzim = false;
                    if (CommonVal.userInfo != null) {
                        my_manual_delete();
                        Toast.makeText(getContext(), "?????? ????????? ???????????? ?????? ???????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getContext(), CommonAlertActivity.class);
                        intent.putExtra("page", "manual_zzim");
                        startActivity(intent);
                    }

                } else {//?????? ??????????????? ??? ????????? ?????????
                    imgv_manual_zzim.setImageResource(R.drawable.icon_heart_fill);
                    zzim = true;
                    if (CommonVal.userInfo != null) {
                        my_manual_insert();
                        Toast.makeText(getContext(), "?????? ???????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getContext(), CommonAlertActivity.class);
                        intent.putExtra("page", "manual_zzim");
                        startActivity(intent);
                    }

                    //* DB??? ????????? ??????????????? ?????????
                }
            }
        });

        //????????? ???????????? ????????????
        imgv_manual_photo = v.findViewById(R.id.imgv_manual_photo);
        tv_manual_brand = v.findViewById(R.id.tv_manual_brand);
        tv_manual_model_name = v.findViewById(R.id.tv_manual_model_name);
        tv_manual_model_code = v.findViewById(R.id.tv_manual_model_code);
        tv_manual_catg = v.findViewById(R.id.tv_manual_catg);

        Glide.with(getContext()).load(model_info.getFilepath().replace("localhost", "121.147.215.12:3302").replace("192.168.0.33","121.147.215.12:3302")).into(imgv_manual_photo);
        tv_manual_brand.setText(model_info.getBrand_name());
        tv_manual_model_name.setText(model_info.getModel_name() + " (" + model_info.getModel_code() + ")");
        tv_manual_model_code.setText(model_info.getModel_code());
        tv_manual_catg.setText(model_info.getCategory_name());

        //?????? ?????? ???????????? ????????????
        imgv_manual_link = v.findViewById(R.id.imgv_manual_link);
        String search_keyword = model_info.getBrand_name() + model_info.getModel_name();
        imgv_manual_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("url", "https://www.google.com/search?q=" + search_keyword + "&oq=" + search_keyword);
                startActivity(intent);
            }
        });
        return v;
    }

//    private void displayFromAsset(String assetFileName) {
//        pdfFileName = assetFileName;
//
//        pdfView.fromAsset(SAMPLE_FILE)
//                .defaultPage(pageNumber)
//                .enableSwipe(true)
//                .fitEachPage(true)
//                .pageFling(true)
//                .pageSnap(true)
//                .autoSpacing(true)
//                .swipeHorizontal(true)
//                .onPageChange(this)
//                .enableAnnotationRendering(false)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(getContext()))
//                .load();
//
//        if (pdfView.isSwipeEnabled()) {
//            pdfView.setSwipeEnabled(true);
//            scrollView.requestDisallowInterceptTouchEvent(true);
//        }
//
////        pdfView.setOnTouchListener(new View.OnTouchListener() {
////
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////
////                if(MotionEvent.ACTION_DOWN == event.getAction()){
////                    Log.d("??????", "onTouch: ?????? ");
////                    scrollView.requestDisallowInterceptTouchEvent(true);
////                }else{
////                    Log.d("??????", "onTouch: ?????? ");
//////                    scrollView.requestDisallowInterceptTouchEvent(true);
////
////                }
////                return true;
////            }
////        });
//        detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onShowPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                return false;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                return false;
//            }
//        });
//    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        pdfView.fitToWidth(nbPages);
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        getActivity().setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("TAG", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonVal.userInfo != null) {
            CommonConn conn = new CommonConn(getContext(), "my_manual_select");
            conn.addParams("email", CommonVal.userInfo.getEmail());
            conn.addParams("model_code", model_info.getModel_code());
            conn.executeConn_no_dialog(new CommonConn.ConnCallback() {
                @Override
                public void onResult(boolean isResult, String data) {
                    help = data;

                }
            });

            if (help.equals("n")) {
                imgv_manual_zzim.setImageResource(R.drawable.icon_heart);
            } else if (help.equals("y")) {
                imgv_manual_zzim.setImageResource(R.drawable.icon_heart_fill);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void my_manual_insert(){
        if (CommonVal.userInfo != null) {
            CommonConn conn = new CommonConn(getContext(), "my_manual_insert");
            conn.addParams("email", CommonVal.userInfo.getEmail());
            conn.addParams("model_code", model_info.getModel_code());
            conn.executeConn_no_dialog(new CommonConn.ConnCallback() {

                @Override
                public void onResult(boolean isResult, String data) {

                }
            });
        }
    }

    public void my_manual_delete(){
        if (CommonVal.userInfo != null) {
            CommonConn conn = new CommonConn(getContext(), "my_manual_delete");
            conn.addParams("email", CommonVal.userInfo.getEmail());
            conn.addParams("model_code", model_info.getModel_code());
            conn.executeConn_no_dialog(new CommonConn.ConnCallback() {

                @Override
                public void onResult(boolean isResult, String data) {

                }
            });
        }
    }



    public class loadpdffromUrl extends AsyncTask<String, Void, InputStream> implements OnLoadCompleteListener, OnErrorListener {
        @Override
        protected InputStream doInBackground(String... strings) {
            //We use InputStream to get PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // if response is success. we are getting input stream from url and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                //method to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            //after the executing async task we load pdf in to pdfview.
            pdfView.fromStream(inputStream)
                    .defaultPage(pageNumber)
                    .enableSwipe(true)
                    .fitEachPage(true)
                    .pageFling(true)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .swipeHorizontal(true)
                    .onPageChange(ManualFragment.this::onPageChanged)
                    .enableAnnotationRendering(false)
                    .onLoad(this)
                    .onError(this)
                    .load();

        }
        @Override
        public void loadComplete(int nbPages) {
            progress_circular.setVisibility(View.GONE);
        }
        @Override
        public void onError(Throwable t) {
            progress_circular.setVisibility(View.GONE);
            Toast.makeText(getContext(),"Error:" +t.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}