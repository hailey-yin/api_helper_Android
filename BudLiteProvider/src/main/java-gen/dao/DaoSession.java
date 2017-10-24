package dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import bean.UserDb;
import bean.GroupDb;
import bean.ApiDb;
import bean.ProjectDb;
import bean.LateCollectionDb;
import bean.JoinDb;

import dao.UserDbDao;
import dao.GroupDbDao;
import dao.ApiDbDao;
import dao.ProjectDbDao;
import dao.LateCollectionDbDao;
import dao.JoinDbDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDbDaoConfig;
    private final DaoConfig groupDbDaoConfig;
    private final DaoConfig apiDbDaoConfig;
    private final DaoConfig projectDbDaoConfig;
    private final DaoConfig lateCollectionDbDaoConfig;
    private final DaoConfig joinDbDaoConfig;

    private final UserDbDao userDbDao;
    private final GroupDbDao groupDbDao;
    private final ApiDbDao apiDbDao;
    private final ProjectDbDao projectDbDao;
    private final LateCollectionDbDao lateCollectionDbDao;
    private final JoinDbDao joinDbDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDbDaoConfig = daoConfigMap.get(UserDbDao.class).clone();
        userDbDaoConfig.initIdentityScope(type);

        groupDbDaoConfig = daoConfigMap.get(GroupDbDao.class).clone();
        groupDbDaoConfig.initIdentityScope(type);

        apiDbDaoConfig = daoConfigMap.get(ApiDbDao.class).clone();
        apiDbDaoConfig.initIdentityScope(type);

        projectDbDaoConfig = daoConfigMap.get(ProjectDbDao.class).clone();
        projectDbDaoConfig.initIdentityScope(type);

        lateCollectionDbDaoConfig = daoConfigMap.get(LateCollectionDbDao.class).clone();
        lateCollectionDbDaoConfig.initIdentityScope(type);

        joinDbDaoConfig = daoConfigMap.get(JoinDbDao.class).clone();
        joinDbDaoConfig.initIdentityScope(type);

        userDbDao = new UserDbDao(userDbDaoConfig, this);
        groupDbDao = new GroupDbDao(groupDbDaoConfig, this);
        apiDbDao = new ApiDbDao(apiDbDaoConfig, this);
        projectDbDao = new ProjectDbDao(projectDbDaoConfig, this);
        lateCollectionDbDao = new LateCollectionDbDao(lateCollectionDbDaoConfig, this);
        joinDbDao = new JoinDbDao(joinDbDaoConfig, this);

        registerDao(UserDb.class, userDbDao);
        registerDao(GroupDb.class, groupDbDao);
        registerDao(ApiDb.class, apiDbDao);
        registerDao(ProjectDb.class, projectDbDao);
        registerDao(LateCollectionDb.class, lateCollectionDbDao);
        registerDao(JoinDb.class, joinDbDao);
    }
    
    public void clear() {
        userDbDaoConfig.getIdentityScope().clear();
        groupDbDaoConfig.getIdentityScope().clear();
        apiDbDaoConfig.getIdentityScope().clear();
        projectDbDaoConfig.getIdentityScope().clear();
        lateCollectionDbDaoConfig.getIdentityScope().clear();
        joinDbDaoConfig.getIdentityScope().clear();
    }

    public UserDbDao getUserDbDao() {
        return userDbDao;
    }

    public GroupDbDao getGroupDbDao() {
        return groupDbDao;
    }

    public ApiDbDao getApiDbDao() {
        return apiDbDao;
    }

    public ProjectDbDao getProjectDbDao() {
        return projectDbDao;
    }

    public LateCollectionDbDao getLateCollectionDbDao() {
        return lateCollectionDbDao;
    }

    public JoinDbDao getJoinDbDao() {
        return joinDbDao;
    }

}
