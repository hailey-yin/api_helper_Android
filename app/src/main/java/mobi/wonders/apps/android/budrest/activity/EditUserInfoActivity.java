package mobi.wonders.apps.android.budrest.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.RestMsg;

import org.apache.commons.lang.StringUtils;

import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.R;
import mobi.wonders.apps.android.budrest.api.APIProvider;
import mobi.wonders.apps.android.budrest.cache.DBManager;
import mobi.wonders.apps.android.budrest.utils.DES3Utils;
import mobi.wonders.apps.android.budrest.utils.DialogUtil;

public class EditUserInfoActivity extends AppCompatActivity {

    private EditText mOldPwb;
    private EditText mNewPwb;
    private EditText mNewPwbSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EditUserInfoActivity.this.finish();
                break;
            case R.id.done : {
                String oldPwb = mOldPwb.getText().toString().trim();
                String newPwb = mNewPwb.getText().toString().trim();
                String newPwbSec = mNewPwbSec.getText().toString().trim();
                if(StringUtils.isEmpty(oldPwb)){
                    AlertDialog dialog = new AlertDialog.Builder(EditUserInfoActivity.this).create();;
                    dialog = DialogUtil.setAnimDialog(dialog);
                    dialog.setMessage("原密码不能为空！");
                    dialog.show();
                } else if(StringUtils.isEmpty(newPwb)){
                    AlertDialog dialog = new AlertDialog.Builder(EditUserInfoActivity.this).create();;
                    dialog = DialogUtil.setAnimDialog(dialog);
                    dialog.setMessage("请输入新密码！");
                    dialog.show();
                } else if(StringUtils.isEmpty(newPwbSec)){
                    AlertDialog dialog = new AlertDialog.Builder(EditUserInfoActivity.this).create();;
                    dialog = DialogUtil.setAnimDialog(dialog);
                    dialog.setMessage("请再次输入密码！");
                    dialog.show();
                } else if (!newPwb.equals(newPwbSec)) {
                    AlertDialog dialog = new AlertDialog.Builder(EditUserInfoActivity.this).create();;
                    dialog = DialogUtil.setAnimDialog(dialog);
                    dialog.setMessage("两次密码输入不一致！");
                    dialog.show();
                } else {
                    APIProvider.getInstance().editUserInfo(oldPwb, newPwb, new APICallBack<RestMsg>() {
                        @Override
                        public void onSuccess(RestMsg data) {
                            AlertDialog dialog = new AlertDialog.Builder(EditUserInfoActivity.this).create();;
                            dialog = DialogUtil.setAnimDialog(dialog);
                            dialog.setMessage("修改成功！");
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditUserInfoActivity.this.finish();
                                }
                            });
                            dialog.show();

                            DBManager.getInstance().delUserInfoByName(DES3Utils.encryptMode(MyApplication.currentUser));

                            // TODO: 2015/12/29 新数据库测试，删除旧代码
                            /*//修改密码成功之后，将原来数据库的账号更改，重新登陆
                            new Delete().from(UserModel.class).where("name = ?", DES3Utils.encryptMode(MyApplication.currentUser)).execute();*/
                            //TODO:删除缓存中的该用户

                            MyApplication.isLogin = false;
                            MyApplication.currentUser = null;

                            //清空sp中的用户名和密码
                            SharedPreferences sp = getSharedPreferences(LoginActivity.TAG, Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(LoginActivity.USER, null);
                            editor.putString(LoginActivity.PWD, null);
                            editor.commit();
                        }

                        @Override
                        public void onFailure(RestMsg data) {
                            AlertDialog dialog = new AlertDialog.Builder(EditUserInfoActivity.this).create();;
                            dialog = DialogUtil.setAnimDialog(dialog);
                            dialog.setMessage(data.getMessage());
                            dialog.show();
                        }
                    });
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

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("修改密码");

        mNewPwb = (EditText) findViewById(R.id.et_new_pwb);
        mNewPwbSec = (EditText) findViewById(R.id.et_new_pwb_sec);
        mOldPwb = (EditText) findViewById(R.id.et_old_pwb);
    }
}
