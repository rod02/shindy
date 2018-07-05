package com.shindygo.shindy;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;



import com.shindygo.shindy.model.OpenEdgeResponse;

public class PaymentPageActivity extends AppCompatActivity {
    private static final String TAG = PaymentPageActivity.class.getSimpleName();

    private static final String VALIDATE_TAG = "<input id=\"validate\" name=\"validate\" type=\"hidden\" value=\"true\"/>";
    private static final String INVALIDATE_TAG = "<input id=\"validate\" name=\"validate\" type=\"hidden\" value=\"false\"/>";
    //private static final String URL_PAYPAGE_HOST = "https://ws.test.paygateway.com/HostPayService/v1/hostpay/paypage/";
    public static final String HTML_DATA = "html_data";
    LinearLayout layProgress;
    OpenEdgeResponse openEdgeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        String htmlString = getIntent().getExtras().getString(HTML_DATA, "");
        layProgress = (LinearLayout) this.findViewById(R.id.lay_progress);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //data == html data which you want to load
        final WebView webview = (WebView)this.findViewById(R.id.webview);
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
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                Log.d(TAG, error.toString());
                handler.proceed(); // Ignore SSL certificate errors
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //view.loadUrl("javascript:console.log(document.body.getElementsByTagName('pre')[0].innerHTML);");
                try {
                    layProgress.setVisibility(View.GONE);

                    Log.d(TAG,"onPageFinished "+ url);
                    if(url.equalsIgnoreCase("about:blank")) return;

                    // Log.d(TAG,"view "+ view.getContentDescription());
                    webview.evaluateJavascript(
                            "(function() { return (document.getElementById('fullResponse').value); })();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String html) {
                                    Log.d(TAG, "onReceiveValue: " + html);
                                    try{
                                        openEdgeResponse = OpenEdgeResponse.fromForm(html);
                                        Log.d(TAG, "onReceiveValue:response " + openEdgeResponse.getResponseCodeText());
                                        Log.d(TAG, "onReceiveValue:response " + openEdgeResponse.getResponseCode());

                                        showAlert(openEdgeResponse.getResponseCodeText());
                                    }catch (NullPointerException e){

                                    }catch (ArrayIndexOutOfBoundsException e){

                                    }


                                }
                            });
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            webview.scrollTo(0,0);

                        }
                    }, 500);
                }catch (NullPointerException e){

                }

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                return false;
            }
        });
      //  webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        // Enable Javascript
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webview.setWebContentsDebuggingEnabled(true);
        String newHtml = htmlString.replace(VALIDATE_TAG, INVALIDATE_TAG);
        webview.loadDataWithBaseURL("", newHtml, "text/html", "UTF-8", "");
    }
    void showAlert(String message){
        AlertDialog.Builder builder =  new AlertDialog.Builder(PaymentPageActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.homeAsUp || id==android.R.id.home) {
            try {
                if(openEdgeResponse!=null){
                    if(!openEdgeResponse.isRetryRecommended()){
                        setResult(openEdgeResponse.getResponseCode().equals("1")? Activity.RESULT_OK : Activity.RESULT_CANCELED);
                        finish();
                        return true;
                    }
                }
            }catch (NullPointerException e){

            }

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
