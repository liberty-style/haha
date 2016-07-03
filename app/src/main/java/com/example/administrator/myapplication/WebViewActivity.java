package com.example.administrator.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewActivity extends Activity {
    private static final int TIMEOUT_MS = 15000;

    private WebView webView;
    private String authorizeURL = "http://www.oschina.net/action/oauth2/authorize?response_type=code&client_id=cBztymAD3m4c00bQCKbs&redirect_uri=http://www.zhoumo.com";

    private String authorizeCode;

    private String authorizeToken = "http://www.oschina.net/action/openapi/token?client_id=cBztymAD3m4c00bQCKbs&client_secret=9MHajf7U2X1obmFFqN1EvMmi8AhYV6PL&grant_type=authorization_code&redirect_uri=http://www.zhoumo.com&dataType=json&code=";

    private String urlNewslist = "https://www.oschina.net/action/openapi/news_list?access_token=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.wvView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.loadUrl(authorizeURL);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                authorizeCode = splitCode(url);

                Log.e("code = ",""+authorizeCode);
                requestToken();

                return true;
            }
        });

    }

    private void requestToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = authorizeToken + authorizeCode;
                try {
                    String  result = get(url);
                    JSONObject jObj = new JSONObject(result);
                    String token = jObj.getString("access_token");

                    Log.e("token",token);
                    String  newsList  = get(urlNewslist+token);

                    Log.e("------------",newsList);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private String splitCode(String codeURL) {
        String code = null;
        String[] split = codeURL.split("&");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (str.contains("code=")) {
                String[] codeSplit = str.split("=");
                code = codeSplit[1];
                return code;
            }
        }
        return code;
    }


    private String get(String urlString) throws IOException {
        StringBuilder builder = new StringBuilder();
        // String urlString = "https://www.baidu.com/";
        // 1.准备URL
        URL url = new URL(urlString);
        // 2.获取HttpURLConnection 对象
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 3.设置请求方式和超时时间等
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        // 设置请求方式为GET请求
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        // 4.获取到请求返回的输入流
        InputStream inputStream = connection.getInputStream();
        // 5.读取输入流里的具体数据
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            builder.append(new String(buffer, 0, len));
        }
        // 6.关闭连接
        connection.disconnect();
        return builder.toString();
    }

    private HttpURLConnection openConnection(String url) throws IOException {
        // 获得URL对象
        URL parseURL = new URL(url);
        // 获得HttpURLConnection对象
        HttpURLConnection connection = (HttpURLConnection) parseURL.openConnection();
        // 设置连接超时
        connection.setConnectTimeout(TIMEOUT_MS);
        // 设置读取超时时间
        connection.setReadTimeout(TIMEOUT_MS);
        // 不使用缓存
        connection.setUseCaches(false);
        // 设置是否从httpUrlConnection读入，默认情况下是true;
        connection.setDoInput(true);
        // 设置httpUrlConnection是否可以输出数据，默认情况下是false;
        connection.setDoOutput(true);
        return connection;
    }


}
