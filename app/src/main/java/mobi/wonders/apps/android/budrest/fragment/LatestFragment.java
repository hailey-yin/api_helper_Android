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

/**
 * create an instance of this fragment.
 */
public class LatestFragment extends Fragment {

    private RecyclerView mRecycler;//RecyclerView，显示数据
    private SwipeRefreshLayout mRefreshLayout;//下拉刷新
    private RelativeLayout mEmptyView;//空页
    private RelativeLayout mErrorView;//错误页

    private HomeRecyclerAdapter mAdapter;

    private List<ProjectVO> mLatestProjects = new ArrayList<ProjectVO>();//从缓存获取到的数据

    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    //无法获取缓存数据
                    mRefreshLayout.setRefreshing(false);
                    showView(mErrorView);
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

    public LatestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        MyApplication.isLatestFragmentForeword = true;
        getCache();
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showCacheData();
    }

    //新建该fragment时，首先会调用该方法，为防止发生空指针异常，特意设置isLatestFragmentForeword标志位
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && MyApplication.isLatestFragmentForeword) {
            LogUtil.d("执行没有");
            showCacheData();
        }
    }


    private void initView(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mEmptyView = (RelativeLayout)view.findViewById(R.id.empty_view);
        mErrorView = (RelativeLayout)view.findViewById(R.id.error_view);
        mRecycler = (RecyclerView) view.findViewById(R.id.latest_recyclerview);

        //adapter的初始化
        mAdapter = new HomeRecyclerAdapter(mLatestProjects, this.getContext());
        mAdapter.setOnItemClickListener(new HomeRecyclerAdapter.CardviewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //项目点击事件
                DBManager.getInstance().saveLatestProject(mLatestProjects, position);
                startActivity(new Intent(getActivity(), ProjectActivity.class));
            }
        });

        //RecyclerView的初始化
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //下啦刷新,获取缓存数据并更新界面
        mRefreshLayout.setColorSchemeResources(R.color.primary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCacheData();
                handler.postDelayed(timeOutTask, 4000);
            }
        });
    }

    //获取缓存
    public void getCache() {
        mLatestProjects.clear();
        List<ProjectVO> temp = new ArrayList<ProjectVO>();//从缓存获取到的数据
        temp = DBManager.getInstance().getLatestProject();
        if (temp.size() > 0){
            for (int i = 0; i < temp.size(); i++){
                mLatestProjects.add(temp.get(i));
            }
        }
    }


    //现实缓存数据
    public void showCacheData(){
        getCache();
        mAdapter.notifyDataSetChanged();
        if(mLatestProjects.size() == 0){
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
