package mobi.wonders.apps.android.budrest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.model.ApiChapterModel;

/**
 * Created by qinyuanmao on 15/12/9.
 */
public class ApiChapterAdapter extends BaseAdapter{

    private Context mContext;
    private List<ApiChapterModel> mModels;

    public ApiChapterAdapter(Context context, List<ApiChapterModel> models) {
        mContext = context;
        mModels = models;
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)  {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_api_chapter, null);
            holder.title = (TextView) convertView.findViewById(R.id.tv_chapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(mModels.get(position).getChapter());

        return convertView;
    }

    public class ViewHolder {
        private TextView title;
    }
}
