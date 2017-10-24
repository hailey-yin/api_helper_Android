package mobi.wonders.apps.android.budrest.activity;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import java.util.List;

import bean.ProjectDb;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.adapter.MyFragmentAdapter;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.fragment.AttentionFragment;
import mobi.wonders.apps.android.budrest.fragment.HomeFragment;
import mobi.wonders.apps.android.budrest.fragment.MyFragment;
import mobi.wonders.apps.android.budrest.fragment.TestHomeFragment;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.utils.DES3Utils;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;
import mobi.wonders.apps.android.budrest.view.NoScrollViewPager;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    DrawerLayout drawer;//抽屉

    private NoScrollViewPager viewPager;//fragment的容
    private RelativeLayout mBtnLogin;//登录按钮
    private TextView mAccount; //用户栏显示
    private ImageView mArrow; //用户栏箭头
    private LinearLayout mPlaceholderPanel;  //占位面板
    private LinearLayout mSlidePanel;  //滑动面板
    private LinearLayout mMenuePanel;  //导航面板
    private View mDrawerDivider;  //分隔栏
    private LinearLayout mDrawerBottom;  //侧滑菜单底部面板

    private LinearLayout mLinHome;//侧滑菜单：首页
    private LinearLayout mLinAttention;//侧滑菜单：关注
    private LinearLayout mLinMy;//侧滑菜单：我的
    private LinearLayout mLinNew;//侧滑菜单：新建
    private LinearLayout mLinSetting;//侧滑菜单：设置
    private LinearLayout mLinTheme;//侧滑菜单：主题
    private LinearLayout mLinAccount;//账号信息
    private LinearLayout mLinChangeAccount;//切换账号
    private LinearLayout mLinChangePWD;//修改密码
    private LinearLayout mLinExitAccount;//退出账号

    private TextView mThemeInit; //主题“BudRest”的首字母
    private TextView mThemeLeft; //主题“BudRest”的剩下字母

    private boolean isToMyFragment = false;//标志现在是否正在向MyFragment切换
    private boolean isToCreateProject = false;//标志现在是否正向新增项目界面跳转
    private boolean isExpand = false;//标志账户界面是否展开

    //延时收起抽屉，好显示水波动画
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://主页
                    drawer.closeDrawer(GravityCompat.START);
                    viewPager.setCurrentItem(0);
                    break;
                case 1://我的
                    drawer.closeDrawer(GravityCompat.START);
                    //已登录，直接切换
                    if (MyApplication.isLogin) {
                        viewPager.setCurrentItem(1);
                    } else {//未登录，跳转到登录界面
                        //1表示MainActivity打开的
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isToMyFragment = true;
                                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 1);
                            }
                        });
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("未登录。是否登录？");
                        alertDialog = DialogUtil.setAnimDialog(alertDialog);
                        alertDialog.show();
                    }
                    break;
                case 2://关注
                    drawer.closeDrawer(GravityCompat.START);
                    viewPager.setCurrentItem(2);
                    break;
                case 3://创建项目
                    drawer.closeDrawer(GravityCompat.START);
                    //没登陆就 不能创建项目
                    if (MyApplication.isLogin){
                        startActivity(new Intent(MainActivity.this, CreateProjectActivity.class));
                    } else {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("未登录。是否登录？");
                        alertDialog = DialogUtil.setAnimDialog(alertDialog);
                        alertDialog.show();
                    }
                    break;
                case 4://设置界面
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    break;
                case 5:
                    drawer.closeDrawer(GravityCompat.START);
                    //TODO:主题切换
                    break;
                case 6://用户信息
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case 7://切换账号
                    drawer.closeDrawer(GravityCompat.START);
                    Bundle bundle = new Bundle();
                    bundle.putInt("tag", 1);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case 8://修改密码
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, EditUserInfoActivity.class));
                    break;
                case 9://退出登录
