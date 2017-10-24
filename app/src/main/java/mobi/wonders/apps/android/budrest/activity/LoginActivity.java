package mobi.wonders.apps.android.budrest.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import bean.UserDb;
import carbon.widget.Button;
import carbon.widget.CheckBox;
import carbon.widget.ImageView;
import carbon.widget.RelativeLayout;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.adapter.UsersAdapter;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.utils.DES3Utils;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;
import mobi.wonders.apps.android.budrest.utils.LogUtil;

public class LoginActivity extends AppCompatActivity {

    //用于在注册页面关闭登录页面
    public static LoginActivity instance;

    private Intent intent;//用于返回数据

    public static String TAG = "login";
    public static String USER = "user";
    public static String PWD = "password";
    public static String UID = "uid";
    public static String AUTO_LOGIN = "autologin";

    private Toolbar toolbar;

    private TextView mTVToRegister;//跳转到注册页面

    public PopupWindow mUsersWindow;

    public EditText mEdtUserName;
    public EditText mEdtPwd;
    private CheckBox mCheckBox;
    private Button mBtnLogin;

    public static RelativeLayout mRelMuluser;//下拉显示曾经登录的用户
    private ImageView mImgUserArrow;
    public static CardView mCVUsers;
    View contentView;//说用户现实view

    private boolean isAUtoLogin ;

    private List<UserDb> users = new ArrayList<UserDb>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        users = DBManager.getInstance().getUserInfo();

        //0表示登录，1表示切换账号
        int tag = getIntent().getIntExtra("tag", 0);
        if (tag == 0){
            instance = this;
            initView();
            //TODO:获取缓存数据
        } else {
            initView();
            getSupportActionBar().setTitle("切换账号");
            mTVToRegister.setVisibility(View.GONE);
            //TODO:获取缓存数据
        }

    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("登录");

        mTVToRegister = (TextView) findViewById(R.id.toregister);
        mEdtUserName = (EditText) findViewById(R.id.login_username);
        mEdtPwd = (EditText) findViewById(R.id.login_pwd);
        mCheckBox = (CheckBox) findViewById(R.id.auto_login);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mRelMuluser = (RelativeLayout) findViewById(R.id.rel_muluser);
        mImgUserArrow = (ImageView) findViewById(R.id.img_arrow);
        mCVUsers = (CardView) findViewById(R.id.cardview_item);
        isAUtoLogin = mCheckBox.isChecked();


        LogUtil.d("==="+users.size());
        //TODO:获取缓存的最近一条用户信息设置到界面上
        if (users.size() > 0){
            mEdtUserName.setText(DES3Utils.decryptMode(users.get(0).getUsername()));
            mEdtPwd.setText(DES3Utils.decryptMode(users.get(0).getPwd()));
        }
        if (users.size() == 1){
            mRelMuluser.setVisibility(View.GONE);
        }

        initUsersWindow();

        //登录按钮监听事件
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mEdtUserName.getText().toString().trim();
                String pwd = mEdtPwd.getText().toString().trim();
                if (StringUtils.isEmpty(userName)){
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog = DialogUtil.setAnimDialog(alertDialog);
                    alertDialog.setMessage("用户名不能为空");
                    alertDialog.show();
                } else if (StringUtils.isEmpty(pwd)){
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog = DialogUtil.setAnimDialog(alertDialog);
                    alertDialog.setMessage("密码不能为空");
                    alertDialog.show();
                } else {
                    //登录之前先清除sp里面的sessionid
                    SharedPreferences sp = getSharedPreferences("session", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sessionid", null);
                    editor.commit();

                    login(userName, pwd);
                }

            }
        });

        //下拉显示曾经登录的用户
        mRelMuluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsersWindow.isShowing()){
                    mImgUserArrow.setImageResource(R.drawable.ic_collapse_holo_light);
                    mUsersWindow.dismiss();
                } else {
                    mImgUserArrow.setImageResource(R.drawable.ic_expand_holo_light);
                    mUsersWindow.showAsDropDown(mEdtUserName, 0, 0);
                }
            }
        });


        //跳转到注册页面
        mTVToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //自动登陆
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sp = getSharedPreferences(TAG, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (isChecked) {
                    editor.putBoolean(AUTO_LOGIN, true);
                    isAUtoLogin = true;
                } else {
                    editor.putBoolean(AUTO_LOGIN, false);
                    isAUtoLogin = false;
                }
                editor.commit();
            }
        });
    }

    //触摸其他地方隐藏软键盘
    /*private void deleteFocus(View view) {
        if (!(view instanceof EditText || (view instanceof android.widget.RelativeLayout) || (view instanceof ImageView))) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mCVUsers.getVisibility() == View.VISIBLE) {
                        mImgUserArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_holo_light));
                        mCVUsers.setVisibility(View.GONE);
                    }
                    Activity activity = LoginActivity.this;
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
        }*/

    public void login(final String userName, final String pwd) {
        APIProvider.getInstance().login(userName, pwd, new APICallBack<RestMsg>() {
            @Override
            public void onSuccess(RestMsg data) {
                MyApplication.isLogin = true;
                MyApplication.currentUser = userName;
                MyApplication.uid = data.getMessage();
                //如果设置自动登陆，将用户名和密码保存到sp中,否则清空sp
                SharedPreferences sp = getSharedPreferences(TAG, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (isAUtoLogin) {
                    editor.putString(USER, DES3Utils.encryptMode(userName));
                    editor.putString(PWD, DES3Utils.encryptMode(pwd));
                    editor.putBoolean(AUTO_LOGIN, true);
                } else {
                    editor.putString(USER, null);
                    editor.putString(PWD, null);
                }
                editor.commit();

                //保存用户名和密码到数据库中
                //先删除后保存，保证此次登录的用户在数据库的最后
                DBManager.getInstance().delUserInfoByName(DES3Utils.encryptMode(userName));
                DBManager.getInstance().savaUserInfo(DES3Utils.encryptMode(userName), DES3Utils.encryptMode(pwd));



                Intent intent = new Intent();
                intent.putExtra("TAG", "loginactivity");
                setResult(0, intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(RestMsg data) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog = DialogUtil.setAnimDialog(alertDialog);
                alertDialog.setMessage(data.getMessage());
                alertDialog.show();
            }
        });
    }


    /**
     * 显示多个用户信息
     */
    private void initUsersWindow() {

        // 一个自定义的布局，作为显示的内容
        contentView = LayoutInflater.from(LoginActivity.this).inflate(
                R.layout.popup_users, null);
        mUsersWindow = new PopupWindow(contentView,
                carbon.widget.LinearLayout.LayoutParams.MATCH_PARENT, carbon.widget.LinearLayout.LayoutParams.WRAP_CONTENT, true);


        //TODO:获取缓存的用户信息，缓存的用户信息是经过加密处理的，取出时要解密

        if (users.size() > 1){
            users.remove(0);
            ListView listUsers = (ListView) contentView.findViewById(R.id.user_list);
            UsersAdapter adapter = new UsersAdapter(getApplicationContext(), users);
            listUsers.setAdapter(adapter);

            listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mEdtUserName.setText(DES3Utils.decryptMode(users.get(position).getUsername()));
                    mEdtPwd.setText(DES3Utils.decryptMode(users.get(position).getPwd()));
                }
            });

            mUsersWindow.setTouchable(true);

            mUsersWindow.setBackgroundDrawable(new BitmapDrawable());
            mUsersWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            mUsersWindow.setAnimationStyle(R.style.PopupAnimation);
            mUsersWindow.update();
        }

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
