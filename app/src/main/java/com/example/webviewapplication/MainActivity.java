package com.example.webviewapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;



class WebAppInterface {
  Context mContext;

  /** Instantiate the interface and set the context */
  WebAppInterface(Context c) {
    mContext = c;
  }

  /** Show a toast from the web page */
  @JavascriptInterface
  public void showToast(String toast) {
    Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
  }
}

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    WebView mWebView = (WebView) findViewById(R.id.webview);
    WebSettings set = mWebView.getSettings();
    set.setJavaScriptEnabled(true);  // 支持javascript

    mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

//
    mWebView.setWebViewClient(new WebViewClient() {
      /**·
       *
       * @return 本地jquery
       */
      private WebResourceResponse editResponse() {
//        Log.i(TAG, "editResponse: ", getAssets());
        try {
          return new WebResourceResponse("application/x-javascript", "utf-8", getAssets().open("jquery.js"));
        } catch (IOException e) {
          e.printStackTrace();
        }
        //需处理特殊情况
        return null;
      }
      @Override
      public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (Build.VERSION.SDK_INT < 21) {
          if (url.contains("jquery")) {
            return editResponse();
          }
        }
        return super.shouldInterceptRequest(view, url);
      }
      @Override
      public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= 21) {
          String url = request.getUrl().toString();

          if (!TextUtils.isEmpty(url) && url.contains("jquery")) {
            return editResponse();
          }
        }
        return super.shouldInterceptRequest(view, request);
      }
    });
//    myWebView.loadUrl("http://www.example.com");

    mWebView.loadUrl("http://172.18.25.15:9000/index.html");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}