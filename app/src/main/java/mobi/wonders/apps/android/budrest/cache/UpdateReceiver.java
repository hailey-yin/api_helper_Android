package mobi.wonders.apps.android.budrest.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * @author zl
 * @class UpdateReceiver
 * @brief
 * @date 2015/11/17.
 */
public class UpdateReceiver extends BroadcastReceiver {
    public UpdateReceiver() {super();}
    public  void onReceive(Context context, Intent intent) {
        // TODO: 2015/12/30 数据库修改
        /*if (MyCache.tableName.equals(ProjectModel.class)) {
            List<ProjectModel> projectModels = new Select().from(ProjectModel.class).execute();
            //删除在本地做的与之相关联的表的数据
            for (int i = 0; i < projectModels.size();i++) {
                LatestModel latestModel = new Select().from(LatestModel.class).where("pid = ?",projectModels.get(i).getid()).executeSingle();
                if (latestModel != null) {
                    latestModel.delete();
                }
                CollectionModel collectionModel = new Select().from(LatestModel.class).where("pid = ?",projectModels.get(i).getid()).executeSingle();
                if (collectionModel != null) {
                    collectionModel.delete();
                }
            }
        }
        new Delete().from(MyCache.tableName).execute();
        // 设置全局定时器(闹钟) outdate秒后再发广播通知本广播接收器触发执行.
        //MyCache.sendUpdateBroadcast(context,MyCache.tableName);*/
    }
}
