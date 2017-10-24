package mobi.wonders.apps.android.budrest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import android.widget.TextView;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.model.ApiSearchModel;

/**
 * Title:[搜索APi的adapter]
 * Description:[]
 * @author qinyuanmao
 * @date 2015/12/8
 */
public class SearchApiAdapter extends BaseAdapter{

    private Context context;
    private List<ApiSearchModel.ResultEntity> models;

    public SearchApiAdapter(Context context, List<ApiSearchModel.ResultEntity> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null)  {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_api, null);
            holder.title = (TextView) convertView.findViewById(R.id.tv_api_title);
            holder.bigTitle = (TextView) convertView.findViewById(R.id.tv_api_big_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bigTitle.setText(models.get(position).getGname());
        holder.title.setText(models.get(position).getName());

        return convertView;
    }

    public class ViewHolder {
        private TextView title;
        private TextView bigTitle;
    }
}
