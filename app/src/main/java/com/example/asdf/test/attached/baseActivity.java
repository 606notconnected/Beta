package com.example.asdf.test.attached;

import android.app.Activity;
import android.os.Bundle;

import com.example.asdf.test.attached.activityCollector;

/**
 * Created by Useradmin on 2016/11/6.
 */
public class baseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        activityCollector.removeActivity(this);
    }
}
