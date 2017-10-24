package mobi.wonders.apps.android.budrest.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import carbon.widget.TextView;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.adapter.ErrorAdapter;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;

public class ApiTestActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView mFeedBack;
    private TextView mTestError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);
        initView();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("API测试");
        mFeedBack = (TextView) findViewById(R.id.btn_feedback);
        mTestError = (TextView) findViewById(R.id.btn_test_error);
        mFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFeedBack();
            }
        });
        mTestError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTestError();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.api_test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                ApiTestActivity.this.finish();
                break;
            case R.id.feedback :
                showFeedbackWindow();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFeedbackWindow() {
        View contentView = LayoutInflater.from(ApiTestActivity.this).inflate(
                R.layout.popup_bug_report, null);

        PopupWindow window = new PopupWindow(contentView,
                carbon.widget.LinearLayout.LayoutParams.MATCH_PARENT, carbon.widget.LinearLayout.LayoutParams.WRAP_CONTENT, true);

        window.setTouchable(true);

        window.setBackgroundDrawable(new BitmapDrawable());
        window.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        window.setAnimationStyle(R.style.PopupAnimation);
        window.showAtLocation(toolbar, 0, 0, 0);
        window.update();
    }
    
    //显示错误反馈窗口
    private void showFeedBack() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.popup_bug_report, (ViewGroup) findViewById(R.id.popup_bug_report));
        ImageView feedback = (ImageView) layout.findViewById(R.id.to_feedback);
        ImageView back = (ImageView) layout.findViewById(R.id.cancel_search);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 反馈api 
            }
        });
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ApiTestActivity.this).setView(layout);
        final AlertDialog aleartDialog = dialogBuilder.create();
        DialogUtil.setAnimDialog(aleartDialog);
        aleartDialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aleartDialog.dismiss();
            }
        });
    }

    //显示
    private void showTestError() {
        LayoutInflater inflater1 = getLayoutInflater();
        View listDialogLayout = inflater1.inflate(R.layout.test_dialog, (ViewGroup) findViewById(R.id.list_dialog));
        ListView listView = (ListView) listDialogLayout.findViewById(R.id.list);
        List<String> data = new ArrayList<String>();
        data.add("网络检测");
        data.add("服务器连接检测");
        data.add("Ping检测");
        data.add("HTTP CODE检测");
        data.add("服务器返回值检测");
        Log.i("list", data.size() + "");
        ErrorAdapter adapter = new ErrorAdapter(ApiTestActivity.this,data);
        listView.setAdapter(adapter);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ApiTestActivity.this).setView(listDialogLayout);
        dialogBuilder.setPositiveButton("去检测",null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
