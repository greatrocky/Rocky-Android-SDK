package com.luoji.pay;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

import com.luoji.pay.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by lixueqing on 2016/12/30
 */

public class LjMainActivity extends AppCompatActivity {
    private WebView tbsContent;
    private static String host = "http://rocky.zhuopu.net:81/api/auth/open_login?";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title6+
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        Intent intent = this.getIntent();
        String appid = intent.getStringExtra("appid");
        String appkey = intent.getStringExtra("appkey");
        String userid = intent.getStringExtra("userid");
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sDateFormat.format(new java.util.Date());
        String turl = "appid=" + appid + "&ts=" + date + "&userid=" + userid + appkey;
        String md5 = MD5.stringToMD5(turl);
        url = host + "userid=" + userid + "&appid=" + appid + "&ts=" + date + "&sign=" + md5;
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        tbsContent = (com.tencent.smtt.sdk.WebView) findViewById(R.id.tbsContent);
        initWebViewSettings();
        tbsContent.loadUrl(url);
//        tbsContent.loadUrl("file:///android_asset/webpage/jsToJava.html");


        tbsContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        tbsContent.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {
            }

            /**
             * java 调用 js方法 并且 传值
             * 步骤：1、调用 js函数  2、js回调一个android方法得到参数  3、js处理函数
             * @return
             */
            @JavascriptInterface
            public void onSubmit(String s) {

                try {
                    JSONObject js = new JSONObject(s).getJSONObject("data");
                    int status=js.getInt("status");
                    EventBus.getDefault().post(new BaseEvent(status));
                    if(status==1){
                        LjMainActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            /**
             * 关闭当前的窗口
             */
            @JavascriptInterface
            public void closeCurrentWindow() {
                LjMainActivity.this.finish();
            }
        }, "Android");

    }

    private void initWebViewSettings() {
        WebSettings webSetting = tbsContent.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && tbsContent.canGoBack()) {
            tbsContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