//                    drawer.closeDrawer(GravityCompat.START);
                    logout();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //drawer覆盖到通知栏
        if(Build.VERSION.SDK_INT >= 21){
            TypedValued typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            int color = typedValue.data;
            drawer.setStatusBarBackgroundColor(color);
        }
        loginLogic();
    }

    //登录逻辑
    private void loginLogic() {
        SharedPreferences sp = getSharedPreferences(LoginActivity.TAG, Activity.MODE_PRIVATE);
        boolean autologin = sp.getBoolean(LoginActivity.AUTO_LOGIN, false);
        if (autologin) {
            String encodeuser = sp.getString(LoginActivity.USER, null);
            String encodepassword = sp.getString(LoginActivity.PWD, null);
            String user = DES3Utils.decryptMode(encodeuser);
            String password = DES3Utils.decryptMode(encodepassword);

            login(user, password);
        }
    }

    public void login(final String userName, final String pwd) {
        APIProvider.getInstance().login(userName, pwd, new APICallBack<RestMsg>() {
            @Override
            public void onSuccess(RestMsg data) {
                MyApplication.isLogin = true;
                MyApplication.currentUser = userName;
                MyApplication.uid = data.getMessage();

                //TODO:先删除后保存，保证此次登录的用户在数据库的最后
            }
            @Override
            public void onFailure(RestMsg data) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog = DialogUtil.setAnimDialog(alertDialog);
                alertDialog.setMessage(data.getMessage());
                alertDialog.show();
            }

        });
    }

    private void logout(){
        APIProvider.getInstance().logout(new APICallBack<RestMsg>() {
            @Override
            public void onSuccess(RestMsg data) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog = DialogUtil.setAnimDialog(alertDialog);
                alertDialog.setMessage("已退出当前账号");
                alertDialog.show();
                //清空sp中的sessionid
                SharedPreferences sp = getSharedPreferences("session", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("sessionid", null);
                editor.commit();

                MyApplication.isLogin = false;
                MyApplication.currentUser = null;
                MyApplication.uid = null;

                //清空sp中保存的登录信息
                SharedPreferences loginsp = getSharedPreferences(LoginActivity.TAG, Activity.MODE_PRIVATE);
                SharedPreferences.Editor logineditor = loginsp.edit();
                logineditor.putString(LoginActivity.USER, null);
                logineditor.putString(LoginActivity.PWD, null);
                logineditor.putBoolean(LoginActivity.AUTO_LOGIN, false);
                logineditor.commit();

                mAccount.setText("未登录");

                fold();

            }

            @Override
            public void onFailure(RestMsg data) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog = DialogUtil.setAnimDialog(alertDialog);
                alertDialog.setMessage(data.getMessage());
                alertDialog.show();
            }
        });
    }

    private void fold(){
        //收回
        mArrow.setImageResource(R.drawable.arrow_down);
        isExpand = false;

        Animation transAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.account_pannel_exit);
        transAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fadeAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                mMenuePanel.setAnimation(fadeAnim);
                mDrawerBottom.setAnimation(fadeAnim);
                mDrawerDivider.setAnimation(fadeAnim);
                mMenuePanel.setVisibility(View.VISIBLE);
                mDrawerBottom.setVisibility(View.VISIBLE);
                mDrawerDivider.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mSlidePanel.setAnimation(transAnim);
        mSlidePanel.setVisibility(View.INVISIBLE);
        mPlaceholderPanel.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.isLogin) {
            mAccount.setText(MyApplication.currentUser);
        } else {
            mAccount.setText(getResources().getString(R.string.nologin));
        }
        if (isExpand) {
            mArrow.setImageResource(R.drawable.arrow_up);
        } else {
            mArrow.setImageResource(R.drawable.arrow_down);
        }
    }

    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mBtnLogin = (RelativeLayout) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mAccount = (TextView) findViewById(R.id.account_textview);
        mArrow = (ImageView) findViewById(R.id.image_arrow);

        mPlaceholderPanel = (LinearLayout) findViewById(R.id.placeholder_panel);
        mPlaceholderPanel.setVisibility(View.GONE);
        mSlidePanel = (LinearLayout) findViewById(R.id.slide_panel);
        mSlidePanel.setVisibility(View.GONE);
        mMenuePanel = (LinearLayout) findViewById(R.id.menu_panel);
        mMenuePanel.setVisibility(View.VISIBLE);
        mDrawerDivider = findViewById(R.id.drawer_divider);
        mDrawerDivider.setVisibility(View.VISIBLE);
        mDrawerBottom = (LinearLayout) findViewById(R.id.drawer_bottom);
        mDrawerBottom.setVisibility(View.VISIBLE);

        mBtnLogin.setOnClickListener(this);
        mLinHome = (LinearLayout) findViewById(R.id.item_home);
        mLinHome.setOnClickListener(this);
        mLinAttention = (LinearLayout) findViewById(R.id.item_attention);
        mLinAttention.setOnClickListener(this);
        mLinMy = (LinearLayout) findViewById(R.id.item_my);
        mLinMy.setOnClickListener(this);
        mLinNew = (LinearLayout) findViewById(R.id.item_new);
        mLinNew.setOnClickListener(this);
        mLinSetting = (LinearLayout) findViewById(R.id.item_setting);
        mLinSetting.setOnClickListener(this);
        mLinTheme = (LinearLayout) findViewById(R.id.item_theme);
        mLinTheme.setOnClickListener(this);
        mLinAccount = (LinearLayout) findViewById(R.id.account_linearlayout);
        mLinAccount.setOnClickListener(this);
        mLinChangeAccount = (LinearLayout) findViewById(R.id.change_account);
        mLinChangeAccount.setOnClickListener(this);
        mLinChangePWD = (LinearLayout) findViewById(R.id.edit_account);
        mLinChangePWD.setOnClickListener(this);
        mLinExitAccount = (LinearLayout) findViewById(R.id.exit_account);
        mLinExitAccount.setOnClickListener(this);

        mThemeInit = (TextView)findViewById(R.id.init_letter);
        mThemeLeft = (TextView)findViewById(R.id.left_letter);
        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);

        //设置fragment
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(3);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
            MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager());
            adapter.addFragment(new HomeFragment(drawer), getResources().getString(R.string.homepage));
            adapter.addFragment(new MyFragment(drawer), getResources().getString(R.string.my));
            adapter.addFragment(new AttentionFragment(drawer), getResources().getString(R.string.attention));
            viewPager.setAdapter(adapter);
        }

        AssetManager asm = getAssets();
        String path = "fonts/caviardreams_bold#2.ttf";

        Typeface typeFace =Typeface.createFromAsset(asm,path);
        mThemeInit.setTypeface(typeFace);
        mThemeLeft.setTypeface(typeFace);
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //菜单选中事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                if (MyApplication.isLogin) {
                    if(isExpand == false){
                        //展开
                        mArrow.setImageResource(R.drawable.arrow_up);
                        isExpand = true;

                        mMenuePanel.setVisibility(View.INVISIBLE);
                        mPlaceholderPanel.setVisibility(View.VISIBLE);
                        Animation transAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.account_pannel_enter);
                        mSlidePanel.setAnimation(transAnim);
                        mSlidePanel.setVisibility(View.VISIBLE);
                        Animation fadeAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                        mDrawerBottom.setAnimation(fadeAnim);
                        mDrawerBottom.setVisibility(View.INVISIBLE);
                        mDrawerDivider.setAnimation(fadeAnim);
                        mDrawerDivider.setVisibility(View.INVISIBLE);
                    }else{
                        fold();
                    }
                } else {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 1);
                }
                break;
            case R.id.item_home:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(0);
                    }
                }, 300);
                break;
            case R.id.item_attention:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 300);
                break;
            case R.id.item_my:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(2);
                    }
                }, 300);
                break;
            case R.id.item_new:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(3);
                    }
                }, 300);
                break;
            case R.id.item_setting:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(4);
                    }
                }, 300);
                break;
            case R.id.item_theme:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(5);
                    }
                }, 300);

                break;
            case R.id.account_linearlayout:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(6);
                    }
                }, 300);

                break;
            case R.id.change_account:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(7);
                    }
                }, 300);

                break;
            case R.id.edit_account:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(8);
                    }
                }, 300);

                break;
            case R.id.exit_account:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(9);
                    }
                }, 300);

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {//0标志从登录界面返回
            if (data != null && "loginactivity".equals(data.getStringExtra("TAG"))){
                //从登录界面返回,并且loginactivity的关闭是由登录成功才关闭的,并且正在向MyFragment切换
                if (isToMyFragment){
                    viewPager.setCurrentItem(1);
                    //现在没有向MyFragment切换，标志位设置为false
                    isToMyFragment = false;
                }
                if (isToCreateProject) {
                    startActivity(new Intent(MainActivity.this, CreateProjectActivity.class));
                    isToCreateProject = false;
                }
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setTitle("提示");
                alertDialog.setMessage("确定退出？");
                alertDialog = DialogUtil.setAnimDialog(alertDialog);
                alertDialog.show();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        MyApplication.isLatestFragmentForeword = false;
        MyApplication.isJoinFragmentForeword = false;
        APIProvider.getInstance().logout(new APICallBack<RestMsg>(){
            @Override
            public void onSuccess(RestMsg data) {
                MyApplication.currentUser = null;
                MyApplication.uid = null;
                MyApplication.isLogin = false;
            }
            @Override
            public void onFailure(RestMsg data) {
            }
        });

        super.onDestroy();
    }
}
