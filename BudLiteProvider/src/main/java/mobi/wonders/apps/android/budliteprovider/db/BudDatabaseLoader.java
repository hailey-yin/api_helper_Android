package mobi.wonders.apps.android.budliteprovider.db;

import android.content.Context;

import dao.DaoMaster;
import dao.DaoSession;
import de.greenrobot.dao.query.QueryBuilder;
import mobi.wonders.apps.android.budliteprovider.exceptions.GlobalException;
import mobi.wonders.apps.android.budliteprovider.parser.BudLiteAttr;
import mobi.wonders.apps.android.budliteprovider.parser.BudLiteParser;


/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * BudDatabaseLoader
 *
 * @author yuqing
 * @date 2015/12/22
 */
public class BudDatabaseLoader {

    private static BudDatabaseLoader instance;
    private static Context appContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private BudLiteAttr liteAttr;

    private BudDatabaseLoader() {
    }

    public static BudDatabaseLoader getInstance() {
        if (instance == null) {
            instance = new BudDatabaseLoader();
        }
        return instance;
    }

    public void initContext(Context context, boolean isDebug) {
        appContext = context;
        if(isDebug) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    /**
     * 取得DaoMaster
     * @param context
     * @return
     */
    protected DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            if (context == null) {
                throw new GlobalException(GlobalException.APPLICATION_CONTEXT_IS_NULL);
            }
            BudDevOpenHelper helper = new BudDevOpenHelper(context, getDbAttrs(context).getDbName() , null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     * @return
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(appContext);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     *  获取解析xml文件属性
     * @return
     */
    public BudLiteAttr getDbAttrs(Context context) {
        if(liteAttr == null) {
            BudLiteParser.parseBudLiteConfiguration(context);
            liteAttr = BudLiteAttr.getInstance();
        }
        return liteAttr;
    }
}
