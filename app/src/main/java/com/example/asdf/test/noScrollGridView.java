package com.example.asdf.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class noScrollGridView extends GridView {

    public noScrollGridView(Context context) {
        super(context);

    }

    public noScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}