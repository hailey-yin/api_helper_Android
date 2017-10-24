package mobi.wonders.apps.android.budrest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;
import java.util.ArrayList;
import java.util.List;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.activity.ProjectActivity;
import mobi.wonders.apps.android.budrest.adapter.HomeRecyclerAdapter;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.LogUtil;

public class AllFragment extends Fragment {

    private RecyclerView mRecycler;
    private HomeRecyclerAdapter mAdapter;
    private List<ProjectVO> mCahceAllProjects = new ArrayList<ProjectVO>();
    private List<ProjectVO> mAllProjects = new ArrayList<ProjectVO>();
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mEmptyView;
    private RelativeLayout mErrorView;


    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    //网络数据获取成功，刷新项目列表
                    mRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    if(mAllProjects.size() == 0){
                        showView(mEmptyView);
                    }else{
                        showView(mRecycler);
                    }
                    break;
                case 1:
                    //网络请求超时
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "网络连接超时", Toast.LENGTH_SHORT).show();
                    if(mAllProjects.size() == 0){
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


    public AllFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        getCache();
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showCacheData();
        LogUtil.d(">>>>>"+mAllProjects.size());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showCacheData();
        }
    }

    private void initView(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mEmptyView = (RelativeLayout)view.findViewById(R.id.empty_view);
        mErrorView = (RelativeLayout)view.findViewById(R.id.error_view);
        mRecycler = (RecyclerView)view.findViewById(R.id.all_recyclerview);


        mAdapter = new HomeRecyclerAdapter(mAllProjects, this.getContext());
        mAdapter.setOnItemClickListener(new HomeRecyclerAdapter.CardviewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //项目点击事件
                DBManager.getInstance().saveLatestProject(mAllProjects, position);
                startActivity(new Intent(getActivity(), ProjectActivity.class));
            }
        });

        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mRefreshLayout.setColorSchemeResources(R.color.primary);
        //下拉监听
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(MyApplication.isLogin){
                    getHttpData(1);
                } else {
                    getHttpData(0);
                }

                handler.postDelayed(timeOutTask, 4000);
            }
        });
    }

    //获取缓存数据
    private void getCache() {
        mCahceAllProjects.clear();
        mAllProjects.clear();
        mCahceAllProjects = DBManager.getInstance().getProjectsOrderByName();
        for (int i=0;i<mCahceAllProjects.size();i++){
            if(mCahceAllProjects.get(i).getAuthStatus() == 1) { //公开项目
                mAllProjects.add(mCahceAllProjects.get(i));
            } else if(mCahceAllProjects.get(i).getAuthStatus() == 2) { //私有项目
                if(MyApplication.isLogin && MyApplication.uid.equals(mCahceAllProjects.get(i).getAuthor())) { //该用户为项目创建者
                    mAllProjects.add(mCahceAllProjects.get(i));
                }
            }
        }
    }

    //现实缓存数据
    public void showCacheData(){
        getCache();
        mAdapter.notifyDataSetChanged();
        if(mAllProjects.size() == 0){
            showView(mEmptyView);
        }else{
            showView(mRecycler);
        }
    }



    private void getHttpData(int type) {
        //获取网络数据，跟新数据库缓存
        APIProvider.getInstance().getProjects(type, new APICallBack<RestMsg>() {
            @Override
            public void onSuccess(RestMsg data) {
                handler.removeCallbacks(timeOutTask);
                //更新数据库缓存;
                List<ProjectVO> pros = (List<ProjectVO>) data.getResult();
                mAllProjects.clear();
                for (int i = 0; i < pros.size(); i++){
                    mAllProjects.add(pros.get(i));
                }
                DBManager.getInstance().saveProjects(pros);
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);

            }
            @Override
            public void onFailure(RestMsg data) {

            }
        });
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

}
