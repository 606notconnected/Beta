package com.example.asdf.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
/**
 * Created by on 15/6/4.
 */


public class MainActivityInstrumentationTest extends ActivityInstrumentationTestCase2<mainView> {

    private Context ctx;

    public MainActivityInstrumentationTest() {
        super(mainView.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ctx = getActivity().getApplicationContext();
    }

    public void testStart() {
        Intent intent = new Intent(ctx, mainView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        Log.i("TestActivity", "................startActivity............................");
    }
}