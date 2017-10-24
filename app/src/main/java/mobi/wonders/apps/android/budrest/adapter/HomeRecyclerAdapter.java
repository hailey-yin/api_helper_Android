package mobi.wonders.apps.android.budrest.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bean.LateCollectionDb;
import bean.ProjectDb;
import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.LogUtil;
import mobi.wonders.apps.android.budrest.view.ExpandableTextView;

/**
 * Created by yhl on 2015/11/18.
 */
public class HomeRecyclerAdapter extends android.support.v7.widget.RecyclerView.Adapter<HomeRecyclerAdapter.ProjectHolder> {

    private List<ProjectVO> projects;
    private Context context;
    private CardviewItemClickListener mListener;

    public HomeRecyclerAdapter(List<ProjectVO> projects, Context context){
        this.projects = projects;
        this.context = context;
    }

    //自定义ViewHolder类
    static class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView projectTitle;
        TextView projectAuthor;
        View divider;
        ExpandableTextView projectDesc;
        ImageView star;
        CardviewItemClickListener mListener;

        public ProjectHolder(View itemView, CardviewItemClickListener listener) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.latest_recyclerview);
            projectTitle = (TextView) itemView.findViewById(R.id.project_title);
            projectAuthor = (TextView) itemView.findViewById(R.id.project_author);
            projectDesc = (ExpandableTextView)itemView.findViewById(R.id.project_desc)
                    .findViewById(R.id.expand_text_view);
            divider = itemView.findViewById(R.id.divider);
            star = (ImageView) itemView.findViewById(R.id.star_image);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    /**
     * 渲染具体的ViewHolder
     * @param viewGroup ViewHolder的容器
     * @param viewType 根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public HomeRecyclerAdapter.ProjectHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_home, viewGroup, false);
        ProjectHolder projectHolder = new ProjectHolder(view, mListener);
        return projectHolder;
    }

    /**
     * 绑定ViewHolder的数据。
     * @param projectHolder
     * @param i 数据源list的下标
     */
    @Override
    public void onBindViewHolder(final ProjectHolder projectHolder, final int i){
        projectHolder.projectTitle.setText(projects.get(i).getName());
        projectHolder.projectAuthor.setText(projects.get(i).getUrlprefix());
        if(!projects.get(i).getDesc().isEmpty() ){
            projectHolder.projectDesc.setText(projects.get(i).getDesc());
            projectHolder.divider.setVisibility(View.VISIBLE);
        }

        //根据项收藏表查找，返回不为null，表示该项目被收藏目
        final LateCollectionDb c = DBManager.getInstance().getLateOrCollection(projects.get(i).getId(),DBManager.TYPE_COLLECTION);
        /*if (projects.get(i).getId() != null) {
            c = DBManager.getInstance().getLateOrCollection(projects.get(i).getId(),DBManager.TYPE_COLLECTION);
        }*/
        if (c != null){
            projectHolder.star.setImageResource(R.drawable.star_collected1);
        } else {
            projectHolder.star.setImageResource(R.drawable.star_uncollected1);
        }

        projectHolder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LateCollectionDb collectionModel = DBManager.getInstance().getLateOrCollection(projects.get(i).getId(),DBManager.TYPE_COLLECTION);
                if (collectionModel != null) {
                    //已经收藏，移除收藏
                    projectHolder.star.setImageResource(R.drawable.star_uncollected1);
                    DBManager.getInstance().delCollectOrLatel(projects.get(i).getId(),DBManager.TYPE_COLLECTION);
                } else {
                    //没有收藏，收藏
                    projectHolder.star.setImageResource(R.drawable.star_collected1);
                    LateCollectionDb lc = new LateCollectionDb();
                    lc.setUsername(MyApplication.currentUser);
                    lc.setPid(projects.get(i).getId());
                    lc.setFlag(DBManager.TYPE_COLLECTION);
                    DBManager.getInstance().saveLateOrCollect(lc);
                }
            }
        });
    }

    /**
     * item点击监听函数
     */
    public void setOnItemClickListener(CardviewItemClickListener listener){
        this.mListener = listener;
    }

    public interface CardviewItemClickListener{
        public void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
