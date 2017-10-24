package mobi.wonders.apps.android.budrest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.wonders.apps.android.budrest.R;

public class EditApiActivity extends AppCompatActivity {

    private List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();

    private LinearLayout mParameterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_api);

        for (int i = 0; i < 5; i++) {
            ParameterInfo info = new ParameterInfo();
            info.isMust = "是";
            info.mean = "登录信息";
            info.name = "用户名";
            info.style = "String";
            parameters.add(info);
        }

        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改API信息");

        mParameterLayout = (LinearLayout) findViewById(R.id.params);
        addViewToParameter();
    }

    private void addViewToParameter(){
        for (int i = 0; i < parameters.size(); i++) {
            View contentView = LayoutInflater.from(this).inflate(
                    R.layout.item_edit_parameter, null);

            EditText tvParameterName = (EditText) contentView.findViewById(R.id.ed_parameter_name);
            EditText tvParameterIsMust = (EditText) contentView.findViewById(R.id.ed_parameter_is_must);
            EditText tvParameterMean = (EditText) contentView.findViewById(R.id.ed_parameter_mean);
            EditText tvParameterStyle = (EditText) contentView.findViewById(R.id.ed_parameter_style);

            tvParameterIsMust.setText(parameters.get(i).isMust);
            tvParameterName.setText(parameters.get(i).name);
            tvParameterMean.setText(parameters.get(i).mean);
            tvParameterStyle.setText(parameters.get(i).style);

            mParameterLayout.addView(contentView);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                EditApiActivity.this.finish();
                break;
            case R.id.done :
                //TODO 提交API修改
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_project_menu, menu);
        return true;
    }

    class ParameterInfo {
        String name;
        String isMust;
        String style;
        String mean;
    }
}
