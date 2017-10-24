package mobi.wonders.apps.android.budrest.db;

import bean.GroupDb;
import de.greenrobot.dao.AbstractDao;
import mobi.wonders.apps.android.budliteprovider.db.BudDataBaseManager;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p/>
 * GroupDbManager
 *
 * @author yuqing
 * @date 2016/1/4
 */
public class GroupDbManager extends BudDataBaseManager<GroupDb, Long> {

    @Override
    public AbstractDao<GroupDb, Long> getAbstractDao() {
        return getDaoSession().getGroupDbDao();
    }
}
