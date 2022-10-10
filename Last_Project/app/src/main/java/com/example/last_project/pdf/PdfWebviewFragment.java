package com.example.last_project.pdf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.example.last_project.R;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

public class PdfWebviewFragment extends Fragment implements DownloadFile.Listener {

    WebView pdf_webView;
    String url;
    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;
    public PdfWebviewFragment(String url) {
        this.url = url;
            }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_pdf_webview, container, false);
//        pdf_webView = v.findViewById(R.id.pdf_webView);
//        pdf_webView.setWebViewClient(new WebViewClient());
//
//        pdf_webView.getSettings().setJavaScriptEnabled(true);
//        pdf_webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);

        remotePDFViewPager =
                new RemotePDFViewPager(getContext(), url, this);

        return v;
    }

    @Override
    public void onSuccess(String url, String destinationPath) {

//        adapter = new PDFPagerAdapter(getContext(), "AdobeXMLFormsSamples.pdf");
//        remotePDFViewPager.setAdapter(adapter);
        getActivity().setContentView(remotePDFViewPager);
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}