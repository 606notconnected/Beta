package com.example.asdf.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asdf.test.R;
import com.example.asdf.test.attached.iClick;

import java.util.List;
import java.util.Map;
public class commentListview  extends BaseAdapter{
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private iClick mListener;
    public static  int num;
    public  commentListview(Context context,List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    class Zujian{
        public TextView name;
        public TextView commen;
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
            convertView = layoutInflater.inflate(R.layout.commentlist, null);
            zujian.name= (TextView) convertView.findViewById(R.id.name);
//            ForegroundColorSpan redSpan = new  ForegroundColorSpan(0xFF000000);
//            SpannableStringBuilder builder = new  SpannableStringBuilder(zujian.name.getText().toString());
//            builder.setSpan(redSpan,  0,  8,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            zujian.commen = (TextView) convertView.findViewById(R.id.commen);
        }
//
        convertView.setTag(zujian);
        //绑定数据
        zujian.commen.setText((String) data.get(position).get("name"));
//        zujian.entertext.setText("进入主页");
        zujian.name.setText((String) data.get(position).get("commen"));
        return convertView;
    }
}