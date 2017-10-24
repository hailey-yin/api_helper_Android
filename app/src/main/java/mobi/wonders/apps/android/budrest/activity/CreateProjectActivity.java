package mobi.wonders.apps.android.budrest.activity;

import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import carbon.widget.LinearLayout;
import carbon.widget.RelativeLayout;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;


public class CreateProjectActivity extends AppCompatActivity {

    private EditText mProName, mProPrefix, mProBrief, mProDescription;
    private LinearLayout mLayoutPrivilege;
    private TextView mProPrivilege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                CreateProjectActivity.this.finish();
                break;
            case R.id.done :{
                ProgramAttribute pa = createClass();
                if(!"".equals(pa.name)){
                    APIProvider.getInstance().createProject(pa.name, pa.prefix, pa.brief, pa.privilege, pa.description, new APICallBack<RestMsg>() {
                        @Override
                        public void onSuccess(RestMsg data) {
                            AlertDialog dialog = new AlertDialog.Builder(CreateProjectActivity.this).create();;
                            dialog = DialogUtil.setAnimDialog(dialog);
                            dialog.setMessage("提交成功！");
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CreateProjectActivity.this.finish();
                                }
                            });
                            dialog.show();
                            //TODO:获取网络数据，跟新数据库
                        }

                        @Override
                        public void onFailure(RestMsg data) {

                        }
                    });
                }else {
                    AlertDialog dialog = new AlertDialog.Builder(CreateProjectActivity.this).create();
                    dialog = DialogUtil.setAnimDialog(dialog);
                    dialog.setMessage("项目名不能为空！");
                    dialog.show();
                }
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_project_menu, menu);
        return true;
    }

    private void initView()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("新建项目");

        mProName = (EditText) findViewById(R.id.et_program_name);
        mProBrief = (EditText) findViewById(R.id.et_program_brief);
        mProPrefix = (EditText) findViewById(R.id.et_program_prefix);
        mProPrivilege = (TextView) findViewById(R.id.et_program_privilege);
        mProDescription = (EditText) findViewById(R.id.et_program_description);
        mLayoutPrivilege = (LinearLayout) findViewById(R.id.layout_program_privilege);

        mLayoutPrivilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

    }
    private void showPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(CreateProjectActivity.this).inflate(
                R.layout.popup_window_creat_project, null);
        // 设置按钮的点击事件
        RelativeLayout layoutPublic = (RelativeLayout) contentView.findViewById(R.id.layout_public);
        RelativeLayout layoutPrivate = (RelativeLayout) contentView.findViewById(R.id.layout_private);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                mLayoutPrivilege.getWidth() , LinearLayout.LayoutParams.WRAP_CONTENT, true);
        layoutPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProPrivilege.setText("仅自己可见");
                popupWindow.dismiss();
            }
        });

        layoutPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProPrivilege.setText("所有人可见");
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.showAsDropDown(mLayoutPrivilege);

    }

    private ProgramAttribute createClass() {
        ProgramAttribute pa = new ProgramAttribute();
        pa.name = mProName.getText().toString().trim();
        pa.brief = mProBrief.getText().toString().trim();
        pa.description = mProDescription.getText().toString().trim();
        pa.prefix = mProPrefix.getText().toString().trim();
        pa.privilege = mProPrivilege.getText().toString().trim().equals("所有人可见") ? "1" : "2";
        return pa;
    }

    class ProgramAttribute {
        String name;
        String prefix;
        String brief;
        String privilege;
        String description;
    }

}
