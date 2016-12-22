package com.example.asdf.test.adapter;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;

        import com.example.asdf.test.R;
        import com.example.asdf.test.attached.tmpBean;
        import com.squareup.picasso.Picasso;

        import java.util.List;

/**
 * Created by huangshuai on 2016
 * Email：huangshuai@wooyun.org
 * 列表Adapter
 */
public class tmpAdapter extends BaseAdapter {

    private List<tmpBean> list_sun;
    private LayoutInflater inflater;
    private Context context;

    public tmpAdapter(List<tmpBean> list_new, Context context) {
        this.list_sun = list_new;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_sun.size();
    }

    @Override
    public Object getItem(int position) {
        return list_sun.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemViewHodler viewHolder;
        if (convertView == null) {
            viewHolder = new ItemViewHodler();

            convertView = inflater.inflate(R.layout.list_image, null);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.item_pic);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHodler) convertView.getTag();
        }

        Picasso.with(context).load(list_sun.get(position).getPicUrl()).fit()
                .placeholder(R.drawable.undownload)
                .error(R.drawable.undownload)//mapBitmap
                .into(viewHolder.pic);


        return convertView;
    }


    class ItemViewHodler {
        ImageView pic;
    }
}
