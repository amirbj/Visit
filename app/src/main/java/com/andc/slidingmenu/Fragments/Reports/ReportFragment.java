package com.andc.slidingmenu.Fragments.Reports;
/*
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;
import com.andc.slidingmenu.R;


public class ReportFragment extends Fragment {
    public int type;
    public View rootView;
    public WebView localWebView;
    private WebViewReportManager jsInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report, container, false);
        setHasOptionsMenu(false);

        ArrayList<ReportItemDB> items = new ArrayList<ReportItemDB>();
        items.addAll(ReportItemDB.listAll(ReportItemDB.class));
        jsInterface = new WebViewReportManager(rootView.getContext());
        localWebView = (WebView) rootView.findViewById(R.id.webview);
        WebSettings webSettings = localWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        localWebView.loadUrl(getString(R.string.html_path) + getString(type) );
        localWebView.setPadding(0, 0, 0, 0);
        localWebView.addJavascriptInterface(jsInterface, "WebViewReportManager");

        return rootView;
    }

}
*/