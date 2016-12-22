package com.example.asdf.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asdf.test.attached.activityCollector;
import com.example.asdf.test.attached.baseActivity;

/**
 * Created by Useradmin on 2016/10/23.
 */
public class set extends baseActivity {
    private ImageView leftDrawer;
    private TextView information;
    private TextView modifyPassword;
    private TextView exit;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        leftDrawer= (ImageView) findViewById(R.id.leftdrawer);
        information= (TextView) findViewById(R.id.information);
        modifyPassword= (TextView) findViewById(R.id.modifyPassword);
        exit= (TextView) findViewById(R.id.exit);
        leftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.this.finish();
            }
        });
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(set.this, modifyinformation.class));
            }
        });
        modifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(set.this,modifyPassword.class));
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.exit(0);
                activityCollector.finishAll();
            }
        });
    }
}