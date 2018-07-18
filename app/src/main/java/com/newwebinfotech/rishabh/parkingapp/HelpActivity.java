package com.newwebinfotech.rishabh.parkingapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.InvocationTargetException;

public class HelpActivity extends AppCompatActivity {

    WebView webView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        webView = (WebView) findViewById(R.id.webview);
        progressDialog = new ProgressDialog(HelpActivity.this);

        webView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int newProgress) {

                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();

                progressDialog.setProgress(newProgress);

                if (newProgress>=85)
                {
                    progressDialog.dismiss();
                }
                //activity.setProgress(newProgress * 1000);
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.loadUrl("http://parkingeye.in");

    }

    @Override
    protected void onPause() {
        super.onPause();

        webView.loadUrl("about:blank");

        progressDialog.dismiss();
        webView.removeAllViews();

        webView.stopLoading();

        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(webView, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {
        } catch(NoSuchMethodException nsme) {
        } catch(InvocationTargetException ite) {
        } catch (IllegalAccessException iae) {
        }

        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.stopLoading();
        webView.loadUrl("about:blank");

        this.finish();
    }

}
