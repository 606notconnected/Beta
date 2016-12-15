package com.example.asdf.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class mainActivity extends Activity{
    private Button login;//登录按钮
    private Button register;//注册按钮
    private TextView forgetPassword;//忘记密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login= (Button) findViewById(R.id.login);
        register= (Button) findViewById(R.id.register);
         forgetPassword= (TextView) findViewById(R.id.forgetPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainActivity.this,mainView.class));
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainActivity.this,forgetPassword.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainActivity.this,register.class));
            }
        });
    }
}