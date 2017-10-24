package mobi.wonders.apps.android.budrest.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import java.util.ArrayList;
import java.util.List;

import bean.JoinDb;
import bean.LateCollectionDb;
import bean.ProjectDb;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.activity.EditProjectActivity;
import mobi.wonders.apps.android.budrest.activity.ManageProjectActivity;
import mobi.wonders.apps.android.budrest.activity.ProjectActivity;
import mobi.wonders.apps.android.budrest.adapter.MineRecyclerAdapter;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;
import mobi.wonders.apps.android.budrest.utils.LogUtil;

public class CreateFragment extends Fragment {

    private View view;
    private RecyclerView mRecycler;
    private MineRecyclerAdapter mAdapter;
    private List<ProjectVO> mProjects = new ArrayList<ProjectVO>();
    private List<JoinDb> mJoinDbs = new ArrayList<JoinDb>();
    private SwipeRefreshLayout mRefreshLayout;
    private AlertDialog.Builder mBuilder;
    private RelativeLayout mEmptyView;
    private RelativeLayout mErrorView;

    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    //网络请求超时
                    mRefreshLayout.setRefreshing(false);
                    AlertDialog dia = new AlertDialog.Builder(getActivity()).create();
                    dia = DialogUtil.setAnimDialog(dia);
                    dia.setMessage("网络连接超时！");
                    dia.show();
                    if(mProjects != null && mProjects.size() == 0){
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

    public CreateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_create, container, false);
        if (MyApplication.isLogin) {
            getCache();
        }
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showCacheData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showCacheData();
        }
    }

    private void initView(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mEmptyView = (RelativeLayout)view.findViewById(R.id.empty_view);
        mErrorView = (RelativeLayout)view.findViewById(R.id.error_view);
        mRecycler = (RecyclerView)view.findViewById(R.id.create_recyclerview);

        initAdapter();

        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        showView(mRecycler);

        mRefreshLayout.setColorSchemeResources(R.color.primary);
        //下拉监听
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttpData();
                handler.postDelayed(timeOutTask, 5000);
            }
        });
    }


    /**
     * 读取缓存
     */
    public void getCache(){
        if (MyApplication.isLogin) {
            mProjects.clear();
            //获取缓存的 我参加的 数据
            mJoinDbs = DBManager.getInstance().getJoin(MyApplication.currentUser, DBManager.TYPE_CREATE);
            List<ProjectVO> temp = DBManager.getInstance().getProjectVos(mJoinDbs);
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

    /**
     * 获取网络数据
     */
    private void getHttpData() {
        APIProvider.getInstance().getProjects(3, new APICallBack<RestMsg>() {
            @Override
            public void onSuccess(RestMsg data) {

                handler.removeCallbacks(timeOutTask);

                if (data.getResult() != null) {

                    mJoinDbs.clear();
                    mProjects.clear();
                    List<ProjectVO> temp = (List<ProjectVO>) data.getResult();
                    for (int i = 0; i < temp.size(); i++) {
                        mProjects.add(temp.get(i));
                    }
                    DBManager.getInstance().saveProjects(mProjects);
                    if (mProjects.size() > 0) {
                        int len = mProjects.size();
                        for (int i = 0; i < len; i++) {
                            JoinDb joinDb = new JoinDb();
                            joinDb.setPid(mProjects.get(i).getId());
                            joinDb.setFlag(DBManager.TYPE_CREATE);
                            joinDb.setUsername(MyApplication.currentUser);
                            mJoinDbs.add(joinDb);
                        }
                        //删除当前用户参加的项目
                        DBManager.getInstance().delJoin(MyApplication.currentUser, DBManager.TYPE_CREATE);
                        DBManager.getInstance().saveJoin(mJoinDbs);
                    }
                    //展示到界面
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(RestMsg data) {

            }
        });
    }

    public void initAdapter(){
        mAdapter = new MineRecyclerAdapter(this.getContext(), mProjects, 1);
        mBuilder = new AlertDialog.Builder(getContext());
        if (mProjects != null) {
            mAdapter.setOnItemClickListener(new MineRecyclerAdapter.CardviewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //项目点击事件
                    DBManager.getInstance().saveLatestProject(mProjects, position);
                    startActivity(new Intent(getActivity(), ProjectActivity.class));
                }

                //项目溢出菜单点击事件
                @Override
                public void onOverflowClick(View v, final int position) {
                    PopupMenu popup = new PopupMenu(getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.create_overflow_list, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit_channel:
                                    //TODO 编辑界面跳转
                                    startActivity(new Intent(getActivity(), EditProjectActivity.class));
                                    break;
                                case R.id.delete_channel:
                                    mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (mProjects.get(position) != null) {
                                                //服务器删除项目
                                                APIProvider.getInstance().deleteProject(mProjects.get(position).getId(), new APICallBack<RestMsg>() {
                                                    @Override
                                                    public void onSuccess(RestMsg data) {
                                                        //删除本地缓存项目
                                                        DBManager.getInstance().delProjectByPID(mProjects.get(position).getId());
                                                        //删除本地JoinDb表中相应记录
                                                        DBManager.getInstance().delJoinByPidFlag(mProjects.get(position).getId(), DBManager.TYPE_CREATE);
                                                        //删除最近打开或收藏表中相应记录
                                                        DBManager.getInstance().delCollectOrLatelyByPid(mProjects.get(position).getId());
                                                        //刷新界面
                                                        mProjects.remove(position);
                                                        getCache();
                                                        handler.sendEmptyMessage(1);
                                                    }

                                                    @Override
                                                    public void onFailure(RestMsg data) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                    AlertDialog deleteDialog = mBuilder.create();
                                    deleteDialog.setTitle("提示");
                                    deleteDialog.setMessage("确定删除此项目？");
                                    deleteDialog = DialogUtil.setAnimDialog(deleteDialog);
                                    deleteDialog.show();
                                    break;
                                case R.id.manage_channel://TODO: 这个功能有待商榷
                                    startActivity(new Intent(getActivity(), ManageProjectActivity.class));
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
