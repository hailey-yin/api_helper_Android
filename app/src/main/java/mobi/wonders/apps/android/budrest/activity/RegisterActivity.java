package mobi.wonders.apps.android.budrest.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import org.apache.commons.lang.StringUtils;

import carbon.widget.Button;
import carbon.widget.TextView;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.utils.DES3Utils;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;

public class RegisterActivity extends AppCompatActivity {

    private ScrollView mScView;//包含布局，适配小屏幕

    private Button mBtnRegister;//注册按钮
    private TextView mTvProtocol;//budrest协议
    private EditText mEdtUserName;
    private EditText mEdtPwd;
    private EditText mEdtConfirmPwd;
    private String mUserName;
    private String mPwd;
    private String mConfirmPwd;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("注册");
        mScView = (ScrollView) findViewById(R.id.scrollView);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mTvProtocol = (TextView) findViewById(R.id.tv_protocol);
        mEdtUserName = (EditText) findViewById(R.id.register_username);
        mEdtPwd = (EditText) findViewById(R.id.register_pwd);
        mEdtConfirmPwd = (EditText) findViewById(R.id.register_confirm_pwd);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
//        deleteFocus(mScrollView);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = mEdtUserName.getText().toString().trim();
                mPwd = mEdtPwd.getText().toString().trim();
                mConfirmPwd = mEdtConfirmPwd.getText().toString().trim();
                if (StringUtils.isEmpty(mUserName)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog = DialogUtil.setAnimDialog(alertDialog);
                    alertDialog.setMessage("用户名不能为空");
                    alertDialog.show();
                }else if (StringUtils.isEmpty(mPwd)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog = DialogUtil.setAnimDialog(alertDialog);
                    alertDialog.setMessage("请输入密码");
                    alertDialog.show();
                }else if (StringUtils.isEmpty(mConfirmPwd)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog = DialogUtil.setAnimDialog(alertDialog);
                    alertDialog.setMessage("请输入确认密码");
                    alertDialog.show();
                }else if(!mPwd.equals(mConfirmPwd)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog = DialogUtil.setAnimDialog(alertDialog);
                    alertDialog.setMessage("两次输入的密码不同");
                    alertDialog.show();
                    mEdtConfirmPwd.setText("");
                } else {
                    register(mUserName,mPwd);
                }

            }
        });
        mTvProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle(getResources().getString(R.string.protocol))
                        .setMessage(getResources().getString(R.string.protocol_content))
                        .setPositiveButton(getResources().getString(R.string.know), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                Window window = alertDialog.getWindow();
                window.setWindowAnimations(R.style.dialogWindowAnim);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });
    }

    private void deleteFocus(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Activity activity = RegisterActivity.this;
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View view1 = ((ViewGroup) view).getChildAt(i);
                deleteFocus(view1);
            }
        }
    }

    private void register(final String mUserName, final String mPwd) {

        APIProvider.getInstance().register(mUserName, mPwd, new APICallBack<RestMsg>() {
            @Override
            public void onSuccess(RestMsg data) {
                AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this).create();
                dialog = DialogUtil.setAnimDialog(dialog);
                dialog.setMessage("注册成功");
                dialog.show();

                MyApplication.isLogin = true;
                MyApplication.currentUser = mUserName;
                //TODO:保存用户名和密码到数据库中

                LoginActivity.instance.finish();
                finish();
            }

            @Override
            public void onFailure(RestMsg data) {
                AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this).create();
                dialog = DialogUtil.setAnimDialog(dialog);
                dialog.setMessage(data.getMessage());
                dialog.show();
                mEdtUserName.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
