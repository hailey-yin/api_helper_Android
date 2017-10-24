package mobi.wonders.apps.android.budrest.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import java.util.ArrayList;
import java.util.List;

import bean.JoinDb;
import bean.LateCollectionDb;
import bean.ProjectDb;
import dao.JoinDbDao;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.activity.LoginActivity;
import mobi.wonders.apps.android.budrest.activity.ManageAPIActivity;
import mobi.wonders.apps.android.budrest.activity.ProjectActivity;
import mobi.wonders.apps.android.budrest.adapter.MineRecyclerAdapter;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;
import mobi.wonders.apps.android.budrest.utils.LogUtil;

public class JoinFragment extends Fragment {

    private View view;
    private RecyclerView mRecycler;
    private MineRecyclerAdapter mAdapter;
    private List<ProjectVO> mProjects = new ArrayList<ProjectVO>();
    private List<JoinDb> joinDbs = new ArrayList<JoinDb>();

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mEmptyView;
    private RelativeLayout mErrorView;

    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    //数据获取成功，刷新项目列表
                    mRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    showView(mRecycler);
                    if(mProjects!=null && mProjects.size() == 0){
                        showView(mEmptyView);
                    }
                    break;
                case 1:
                    mRefreshLayout.setRefreshing(false);
                    if(mProjects!=null && mProjects.size() == 0){
                        showView(mErrorView);
                    }
                    break;
                default:
            }
        }
    };

    //多线程控制加载图标显示
    Runnable timeOutTask = new Runnable() {
        public void run() {
            handler.sendEmptyMessage(1);
        }
    };

    public JoinFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_join, container, false);
        MyApplication.isJoinFragmentForeword = true;
        if (MyApplication.isLogin) {
            getCache();
        }
        initView(view);
        initAdapter();
        return view;
    }

    //从登录页面返回来时的刷新数据
    @Override
    public void onResume() {
        super.onResume();
        showCacheData();
    }

    //从createfragment切换回来时的数据刷新
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && MyApplication.isJoinFragmentForeword) {
            showCacheData();
        }
    }

    /**
     * 初始化视图
     */
    private void initView(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecycler = (RecyclerView)view.findViewById(R.id.join_recyclerview);
        mEmptyView = (RelativeLayout)view.findViewById(R.id.empty_view);
        mErrorView = (RelativeLayout) view.findViewById(R.id.error_view);

        initAdapter();

        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //下拉监听, 获取网络数据
        mRefreshLayout.setColorSchemeResources(R.color.primary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttpData();
                handler.postDelayed(timeOutTask, 5000);
            }
        });
    }

    //获取参加的项目
    public void getCache(){
        if (MyApplication.isLogin) {
            mProjects.clear();
            //获取缓存的 我参加的 数据
            joinDbs = DBManager.getInstance().getJoin(MyApplication.currentUser, DBManager.TYPE_JOIN);
            List<ProjectVO> temp = DBManager.getInstance().getProjectVos(joinDbs);
            if (temp != null && temp.size() > 0){
                for (int i = 0; i < temp.size(); i++){
                    mProjects.add(temp.get(i));
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
            handler.removeMessages(1);
            showView(mRecycler);
        }
    }

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

    //从服务器获取用户的项目列表，存入数据库
    private void getHttpData() {
        APIProvider.getInstance().getProjects(2, new APICallBack<RestMsg>() {
            @Override
            public void onSuccess(RestMsg data) {

                handler.removeCallbacks(timeOutTask);

                if (data.getResult() != null) {

                    joinDbs.clear();
                    mProjects.clear();
                    List<ProjectVO> temp = (List<ProjectVO>) data.getResult();
                    for (int i = 0; i < temp.size(); i++){
                        mProjects.add(temp.get(i));
                    }
                    DBManager.getInstance().saveProjects(mProjects);
                    if (mProjects.size() > 0) {
                        int len = mProjects.size();
                        for (int i = 0; i < len; i++) {
                            JoinDb joinDb = new JoinDb();
                            joinDb.setPid(mProjects.get(i).getId());
                            joinDb.setFlag(DBManager.TYPE_JOIN);
                            joinDb.setUsername(MyApplication.currentUser);
                            joinDbs.add(joinDb);
                        }
                        //删除当前用户参加的项目
                        DBManager.getInstance().delJoin(MyApplication.currentUser, DBManager.TYPE_JOIN);
                        DBManager.getInstance().saveJoin(joinDbs);
                    }
                    LogUtil.d("++++++"+mProjects.size());
                    //展示到界面
                    initAdapter();
                    mRecycler.setAdapter(mAdapter);
//                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(RestMsg data) {

            }
        });
    }


    public void initAdapter(){
            mAdapter = new MineRecyclerAdapter(this.getContext(), joinDbs, mProjects, 0);
            mAdapter.setOnItemClickListener(new MineRecyclerAdapter.CardviewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //项目点击事件
                    DBManager.getInstance().saveLatestProject(mProjects,position);
                    startActivity(new Intent(getActivity(), ProjectActivity.class));
                }
                @Override
                public void onOverflowClick(View v, final int position) {
                    PopupMenu popup = new PopupMenu(getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    // FIXME: 在这里joinDbs的flag都是TYPE_JOIN
                    if (joinDbs.get(position).getFlag() == DBManager.TYPE_JOIN) {
                        inflater.inflate(R.menu.jion_overflow_list, popup.getMenu());
                    } else if (joinDbs.get(position).getFlag() == DBManager.TYPE_CREATE) {
                        inflater.inflate(R.menu.jion_overflow_list_edit, popup.getMenu());
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.api_channel:
                                    startActivity(new Intent(getActivity(), ManageAPIActivity.class));
                                    break;
                                case R.id.attention_channel:
                                    LateCollectionDb c = DBManager.getInstance()
                                            .getLateOrCollection(mProjects.get(position).getId(), DBManager.TYPE_COLLECTION);
                                    if (c != null) {
                                        DBManager.getInstance().delCollectOrLatel(mProjects.get(position).getId(),DBManager.TYPE_COLLECTION);
                                    } else {
                                        //该项目未被收藏
                                        LateCollectionDb lc = new LateCollectionDb();
                                        lc.setPid(mProjects.get(position).getId());
                                        lc.setUsername(MyApplication.currentUser);
                                        lc.setFlag(DBManager.TYPE_COLLECTION);
                                        DBManager.getInstance().saveLateOrCollect(lc);
                                    }
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
    }


}
