package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnWebView;

    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnWebView = (Button) findViewById(R.id.btnWebView);
        btnWebView.setOnClickListener(this);

        ivImage = (ImageView) findViewById(R.id.ivImage);

        String code = splitCode(exampleURL);

        Log.e("==============",code);

        Glide.with(this)
                .load("http://img3.imgtn.bdimg.com/it/u=895009738,1542646259&fm=21&gp=0.jpg")
                .into(ivImage);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWebView:
                skip2WebView();
                break;
        }
    }

    private void skip2WebView() {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }



    private  String  exampleURL = "https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA&state=xyz";

    private String splitCode(String codeURL){
        String code = null;
        String[] split = codeURL.split("&");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if(str.contains("code=")){
                String[] codeSplit = str.split("=");
                code = codeSplit[1];
                return  code;
            }
        }
        return code;
    }

}
