package mobi.wonders.apps.android.budrest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.model.ProjectVO;

/**
 * Created by yhl on 2015/11/26.
 */
public class AttentionRecyclerAdapter  extends android.support.v7.widget.RecyclerView.Adapter<AttentionRecyclerAdapter.ProjectHolder>{
    private List<ProjectVO> mProjects;
    private Context mContext;
    private CardviewItemClickListener mListener;

    public AttentionRecyclerAdapter(List<ProjectVO> projects, Context context){
        this.mProjects = projects;
        this.mContext = context;
    }

    //自定义ViewHolder类
    static class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView projectTitle;
        TextView projectAuthor;
        ImageView star;
        CardviewItemClickListener mListener;

        public ProjectHolder(View itemView, CardviewItemClickListener listener) {
            super(itemView);
            projectTitle = (TextView) itemView.findViewById(R.id.project_title);
            projectAuthor = (TextView) itemView.findViewById(R.id.project_author);
            star = (ImageView) itemView.findViewById(R.id.star_image);
            mListener = listener;
            itemView.setOnClickListener(this);
            star.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == itemView){
                mListener.onItemClick(v, getAdapterPosition());
            }else if(v == star){
                mListener.onStarClick(v, getAdapterPosition());
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
    public ProjectHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_atttention, viewGroup, false);
        ProjectHolder projectHolder = new ProjectHolder(view, mListener);
        return projectHolder;
    }

    /**
     * 绑定ViewHolder的数据。
     * @param projectHolder
     * @param i 数据源list的下标
     */
    @Override
    public void onBindViewHolder(final AttentionRecyclerAdapter.ProjectHolder projectHolder, final int i){
        projectHolder.projectTitle.setText(mProjects.get(i).getName());
        projectHolder.projectAuthor.setText(mProjects.get(i).getInfo());

    }

    /**
     * item点击监听函数
     */
    public void setOnItemClickListener(CardviewItemClickListener listener){
        this.mListener = listener;
    }

    public interface CardviewItemClickListener{
        void onItemClick(View view, int position);
        void onStarClick(View view, int positino);
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }
}
