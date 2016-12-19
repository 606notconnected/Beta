package com.example.asdf.test.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asdf.test.R;
import com.example.asdf.test.attached.iClick;

import java.util.List;
import java.util.Map;


/**
 * Created by Useradmin on 2016/11/15.
 */
public class friendListView extends BaseAdapter{
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private iClick mListener;
    public static  int num;
    public  friendListView(Context context,List<Map<String, Object>> data,iClick listener){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        mListener = listener;
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    class Zujian{
        //            public ImageView enter;
        public TextView friendName;
        //            public TextView entertext;
        public ImageView examplePicture;
        public TextView exampletext;
        public Button love;
        public TextView lovetext;
        public LinearLayout click;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     *
     */

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Zujian zujian;
        if(convertView!=null)            {
            zujian=(Zujian)convertView.getTag();
        }
        else {
            zujian=new Zujian();
            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.friend_list, null);
            zujian.friendName= (TextView) convertView.findViewById(R.id.friendName);
            zujian.click= (LinearLayout) convertView.findViewById(R.id.click);
            zujian.examplePicture = (ImageView) convertView.findViewById(R.id.examplePicture);
            zujian.exampletext = (TextView) convertView.findViewById(R.id.exampleText);
            zujian.love = (Button) convertView.findViewById(R.id.love);
            zujian.lovetext = (TextView) convertView.findViewById(R.id.lovetext);
        }
//
        convertView.setTag(zujian);
        //绑定数据
//        zujian.enter.setBackgroundResource((Integer)data.get(position).get("enter"));
        zujian.love.setBackgroundResource((Integer)data.get(position).get("love"));
        zujian.examplePicture.setBackgroundDrawable((Drawable) data.get(position).get("examplePicture"));
        zujian.friendName.setText((String) data.get(position).get("friendName"));
//        zujian.entertext.setText("进入主页");
        zujian.lovetext.setText("取消关注");
        zujian.exampletext.setText((String) data.get(position).get("exampletext"));
        zujian.click.setOnClickListener(mListener);
        zujian.click.setTag(position);
        return convertView;
    }
}