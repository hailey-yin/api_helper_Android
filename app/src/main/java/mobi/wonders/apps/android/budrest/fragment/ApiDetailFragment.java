package mobi.wonders.apps.android.budrest.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.adapter.ApiChapterAdapter;
import mobi.wonders.apps.android.budrest.adapter.ApiTitleAdapter;
import mobi.wonders.apps.android.budrest.model.ApiChapterModel;

/**
 * Created by qinyuanmao on 15/12/8.
 */
public class ApiDetailFragment extends Fragment {

    private View mParentView;
    private List<ApiChapterModel> models;
    private LinearLayout mLayoutChooseApi;
    private TextView mTVChooseApi;
    private PopupWindow popupWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_api_detail, container, false);

        //初始化假数据
        models = new ArrayList<ApiChapterModel>();
        for (int i = 0; i < 5; i++) {
            ApiChapterModel model = new ApiChapterModel();
            model.setChapter("用户模块");
            List<ApiChapterModel.Title> lists = new ArrayList<ApiChapterModel.Title>();
            for(int j = 0; j < 7; j++){
                ApiChapterModel.Title title = new ApiChapterModel.Title();
                title.setContent("用户注册");
                lists.add(title);
            }
            model.setTitles(lists);
            models.add(model);
        }

        initView();
        return mParentView;
    }

    private void initView(){
        mLayoutChooseApi = (LinearLayout) mParentView.findViewById(R.id.layout_choose_api);
        mTVChooseApi = (TextView) mParentView.findViewById(R.id.tv_choose_api);
        mTVChooseApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.popup_api_chapter, null);
        // 设置按钮的点击事件
        ListView chapterList = (ListView) contentView.findViewById(R.id.lv_chapter);
        final ListView titleList = (ListView) contentView.findViewById(R.id.lv_title);

        ApiChapterAdapter adapter = new ApiChapterAdapter(getActivity(), models);

        chapterList.setAdapter(adapter);

        chapterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApiTitleAdapter titleAdapter = new ApiTitleAdapter(getActivity(), models.get(position).getTitles());
                titleList.setAdapter(titleAdapter);
            }
        });

        popupWindow = new PopupWindow(contentView,
                carbon.widget.LinearLayout.LayoutParams.MATCH_PARENT, carbon.widget.LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAsDropDown(mLayoutChooseApi);
        popupWindow.update();

    }
}
