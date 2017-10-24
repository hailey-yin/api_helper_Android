package mobi.wonders.apps.android.budliteprovider.db;

import java.util.Collection;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p>
 * BudDataBase
 *
 * @author yuqing
 * @date 2015/12/22
 */
public interface BudDataBase<M, K> {

    boolean insert(M m);

    boolean delete(M m);

    boolean deleteByKey(K key);

    boolean deleteList(List<M> mList);

    boolean deleteByKeyInTx(K... key);

    boolean deleteAll();

    boolean insertOrReplace(M m);

    boolean update(M m);

    boolean updateInTx(M... m);

    boolean updateList(List<M> mList);

    M selectByPrimaryKey(K key);

    List<M> loadAll();

    boolean refresh(M m);

    /**
     * Closing available connections
     */
    void closeDbConnections();

    /**
     * 清理缓存
     */
    void clearDaoSession();

    /**
     * Delete all tables and content from our database
     */
    boolean dropDatabase();

    /**
     * 事务
     */
    void runInTx(Runnable runnable);

    /**
     * 获取Dao
     *
     * @return
     */
    AbstractDao<M, K> getAbstractDao();

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertList(List<M> mList);

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertOrReplaceList(List<M> mList);

    /**
     * 自定义查询
     *
     * @return
     */
    QueryBuilder<M> getQueryBuilder();

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    List<M> queryRaw(String where, String... selectionArg);

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    Query<M> queryRawCreate(String where, Object... selectionArg);

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    Query<M> queryRawCreateListArgs(String where, Collection<Object> selectionArg);
}
