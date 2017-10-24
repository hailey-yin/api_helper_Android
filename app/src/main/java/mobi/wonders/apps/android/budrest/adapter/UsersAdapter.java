package mobi.wonders.apps.android.budrest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

import bean.UserDb;
import carbon.widget.RelativeLayout;
import carbon.widget.TextView;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.activity.LoginActivity;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.model.UserModel;
import mobi.wonders.apps.android.budrest.utils.DES3Utils;

/**
 * Title:[多用户的adapter]
 * Description:[]
 * @author jiangguangming
 * @date 2015/11/25
 */
public class UsersAdapter extends BaseAdapter{

    private Context context;
    private List<UserDb> userModels;

    public UsersAdapter(Context context, List<UserDb> userModels) {
        this.context = context;
        this.userModels = userModels;
    }

    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public Object getItem(int position) {
        return userModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null)  {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_usersadapter, null);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.delete = (RelativeLayout) convertView.findViewById(R.id.rel_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(DES3Utils.decryptMode(userModels.get(position).getUsername()));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2015/12/30 数据库修改
                DBManager.getInstance().delUserInfoByName(userModels.get(position).getUsername());
                userModels.remove(position);
                if (userModels.size() == 0) {
                    LoginActivity.instance.mUsersWindow.dismiss();
                    LoginActivity.instance.mRelMuluser.setVisibility(View.GONE);

                }
                UsersAdapter.this.notifyDataSetChanged();

            }
        });

        return convertView;
    }

    public class ViewHolder {
        private TextView username;
        private RelativeLayout delete;
    }
}
