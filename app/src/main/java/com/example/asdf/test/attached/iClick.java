package com.example.asdf.test.attached;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class iClick implements OnClickListener {
    @Override
    public void onClick(View v) {
        listViewItemClick((Integer) v.getTag(), v);
    }

    public abstract void listViewItemClick(int position, View v);

}
