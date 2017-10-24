package mobi.wonders.apps.android.budrest.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;

import mobi.wonders.apps.android.budrest.R;

/**
 * dialog工具类
 * Created by jiangguangming on 2015/11/11.
 */
public class DialogUtil {

    /*//提示框，显示网络断开提示
    public static void showBasicDialog(Context context, int content) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(R.string.notice)
                .setMessage(content)
                .setPositiveButton(R.string.know, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    //显示协议
    public static void showProtocol(Context context){
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(R.string.protocol)
                .setMessage(R.string.protocol_content)
                .setPositiveButton(R.string.protocol, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }*/

    public static AlertDialog setAnimDialog(AlertDialog alertDialog){
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        alertDialog.setCanceledOnTouchOutside(true);

        return alertDialog;
    }
}
