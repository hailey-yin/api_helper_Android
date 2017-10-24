package mobi.wonders.apps.android.budrest.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.adapter.MyFragmentAdapter;

public class MyFragment extends Fragment {

    DrawerLayout drawerLayout;

    ViewPager viewPager;

    TabLayout tabLayout;

    public MyFragment(){}

    public MyFragment(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.isLogin) {
            //getAccountsInfo();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getResources().getString(R.string.my));

        //mDrawerLayout为MainActivity传过来的参数
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);
        /*tabLayout.setSelectedTabIndicatorColor(R.color.blue);*/

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        MyFragmentAdapter adapter = new MyFragmentAdapter(getChildFragmentManager());
        adapter.addFragment(new JoinFragment(), "参加");
        adapter.addFragment(new CreateFragment(), "创建");
        viewPager.setAdapter(adapter);
    }

    //获取网络数据并保存到数据库中
    private void getAccountsInfo() {
            /*APIProvider.getInstance().getUserInfo(MyApplication.uid, new APICallBack<RestMsg>() {
                @Override
                public void onSuccess(RestMsg data) {
                        List<SimpleProject> simpleProjects = new ArrayList<SimpleProject>();
                        simpleProjects = ((AccountVO)data.getResult()).getProject();
                        if (simpleProjects.size() > 0) {
                            //当网络上获取的数据为空时不清空数据库
                            DBManager.getInstance().delJoinByName(MyApplication.currentUser);
                            List<JoinDb> jds = new ArrayList<>();
                            for(int i = 0; i < simpleProjects.size(); i++) {
                                JoinDb jd = new JoinDb(null, MyApplication.currentUser,
                                        simpleProjects.get(i).getPid(),
                                        simpleProjects.get(i).getAuth());
                                jds.add(jd);
                            }
                            DBManager.getInstance().saveJoin(jds);

                            // TODO: 2015/12/30 数据库测试完删除旧代码
                            new Delete().from(JoinModel.class).where("uid = ?", MyApplication.uid).execute();
                            ActiveAndroid.beginTransaction();
                            try {
                                for (int i = 0;i < simpleProjects.size();i++) {
                                    if (simpleProjects.get(i).getAuth() == 1 ||simpleProjects.get(i).getAuth() == 2) {
                                        JoinModel joinModel = new JoinModel();
                                        joinModel.setPid(simpleProjects.get(i).getPid());
                                        joinModel.setAuth(simpleProjects.get(i).getAuth());
                                        joinModel.setUid(MyApplication.uid);
                                        joinModel.save();
                                    }
                                }
                                ActiveAndroid.setTransactionSuccessful();
                            }finally {
                                ActiveAndroid.endTransaction();
                            }
                        }
                }

                @Override
                public void onFailure(RestMsg data) {

                }
            });*/
    }

}
