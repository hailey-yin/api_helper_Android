package mobi.wonders.apps.android.budrest.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import bean.LateCollectionDb;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.activity.ProjectActivity;
import mobi.wonders.apps.android.budrest.adapter.AttentionRecyclerAdapter;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;
import mobi.wonders.apps.android.budrest.view.DividerItemDecoration;


public class AttentionFragment extends Fragment {

    private View view;
    private RecyclerView mRecycler;
    private RelativeLayout mEmptyView;//空页
    private RelativeLayout mErrorView;//错误页
    private AttentionRecyclerAdapter mAdapter;
    private List<ProjectVO> mProjects;
    private List<LateCollectionDb> mAttentions;
    private SwipeRefreshLayout mRefreshLayout;
    private DrawerLayout drawerLayout;

    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    mRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    };

    //多线程控制加载图标显示
    Runnable timeOutTask = new Runnable() {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    };

    public AttentionFragment() {
    }

    public AttentionFragment(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjects = new ArrayList<ProjectVO>();
        mAttentions = new ArrayList<LateCollectionDb>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attention, container, false);
        initToolBar(view);
        getCache();
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showCacheData();
    }

    //当该界面出现在用户眼前时回调
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showCacheData();
        }
    }

    public void initToolBar(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getResources().getString(R.string.attention));

        //mDrawerLayout为MainActivity传过来的参数
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initView(View view) {
        mEmptyView = (RelativeLayout)view.findViewById(R.id.empty_view);
        mErrorView = (RelativeLayout)view.findViewById(R.id.error_view);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecycler = (RecyclerView)view.findViewById(R.id.attention_recyclerview);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) ;
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        //初始化adapter
        mAdapter = new AttentionRecyclerAdapter(mProjects, this.getContext());
        mAdapter.setOnItemClickListener(new AttentionRecyclerAdapter.CardviewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 项目点击事件
                DBManager.getInstance().saveLatestProject(mProjects, position);
                startActivity(new Intent(getActivity(), ProjectActivity.class));
            }

            @Override
            public void onStarClick(View view, final int position) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.getInstance().delCollectOrLatel(mProjects.get(position).getId(), DBManager.TYPE_COLLECTION);
                        mProjects.remove(position);
                        handler.sendEmptyMessage(1);
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
                alertDialog.setMessage("确定取消关注此项目？");
                alertDialog = DialogUtil.setAnimDialog(alertDialog);
                alertDialog.show();
            }
        });


        //初始化
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //下拉刷新
        mRefreshLayout.setColorSchemeResources(R.color.primary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCacheData();
                handler.postDelayed(timeOutTask, 4000);
            }
        });

    }

    private void getCache() {
        mProjects.clear();
        //获取所有收藏的项目
        mAttentions = DBManager.getInstance().getLatesOrCollections(DBManager.TYPE_COLLECTION);
        List<LateCollectionDb> temp = new ArrayList<LateCollectionDb>();
        List<ProjectVO> VOtemp ;
        if (mAttentions != null) {

            //如果没登录
            for (int i = 0; i < mAttentions.size(); i++){
                if ("".equals(mAttentions.get(i).getUsername())){
                    temp.add(mAttentions.get(i));
                }
            }
            VOtemp = DBManager.getInstance().getLCProjectVos(temp);
            //如果登录了
            if (MyApplication.isLogin){
                for (int i = 0; i < mAttentions.size(); i++){
                    if (MyApplication.currentUser.equals(mAttentions.get(i).getUsername())){
                        temp.add(mAttentions.get(i));
                    }
                }
                VOtemp = DBManager.getInstance().getLCProjectVos(temp);
            }

            if (VOtemp != null && VOtemp.size() > 0){
                for (int i = 0; i < VOtemp.size(); i++){
                    mProjects.add(VOtemp.get(i));
                }
            }

        }
    }

    //显示缓存数据
    public void showCacheData(){
        getCache();
        mAdapter.notifyDataSetChanged();
        if(mProjects.size() == 0){
            showView(mEmptyView);
        }else{
            mRefreshLayout.setRefreshing(false);
            handler.removeMessages(0);
            showView(mRecycler);
        }
    }

    //根据返回的数据现实界面
    private void showView(View view){
        if(view == mErrorView){
            mErrorView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
        } else if(view == mEmptyView){
            mErrorView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else if(view == mRecycler){
            mErrorView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }



}
