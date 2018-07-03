package com.shindygo.shindy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PaymentPageActivity extends AppCompatActivity {
    private static final String TAG = PaymentPageActivity.class.getSimpleName();

    public static final String HTML_DATA = "html_data";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        String htmlString = getIntent().getExtras().getString(HTML_DATA, "");
        //data == html data which you want to load
        WebView webview = (WebView)this.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cmsg)
            {

                /* process JSON */
                String msg =cmsg.message();
                Log.d(TAG, "onConsoleMessage : " + msg);
                return true;

            }
        });

        webview.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Log.e("Load  page", description);
                Toast.makeText(
                        PaymentPageActivity.this,
                        "Problem loading. Make sure internet connection is available.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //view.loadUrl("javascript:console.log(document.body.getElementsByTagName('pre')[0].innerHTML);");
            }
        });

        webview.loadDataWithBaseURL("", htmlString, "text/html", "UTF-8", "");
    }
}
