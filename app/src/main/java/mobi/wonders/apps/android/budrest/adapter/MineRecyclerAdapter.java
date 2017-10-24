package mobi.wonders.apps.android.budrest.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bean.JoinDb;
import bean.ProjectDb;
import carbon.widget.ImageView;
import carbon.widget.RelativeLayout;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.LogUtil;
import mobi.wonders.apps.android.budrest.view.ExpandableTextView;

/**
 * Created by yhl on 2015/11/25.
 */
public class MineRecyclerAdapter extends android.support.v7.widget.RecyclerView.Adapter<MineRecyclerAdapter.ProjectHolder>{
    private List<ProjectVO> projects;
    private List<JoinDb> joinModels;
    private int tag;//0表示参加，1表示创建
    private Context context;
    private CardviewItemClickListener mListener;

    public MineRecyclerAdapter(Context context, List<ProjectVO> projects, int tag) {
        this.context = context;
        this.projects = projects;
        this.tag = tag;
    }

    public MineRecyclerAdapter(Context context, List<JoinDb> joinModels, List<ProjectVO> projects, int tag) {
        this.context = context;
        this.joinModels = joinModels;
        this.projects = projects;
        this.tag = tag;
    }

    //自定义ViewHolder类
    static class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView projectTitle;
        TextView projectAuthor;
        ExpandableTextView projectDesc;
        View divider;
        RelativeLayout overflow;
        ImageView authority;
        CardviewItemClickListener mListener;

        public ProjectHolder(View itemView, CardviewItemClickListener listener) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.latest_recyclerview);
            projectTitle = (TextView) itemView.findViewById(R.id.project_title);
            projectAuthor = (TextView) itemView.findViewById(R.id.project_author);
            projectDesc = (ExpandableTextView)itemView.findViewById(R.id.project_desc)
                    .findViewById(R.id.expand_text_view);
            divider = itemView.findViewById(R.id.divider);
            overflow = (RelativeLayout) itemView.findViewById(R.id.layout_overflow);
            authority = (ImageView) itemView.findViewById(R.id.cardvire_authority);
            mListener = listener;
            itemView.setOnClickListener(this);
            overflow.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == itemView){
                mListener.onItemClick(v, getAdapterPosition());
            }else if(v == overflow){
                mListener.onOverflowClick(v, getAdapterPosition());
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
    public MineRecyclerAdapter.ProjectHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_mine, viewGroup, false);
        ProjectHolder projectHolder = new ProjectHolder(view, mListener);
        return projectHolder;
    }

    /**
     * 绑定ViewHolder的数据。
     * @param projectHolder
     * @param i 数据源list的下标
     */
    @Override
    public void onBindViewHolder(final MineRecyclerAdapter.ProjectHolder projectHolder, final int i){
        projectHolder.projectTitle.setText(projects.get(i).getName());
        projectHolder.projectAuthor.setText(projects.get(i).getUrlprefix());
        if (tag == 0){
            if (joinModels.size() > 0 ){
                if(joinModels.get(i).getFlag() == 2) {
                    projectHolder.authority.setImageResource(R.drawable.ic_menu_edit);
                } else {
                    projectHolder.authority.setVisibility(View.GONE);
                }
            }

        } else {
            projectHolder.authority.setVisibility(View.GONE);
        }
        if(!projects.get(i).getDesc().isEmpty() ){
            projectHolder.projectDesc.setText(projects.get(i).getDesc());
            projectHolder.divider.setVisibility(View.VISIBLE);
        }
    }

    /**
     * item点击监听函数
     */
    public void setOnItemClickListener(CardviewItemClickListener listener){
        this.mListener = listener;
    }

    public interface CardviewItemClickListener{
        void onItemClick(View view, int position);
        void onOverflowClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
