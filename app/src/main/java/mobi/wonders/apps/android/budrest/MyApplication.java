package mobi.wonders.apps.android.budrest;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.woders.apiprovider.APIUtil;

import mobi.wonders.apps.android.budliteprovider.db.BudDataBaseManager;

/**
 * Created by jiangguangming on 2015/11/16.
 */
public class MyApplication extends Application{

    private static MyApplication application;

    private static Context context;

    public static RequestQueue queues;

    public static boolean isLogin = false;//是否已登录

    public static String currentUser;//当前的用户
    public static String uid;

    public static int netState = -1;//网络状态

    public static boolean isLatestFragmentForeword;//该属性标识Homefragment是否可见，用于重构Homefragment，实现刷新功能
    public static boolean isJoinFragmentForeword;

    private static final boolean DEBUG = false; // greendao 数据库调试日志

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        currentUser = "";
        uid = "";
        //初始化网络请求队列
        APIUtil.getInstance().initContext(getApplicationContext());

        BudDataBaseManager.initContext(this, DEBUG);

        context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static MyApplication getInstance(){
        if (application == null){
            application = new MyApplication();
        }
        return application;
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }

    public static Context getContext(){

        return context;
    }
}
