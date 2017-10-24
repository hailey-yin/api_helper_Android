package mobi.wonders.apps.android.budrest.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import bean.ProjectDb;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.adapter.HomeRecyclerAdapter;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.LogUtil;


/**
 * Created by yhl on 2015/12/3.
 */
public class SearchActivity extends AppCompatActivity{

    private RecyclerView mRecycler;
    private HomeRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private List<ProjectVO> mProjects;

    private SearchView mSearchView;

    boolean tag ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tag = MyApplication.isLogin;

        initToolbar();
        initDataList();
        getCache();
        initAdapter();
        initRecycler();
        initManager();
    }

    public void initDataList(){
        mProjects = new ArrayList<ProjectVO>();
    }

    public void getCache(){
        //TODO 搜索
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("搜索");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void initRecycler(){
        mRecycler = (RecyclerView)findViewById(R.id.search_recyclerview);
        mRecycler.setHasFixedSize(true);
        if (mAdapter != null) {
            mRecycler.setAdapter(mAdapter);
        }
    }

    public void initAdapter(){
        mAdapter = new HomeRecyclerAdapter(mProjects, SearchActivity.this);

        mAdapter.setOnItemClickListener(new HomeRecyclerAdapter.CardviewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //项目点击事件
                startActivity(new Intent(SearchActivity.this, ProjectActivity.class));
                //先删除缓存（如果存在的话），再存入数据
                DBManager.getInstance().saveLatestProject(mProjects, position);
                // TODO: 2015/12/30 数据库测试完后 删除旧代码
            }
        });
    }

    private void initManager(){
        mManager = new LinearLayoutManager(SearchActivity.this);
        mRecycler.setLayoutManager(mManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setQueryHint("请输入项目名...");

        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconified(false);
//            mSearchView.setIconifiedByDefault(false);
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    //TODO 即时搜索
                    mProjects.clear();
                    if (newText != null && !"".equals(newText)){

                        List<ProjectVO> pds = DBManager.getInstance().getFromSearch(newText);

                        if (pds.size()>0){
                            for (int i = 0; i<pds.size(); i++){
                                //未登录
                                if (!MyApplication.isLogin){
                                    if (pds.get(i).getAuthStatus() == 1){
                                        mProjects.add(pds.get(i));
                                    }
                                } else {
                                    //已登录
                                    if (pds.get(i).getAuthStatus() == 1){
                                        mProjects.add(pds.get(i));
                                    }
                                    if (pds.get(i).getAuthStatus() == 2
                                            && pds.get(i).getAuthor().equals(MyApplication.uid)){
                                        mProjects.add(pds.get(i));
                                    }
                                }

                            }
                        }

                    } else {
                        mProjects.clear();
                    }
                    mAdapter.notifyDataSetChanged();
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    //TODO 输入完成后搜索
                    return true;
                }
            };
            mSearchView.setOnQueryTextListener(queryTextListener);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
