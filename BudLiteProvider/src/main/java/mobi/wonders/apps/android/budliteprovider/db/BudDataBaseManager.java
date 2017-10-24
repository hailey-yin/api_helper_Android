package mobi.wonders.apps.android.budliteprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import java.util.Collection;
import java.util.List;

import dao.DaoMaster;
import dao.DaoSession;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import mobi.wonders.apps.android.budliteprovider.exceptions.GlobalException;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * BudDataBaseManager
 *
 * @author yuqing
 * @date 2015/12/22
 */
public abstract class BudDataBaseManager<M, K> implements BudDataBase<M, K> {

    private static BudDevOpenHelper mHelper;
    private static DaoSession daoSession;
    private static SQLiteDatabase database;
    private static DaoMaster daoMaster;

    protected static Context context;
    protected String dbName;

    public static void initContext(Context c, boolean isDebug) {
        context = c;
        if(isDebug) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    public BudDataBaseManager() {
        this.dbName = BudDatabaseLoader.getInstance().getDbAttrs(context).getDbName();
        getOpenHelper(context, dbName);
    }

    public BudDataBaseManager(String dataBaseName) {
        this.dbName = dataBaseName;
        getOpenHelper(context, dbName);
    }

    /**
     * 打开可读的数据库操作
     * @throws SQLiteException
     */
    protected void openReadableDb() throws SQLiteException {
        getReadableDatabase();
        getDaoMaster();
        getDaoSession();
    }

    /**
     * 打开可写的数据库操作
     * @throws SQLiteException
     */
    protected void openWritableDb() throws SQLiteException {
        getWritableDatabase();
        getDaoMaster();
        getDaoSession();
    }

    protected SQLiteDatabase getWritableDatabase() {
        database = getOpenHelper(context, dbName).getWritableDatabase();
        return database;
    }

    protected SQLiteDatabase getReadableDatabase() {
        database = getOpenHelper(context, dbName).getReadableDatabase();
        return database;
    }

    private BudDevOpenHelper getOpenHelper(Context context, String dataBaseName) {
        if (mHelper == null) {
            if (context == null) {
                throw new GlobalException(GlobalException.APPLICATION_CONTEXT_IS_NULL);
            }
            mHelper = new BudDevOpenHelper(context, dataBaseName, null);
        }
        return mHelper;
    }

    /**
     *  初始化DaoMaster
     * @return DaoMaster对象
     */
    private DaoMaster getDaoMaster() {
        daoMaster = new DaoMaster(database);
        return daoMaster;
    }

    /**
     *  初始化DaoSession
     * @return DaoSession
     */
    protected DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     *  关闭数据库连接对象
     */
    @Override
    public void closeDbConnections() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    /**
     *  清理缓存
     */
    @Override
    public void clearDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    @Override
    public boolean dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // 删除所有表
//            mHelper.onCreate(database); // 创建所有表
//            daoSession.deleteAll(clazz); // 把某表清空
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  插入
     * @param m
     * @return
     */
    @Override
    public boolean insert(M m) {
        try {
            if (m == null) {
                return false;
            }
            openWritableDb();
            getAbstractDao().insert(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 使用事物插入list集合数据
     * @param ms
     * @return
     */
    @Override
    public boolean insertList(List<M> ms) {
        try {
            if (ms == null || TextUtils.isEmpty(ms.toString())) {
                return false;
            }
            openWritableDb();
            getAbstractDao().insertInTx(ms);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  删除
     * @param m
     * @return
     */
    @Override
    public boolean delete(M m) {
        try {
            if (m == null) {
                return false;
            }
            openWritableDb();
            getAbstractDao().delete(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据主键删除
     * @param key
     * @return
     */
    @Override
    public boolean deleteByKey(K key) {
        try {
            if (key.toString() == null || TextUtils.isEmpty(key.toString())) {
                return false;
            }
            openWritableDb();
            getAbstractDao().deleteByKey(key);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  使用事物删除list集合中的数据
     * @param ms
     * @return
     */
    @Override
    public boolean deleteList(List<M> ms) {
        try {
            if (ms == null || TextUtils.isEmpty(ms.toString())) {
                return false;
            }
            openWritableDb();
            getAbstractDao().deleteInTx(ms);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  使用事物删除多条数据
     * @param key
     * @return
     */
    @Override
    public boolean deleteByKeyInTx(K... key) {
        try {
            openWritableDb();
            getAbstractDao().deleteByKeyInTx(key);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  删除所有
     * @return
     */
    @Override
    public boolean deleteAll() {
        try {
            openWritableDb();
            getAbstractDao().deleteAll();
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  插入或修改
     * @param m
     * @return
     */
    @Override
    public boolean insertOrReplace(M m) {
        try {
            if (m == null) {
                return false;
            }
            openWritableDb();
            getAbstractDao().insertOrReplace(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  修改
     * @param m
     * @return
     */
    @Override
    public boolean update(M m) {
        try {
            if (m == null) {
                return false;
            }
            openWritableDb();
            getAbstractDao().update(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  使用事物更新多条
     * @param m
     * @return
     */
    @Override
    public boolean updateInTx(M... m) {
        try {
            if (m == null) {
                return false;
            }
            openWritableDb();
            getAbstractDao().updateInTx(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  使用事物更新list集合的数据
     * @param ms
     * @return
     */
    @Override
    public boolean updateList(List<M> ms) {
        try {
            if (ms == null || TextUtils.isEmpty(ms.toString())) {
                return false;
            }
            openWritableDb();
            getAbstractDao().updateInTx(ms);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  根据主键id加载数据
     * @param key
     * @return
     */
    @Override
    public M selectByPrimaryKey(K key) {
        try {
            openReadableDb();
            return getAbstractDao().load(key);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  加载所有内容
     * @return
     */
    @Override
    public List<M> loadAll() {
        List<M> mList = null;
        try {
            openReadableDb();
            mList = getAbstractDao().loadAll();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     *  重新加载数据库数据
     * @param m
     * @return
     */
    @Override
    public boolean refresh(M m) {
        try {
            if (m == null) {
                return false;
            }
            openWritableDb();
            getAbstractDao().refresh(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  在数据库事物进行线程操作
     * @param runnable
     */
    @Override
    public void runInTx(Runnable runnable) {
        try {
            openReadableDb();
            getDaoSession().runInTx(runnable);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insertOrReplaceList(List<M> ms) {
        try {
            if (ms == null || TextUtils.isEmpty(ms.toString())) {
                return false;
            }
            openWritableDb();
            getAbstractDao().insertOrReplaceInTx(ms);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public QueryBuilder<M> getQueryBuilder() {
        openReadableDb();
        return getAbstractDao().queryBuilder();
    }

    @Override
    public List<M> queryRaw(String where, String... selectionArg) {
        openReadableDb();
        return getAbstractDao().queryRaw(where, selectionArg);
    }

    @Override
    public Query<M> queryRawCreate(String where, Object... selectionArg) {
        openReadableDb();
        return getAbstractDao().queryRawCreate(where, selectionArg);
    }

    @Override
    public Query<M> queryRawCreateListArgs(String where, Collection<Object> selectionArg) {
        openReadableDb();
        return getAbstractDao().queryRawCreateListArgs(where, selectionArg);
    }

}
