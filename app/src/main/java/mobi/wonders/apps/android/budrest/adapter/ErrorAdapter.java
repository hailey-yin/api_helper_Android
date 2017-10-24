package mobi.wonders.apps.android.budrest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobi.wonders.apps.android.budrest.R;

/**
 * @author ZL
 * @class ErrorAdapter
 * @brief
 * @date 2015/12/30.
 */
public class ErrorAdapter extends BaseAdapter {
    private List<String> data;
    private Context context;

    public ErrorAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_test,null);
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imgview);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(data.get(position));

        return convertView;
    }

    class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }
}
