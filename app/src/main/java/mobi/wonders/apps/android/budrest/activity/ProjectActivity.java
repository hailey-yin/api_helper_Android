package mobi.wonders.apps.android.budrest.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.adapter.ApiChapterAdapter;
import mobi.wonders.apps.android.budrest.adapter.ApiTitleAdapter;
import mobi.wonders.apps.android.budrest.adapter.SearchApiAdapter;
import mobi.wonders.apps.android.budrest.model.ApiChapterModel;
import mobi.wonders.apps.android.budrest.model.ApiSearchModel;

public class ProjectActivity extends BaseActivity {

    private Toolbar toolbar;
    private PopupWindow mSearchWindow;
    private List<ApiChapterModel> models;
    private LinearLayout mLayoutChooseApi;
    //private TextView mTVChooseApi;
    private TextView mTogApi;
    private TextView mFeedBack;
    private PopupWindow mChapterWindow;
    private PopupWindow mTestWindow;
    private FloatingActionButton mFab;
    private LinearLayout mParameterLayout;
    private List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        //初始化假数据
        models = new ArrayList<ApiChapterModel>();
        for (int i = 0; i < 5; i++) {
            ApiChapterModel model = new ApiChapterModel();
            model.setChapter("用户模块");
            List<ApiChapterModel.Title> lists = new ArrayList<ApiChapterModel.Title>();
            for(int j = 0; j < 7; j++){
                ApiChapterModel.Title title = new ApiChapterModel.Title();
                title.setContent("用户注册");
                lists.add(title);
            }
            model.setTitles(lists);
            models.add(model);

            ParameterInfo info = new ParameterInfo();
            info.isMust = "是";
            info.mean = "登录信息";
            info.name = "用户名";
            info.style = "String";
            parameters.add(info);
        }

        initView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                ProjectActivity.this.finish();
                break;
            case R.id.search_api :
                showSearchWindow();
                break;
            case R.id.edit_api : {
                Intent intent = new Intent(ProjectActivity.this, EditApiActivity.class);
                startActivity(intent);
            }break;
            case R.id.test_api : {
                Intent intent = new Intent(ProjectActivity.this, ApiTestActivity.class);
                startActivity(intent);
            }break;
            case R.id.setting_api :
                Intent intent = new Intent(ProjectActivity.this, ManageProjectActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_menu, menu);
        return true;
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("项目详情");

        mParameterLayout = (LinearLayout) findViewById(R.id.params);
        mTogApi = (TextView) findViewById(R.id.tog_api);
        mLayoutChooseApi = (LinearLayout) findViewById(R.id.layout_choose_api);
        //mTVChooseApi = (TextView) findViewById(R.id.tv_choose_api);
        mTogApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChapterWindow();
            }
        });
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTestWindow();
            }
        });
        addViewToParameter();
    }

    private void addViewToParameter(){
        for (int i = 0; i < parameters.size(); i++) {
            View contentView = LayoutInflater.from(ProjectActivity.this).inflate(
                    R.layout.item_layout_parameter, null);

            TextView tvParameterName = (TextView) contentView.findViewById(R.id.tv_parameter_name);
            TextView tvParameterIsMust = (TextView) contentView.findViewById(R.id.tv_parameter_is_must);
            TextView tvParameterMean = (TextView) contentView.findViewById(R.id.tv_parameter_mean);
            TextView tvParameterStyle = (TextView) contentView.findViewById(R.id.tv_parameter_style);

            tvParameterIsMust.setText(parameters.get(i).isMust);
            tvParameterName.setText(parameters.get(i).name);
            tvParameterMean.setText(parameters.get(i).mean);
            tvParameterStyle.setText(parameters.get(i).style);

            mParameterLayout.addView(contentView);
        }

    }

    private void showSearchWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(ProjectActivity.this).inflate(
                R.layout.popup_search_api, null);
        // 设置按钮的点击事件
        ListView apiList = (ListView) contentView.findViewById(R.id.search_list);

        ImageView mIBCancelSearch = (ImageView) contentView.findViewById(R.id.cancel_search);
        mIBCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchWindow.dismiss();
            }
        });

        List<ApiSearchModel.ResultEntity> models = new ArrayList<ApiSearchModel.ResultEntity>();
        ApiSearchModel.ResultEntity model;
        for(int i = 0; i < 5; i++){
            model = new ApiSearchModel.ResultEntity();
            model.setGname("用户模块");
            model.setName("用户注册");
            models.add(model);
        }
        SearchApiAdapter adapter = new SearchApiAdapter(this, models);

        apiList.setAdapter(adapter);

        mSearchWindow = new PopupWindow(contentView,
                carbon.widget.LinearLayout.LayoutParams.MATCH_PARENT, carbon.widget.LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mSearchWindow.setTouchable(true);

        mSearchWindow.setBackgroundDrawable(new BitmapDrawable());
        mSearchWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mSearchWindow.setAnimationStyle(R.style.PopupAnimation);
        mSearchWindow.showAtLocation(toolbar, 0, 0, 0);
        mSearchWindow.update();
    }

    private void showChapterWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_api_chapter, null);
        // 设置按钮的点击事件
        ListView chapterList = (ListView) contentView.findViewById(R.id.lv_chapter);
        final ListView titleList = (ListView) contentView.findViewById(R.id.lv_title);

        ApiChapterAdapter adapter = new ApiChapterAdapter(this, models);

        chapterList.setAdapter(adapter);

        chapterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApiTitleAdapter titleAdapter = new ApiTitleAdapter(ProjectActivity.this, models.get(position).getTitles());
                titleList.setAdapter(titleAdapter);
            }
        });

        mChapterWindow = new PopupWindow(contentView,
                carbon.widget.LinearLayout.LayoutParams.MATCH_PARENT, carbon.widget.LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mChapterWindow.setTouchable(true);

        mChapterWindow.setBackgroundDrawable(new BitmapDrawable());
        mChapterWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mChapterWindow.setAnimationStyle(R.style.PopupAnimation);
        mChapterWindow.showAsDropDown(mLayoutChooseApi);
        mChapterWindow.update();

    }

    private void showTestWindow(){
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(ProjectActivity.this).inflate(
                R.layout.dialog_api_test, null);
        // 设置按钮的点击事件
        ListView usersList = (ListView) contentView.findViewById(R.id.search_list);

        TextView cancel = (TextView) contentView.findViewById(R.id.btn_back);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestWindow.dismiss();
            }
        });

        List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();

        List<ApiSearchModel.ResultEntity> models = new ArrayList<ApiSearchModel.ResultEntity>();
        ApiSearchModel.ResultEntity model;
        for(int i = 0; i < 5; i++){
            model = new ApiSearchModel.ResultEntity();
            model.setGname("用户模块");
            model.setName("用户注册");
            models.add(model);
        }
        SearchApiAdapter adapter = new SearchApiAdapter(this, models);

        usersList.setAdapter(adapter);

        mTestWindow = new PopupWindow(contentView,
                carbon.widget.LinearLayout.LayoutParams.MATCH_PARENT, carbon.widget.LinearLayout.LayoutParams.MATCH_PARENT, true);

        mTestWindow.setTouchable(true);

        mTestWindow.setBackgroundDrawable(new BitmapDrawable());
        mTestWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mTestWindow.setAnimationStyle(R.style.PopupAnimation);
        mTestWindow.showAsDropDown(toolbar, 0, 0);
        mTestWindow.update();

    }

    class ParameterInfo {
        String name;
        String isMust;
        String style;
        String mean;
    }

}
