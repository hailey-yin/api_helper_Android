package mobi.wonders.apps.android.budrest.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import carbon.widget.RelativeLayout;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.cache.DataCleanManager;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;
import mobi.wonders.apps.android.budrest.utils.FileUtil;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mLayoutHelp;
    private RelativeLayout mLayoutDocument;
    private RelativeLayout mLayoutVersion;
    private RelativeLayout mLayoutClearCache;
    private TextView mBtnExit;

    private TextView mTvCache;//显示缓存大小
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SettingActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        double beforecache = FileUtil.getFileOrFilesSize("/data/data/mobi.wonders.apps.android.budrest/databases/BudRest_Android.db", 1)
                + FileUtil.getFileOrFilesSize("/data/data/mobi.wonders.apps.android.budrest/cache", 1);

        if(beforecache > 65*1024){
            mTvCache.setVisibility(View.VISIBLE);
            mTvCache.setText(DataCleanManager.getFormatSize(beforecache));
        }else{
            mTvCache.setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");

        mLayoutHelp = (RelativeLayout) findViewById(R.id.layout_help);
        mLayoutDocument = (RelativeLayout) findViewById(R.id.layout_document);
        mLayoutVersion = (RelativeLayout) findViewById(R.id.layout_version);
        mLayoutClearCache = (RelativeLayout) findViewById(R.id.layout_clear_cache);
        mBtnExit = (TextView) findViewById(R.id.exit_login);

        mTvCache = (TextView) findViewById(R.id.text_cachesize);

        mLayoutHelp.setOnClickListener(this);
        mLayoutDocument.setOnClickListener(this);
        mLayoutVersion.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
        mLayoutClearCache.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_help:
                // TODO: 2015/11/18
                break;
            case R.id.layout_document:
                // TODO: 2015/11/18
                break;
            case R.id.layout_version:
                // TODO: 2015/11/18
                break;
            case R.id.layout_clear_cache:

                boolean isSuccess = DBManager.getInstance().delAll();

                // TODO: 2015/12/29 数据库测试完后删除旧代码

                DataCleanManager.cleanInternalCache(SettingActivity.this);
                //TODO 待增加
                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
                dialog = DialogUtil.setAnimDialog(dialog);
                dialog.setMessage("缓存已清除！");
                dialog.show();
                double aftercache = FileUtil.getFileOrFilesSize("/data/data/mobi.wonders.apps.android.budrest/databases/BudRest_Android.db", 1)
                        + FileUtil.getFileOrFilesSize("/data/data/mobi.wonders.apps.android.budrest/cache", 1);
                if (aftercache > 65*1024){
                    mTvCache.setVisibility(View.VISIBLE);
                    mTvCache.setText(DataCleanManager.getFormatSize(aftercache));
                }else{
                    mTvCache.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.exit_login:
                if (MyApplication.isLogin) {
                    SharedPreferences sp = getSharedPreferences(LoginActivity.TAG, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(LoginActivity.USER, null);
                    editor.putString(LoginActivity.PWD, null);
                    editor.putString(LoginActivity.UID, null);
                    editor.putBoolean(LoginActivity.AUTO_LOGIN, false);
                    MyApplication.currentUser = null;
                    MyApplication.isLogin = false;


                    //实现退出的api，清空服务器session
                    APIProvider.getInstance().logout(new APICallBack<RestMsg>() {
                        @Override
                        public void onSuccess(RestMsg data) {

                        }

                        @Override
                        public void onFailure(RestMsg data) {

                        }
                    });

                    //不在网络接口中跳转是为了快速跳转，
                    // 因为不管是否调用退出接口，本地的账号都会退出，调用接口是为了清除服务器端的session
                    SettingActivity.this.setResult(1, new Intent());

                    finish();
                }
                break;
        }

    }
}
