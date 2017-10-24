package mobi.wonders.apps.android.budrest.cache;

import java.util.ArrayList;
import java.util.List;

import bean.JoinDb;
import bean.LateCollectionDb;
import bean.ProjectDb;
import bean.UserDb;
import dao.JoinDbDao;
import dao.LateCollectionDbDao;
import dao.ProjectDbDao;
import dao.UserDbDao;
import mobi.wonders.apps.android.budrest.db.ApiDbManager;
import mobi.wonders.apps.android.budrest.db.GroupDbManager;
import mobi.wonders.apps.android.budrest.db.JoinDbManager;
import mobi.wonders.apps.android.budrest.db.LateCollectionDbManager;
import mobi.wonders.apps.android.budrest.db.ProjectDbManager;
import mobi.wonders.apps.android.budrest.db.UserDbManager;
import mobi.wonders.apps.android.budrest.model.ProjectModel;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.utils.LogUtil;

/**
 * <p>
 * Title:CMS_[所属模块]_[标题]
 * </p>
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * <p/>
 * DBManager
 *
 * @author yuqing
 * @date 2015/12/29
 */
public class DBManager {
    private static DBManager instance;

    private UserDbManager mUserDao;
    private JoinDbManager mJoinDao;
    private ProjectDbManager mProjectDao;
    private LateCollectionDbManager mLateCollectionDao;
    private GroupDbManager mGroupDao;
    private ApiDbManager mApiDao;

    public static final int TYPE_LATEOPEN = 1;
    public static final int TYPE_COLLECTION = 2;

    public static final int TYPE_JOIN = 0;
    public static final int TYPE_CREATE = 1;

    public DBManager() {
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();

            instance.mUserDao = new UserDbManager();
            instance.mJoinDao = new JoinDbManager();
            instance.mProjectDao = new ProjectDbManager();
            instance.mLateCollectionDao = new LateCollectionDbManager();
            instance.mGroupDao = new GroupDbManager();
            instance.mApiDao = new ApiDbManager();
        }
        return instance;
    }

    /**
     *  保存用户名和密码
     * @param username
     * @param pwd
     * @return
     */
    public boolean savaUserInfo(String username, String pwd) {
        return mUserDao.insertOrReplace(new UserDb(null, username, pwd, null));
    }

    /**
     *  获取指定用户信息
     * @return
     */
    public UserDb getUserByName(String username) {
        return mUserDao.getQueryBuilder().where(UserDbDao.Properties.Username.eq(username)).build().unique();
    }

    /**
     *  获取所有用户信息
     * @return
     */
    public List<UserDb> getUserInfo() {
        return mUserDao.getQueryBuilder().orderDesc(UserDbDao.Properties.Id).build().list();
    }

    /**
     *  删除指定username的数据
     * @param username
     */
    public void delUserInfoByName(String username) {
        mUserDao.getQueryBuilder().where(UserDbDao.Properties.Username
                .eq(username)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     *  我参与的
     * @param jds
     * @return
     */
    public boolean saveJoin(List<JoinDb> jds) {
        return mJoinDao.insertOrReplaceList(jds);
    }

    /**
     * 通过username获取我参加的项目列表
     * @param username
     * @return
     */
    public List<JoinDb> getJoin(String username,int flag) {
        return mJoinDao.getQueryBuilder().where(JoinDbDao.Properties.Username.eq(username),JoinDbDao.Properties.Flag.eq(flag))
                .build().list();
    }

    /**
     *  根据用户名来删除我参与的项目
     * @param username
     * @return
     */
    public boolean delJoin(String username,int flag) {
        try {
            mJoinDao.getQueryBuilder().where(JoinDbDao.Properties.Username
                    .eq(username),JoinDbDao.Properties.Flag.eq(flag)).buildDelete().executeDeleteWithoutDetachingEntities();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据id和flag删除jion表中的数据
     * @param pid
     * @param flag
     * @return
     */
    public boolean delJoinByPidFlag(String pid,int flag) {
        try {
            mJoinDao.getQueryBuilder().where(JoinDbDao.Properties.Pid
                    .eq(pid),JoinDbDao.Properties.Flag.eq(flag)).buildDelete().executeDeleteWithoutDetachingEntities();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertJoinDb(JoinDb joinDb) {
        return mJoinDao.insert(joinDb);
    }

    /**
     * 删除我参加的所有数据
     * @return
     */
    public boolean deletAllJoin() {
        return mJoinDao.deleteAll();
    }

    /**
     *  根据项目id获取我参与
     * @param pid
     * @return
     */
    public JoinDb getJoinByPID(String pid) {
        return mJoinDao.getQueryBuilder().where(JoinDbDao.Properties.Pid.eq(pid)).build().unique();
    }

    /**
     * 根据我参加的项目的获取项目表中的项目再转化为VO数组并返回
     * @param joinDbs
     * @return
     */
    public List<ProjectVO> getProjectVos(List<JoinDb> joinDbs) {
        List<ProjectVO> Vos = new ArrayList<ProjectVO>();
        for (JoinDb joinDb :joinDbs) {
            ProjectVO projectVO = new ProjectVO();
            if (getProjectsByPID(joinDb.getPid()) != null) {
                projectVO = getProjectsByPID(joinDb.getPid());
                Vos.add(projectVO);
            }
        }
        return Vos.isEmpty()?null:Vos;
    }

    /**
     *  保存项目列表
     * @param pds
     * @return
     */
    public boolean saveProjects(List<ProjectVO> pds){
        for (ProjectVO vo : pds) {
            mProjectDao.getQueryBuilder().where(ProjectDbDao.Properties.Pid.eq(vo.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
        }
        return mProjectDao.insertOrReplaceList(ChangeToDBs(pds));
    }

    /**
     *  获取所有项目信息
     * @return
     */
    public List<ProjectVO> getProjects() {
        return ChangeToVOs(mProjectDao.loadAll());
    }

    /**
     *  根据作者创建查找项目
     * @param author
     * @return
     */
    public List<ProjectVO> getProjectsByAuthor(String author) {
        List<ProjectDb> pds = mProjectDao.getQueryBuilder().where(ProjectDbDao.Properties.Author
                .eq(author)).build().list();
        return ChangeToVOs(pds);
    }

    /**
     *  获取项目信息 根据name字段排序
     * @return
     */
    public List<ProjectVO> getProjectsOrderByName() {
        List<ProjectDb> pds = mProjectDao.getQueryBuilder().orderAsc(ProjectDbDao.Properties.Name).list();
        return ChangeToVOs(pds);
    }

    /**
     *  根据项目id获取项目
     * @param PID
     * @return
     */
    public ProjectVO getProjectsByPID(String PID) {
        ProjectDb pd = new ProjectDb();
        ProjectVO pv = new ProjectVO();
        pd = mProjectDao.getQueryBuilder().where(ProjectDbDao.Properties.Pid.eq(PID)).build().unique();
        if (pd != null) {
            pv = ChangeToVO(pd);
        }else {
            pv = null;
        }
        return pv;
    }

    /**
     *  根据username和flag获取最近打开或者搜藏的所有项目
     * @param username,flag
     * @return
     */
    public List<LateCollectionDb> getLatesOrCollections(String username,int flag) {
        List<LateCollectionDb> lcs = new ArrayList<>();
        lcs = mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Username.eq(username),
                LateCollectionDbDao.Properties.Flag.eq(flag))
                .build()
                .list();
        return lcs.isEmpty()?null:lcs;
    }

    /**
     * 根据flag查询收藏的项目
     * @param flag
     * @return
     */
    public List<LateCollectionDb> getLatesOrCollections(int flag) {
        List<LateCollectionDb> lcs = new ArrayList<>();
        lcs = mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Flag.eq(flag))
                .build()
                .list();
        return lcs.isEmpty()?null:lcs;
    }

    /**
     * 根据收藏表项目的获取项目表中的项目再转化为VO数组并返回
     * @param lcs
     * @return
     */
    public List<ProjectVO> getLCProjectVos(List<LateCollectionDb> lcs) {
        List<ProjectVO> Vos = new ArrayList<ProjectVO>();
        for (LateCollectionDb lc :lcs) {
            ProjectVO projectVO = new ProjectVO();
            if (getProjectsByPID(lc.getPid()) != null) {
                projectVO = getProjectsByPID(lc.getPid());
                Vos.add(projectVO);
            }
        }
        return Vos.isEmpty()?null:Vos;
    }

    /**
     *  根据pid和flag获取最近打开或收藏的项目
     * @param pid
     * @return
     */
    public LateCollectionDb getLateOrCollection(String pid,int flag) {
        return mLateCollectionDao.getQueryBuilder()
                .where(LateCollectionDbDao.Properties.Pid.eq(pid), LateCollectionDbDao.Properties.Flag.eq(flag))
                .build().unique();
    }


    /**
     *  根据项目id删除项目缓存
     * @param pid
     * @return
     */
    public boolean delProjectByPID(String pid) {
        try{
            mProjectDao.getQueryBuilder().where(ProjectDbDao.Properties.Pid
                    .eq(pid))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 清除所有项目缓存
     * @return
     */
    public boolean delAllProject() {
        return mProjectDao.deleteAll();
    }

    /**
     *  保存 最近打开或收藏 的项目属性
     * @param lc
     * @return
     */
    public boolean saveLateOrCollect(LateCollectionDb lc) {
        return mLateCollectionDao.insert(lc);
    }

    /**
     *  删除最近打开或者收藏的项目
     * @param pid
     * @return
     */
    public boolean delCollectOrLatel(String pid,int flag) {
        try{
            mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Pid
                    .eq(pid),LateCollectionDbDao.Properties.Flag.eq(flag))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据pid删除最近打开或收藏表的记录
     * @param pid
     * @return
     */
    public boolean delCollectOrLatelyByPid(String pid) {
        try{
            mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Pid
                    .eq(pid))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  保存最近打开的项目
     * @param pjs
     * @param position
     * @return
     */
    public boolean saveCollectionProject(List<ProjectVO> pjs, int position) {
        try {

            int maxSize = 10; // 最近打开保存最大数量
            int earlyPositon = 0; // 记录最早保存的位置

            List<LateCollectionDb> lcsCache = mLateCollectionDao.loadAll();
            int lcsCacheLen = lcsCache.size();
            if(lcsCacheLen > maxSize) {
                mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Pid
                        .eq(lcsCache.get(earlyPositon).getPid()))
                        .buildDelete().executeDeleteWithoutDetachingEntities();
                earlyPositon++;
            }

            LateCollectionDb lc = mLateCollectionDao.getQueryBuilder()
                    .where(LateCollectionDbDao.Properties.Pid
                            .eq(pjs.get(position).getId())).build().unique();

            if(lc == null) {
                if(maxSize >= 10) {
                    mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Pid
                            .eq(lcsCache.get(earlyPositon).getPid()))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                }
            }else {
                mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Pid
                        .eq(pjs.get(position).getId()))
                        .buildDelete().executeDeleteWithoutDetachingEntities();
            }
            mLateCollectionDao.insert(new LateCollectionDb(null, pjs.get(position).getUsername(),
                    pjs.get(position).getId(), TYPE_COLLECTION));
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  保存最近打开的项目 保存最多十条
     * @param pjs
     * @param position
     * @return
     */
    public boolean saveLatestProject(List<ProjectVO> pjs, int position) {
        try {

            int maxSize = 10; // 最近打开保存最大数量

            List<LateCollectionDb> lcsCache = mLateCollectionDao.getQueryBuilder()
                    .where(LateCollectionDbDao.Properties.Flag.eq(TYPE_LATEOPEN))
                    .orderDesc(LateCollectionDbDao.Properties.Id).build().list();

            LateCollectionDb lc = mLateCollectionDao.getQueryBuilder()
                    .where(LateCollectionDbDao.Properties.Pid
                            .eq(pjs.get(position).getId())).build().unique();

            if(lc == null) {
                if(lcsCache.size() >= maxSize) {
                    mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Pid
                            .eq(lcsCache.get(maxSize-1).getPid()))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                }
            }else {
                mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Pid
                        .eq(pjs.get(position).getId()))
                        .buildDelete().executeDeleteWithoutDetachingEntities();
            }

            mLateCollectionDao.insert(new LateCollectionDb(null, pjs.get(position).getUsername(),
                    pjs.get(position).getId(), TYPE_LATEOPEN));

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  获取最近打开的项目
     * @return zxzzz
     */
    public List<ProjectVO> getLatestProject() {
        List<LateCollectionDb> list = mLateCollectionDao.getQueryBuilder().where(LateCollectionDbDao.Properties.Flag.eq(1))
                .orderDesc(LateCollectionDbDao.Properties.Id).build().list();
        List<ProjectDb> pros = new ArrayList<ProjectDb>();
        for(int i = 0; i < list.size(); i++) {
            ProjectDb pro = mProjectDao.getQueryBuilder().where(
                    ProjectDbDao.Properties.Pid.eq(list.get(i).getPid())).build().unique();
            pros.add(pro);
        }
        return ChangeToVOs(pros);
    }

    /**
     *  查询项目从本地数据库
     * @param searchText
     * @return
     */
    public List<ProjectVO> getFromSearch(String searchText) {
        // TODO: 2015/12/29 测试like字段
        List<ProjectDb> dbs = mProjectDao.getQueryBuilder().where(ProjectDbDao.Properties.Name.like("%"+searchText+"%")).build().list();
        return ChangeToVOs(dbs);
    }

    /**
     *  Object数据类型转换
     */
    private ProjectVO ChangeToVO(ProjectDb pd) {
        ProjectVO pm = new ProjectVO();
        pm.setId(pd.getPid());
        pm.setName(pd.getName());
        pm.setInfo(pd.getInfo());
        pm.setDesc(pd.getDesc());
        pm.setUrlprefix(pd.getUrlprefix());
        pm.setStatus(pd.getStatus());
        pm.setAuthStatus(pd.getAuthorStatus());
        pm.setVirtualUrl(pd.getVirtualUrl());
        pm.setAuthor(pd.getAuthor());
        pm.setAuth(pd.getAuthority());
        pm.setUsername(pd.getUsername());
        return pm;
    }

    /**
     *  Object数据类型转换
     */
    private ProjectDb ChangeToDB(ProjectVO pd) {
        ProjectDb pm = new ProjectDb();
        pm.setPid(pd.getId());
        pm.setName(pd.getName());
        pm.setInfo(pd.getInfo());
        pm.setDesc(pd.getDesc());
        pm.setUrlprefix(pd.getUrlprefix());
        pm.setStatus(pd.getStatus());
        pm.setAuthorStatus(pd.getAuthStatus());
        pm.setVirtualUrl(pd.getVirtualUrl());
        pm.setAuthor(pd.getAuthor());
        pm.setAuthority(pd.getAuth());
        pm.setUsername(pd.getUsername());
        return pm;
    }

    /**
     *  List数据类型转换
     */
    private List<ProjectVO> ChangeToVOs(List<ProjectDb> pds) {
        // 组装数据库数据
        List<ProjectVO> pms = new ArrayList<>();
        if(pds != null){
            for (ProjectDb p : pds) {
                if (p != null){
                    ProjectVO pm = new ProjectVO();
                    pm.setId(p.getPid());
                    pm.setName(p.getName());
                    pm.setInfo(p.getInfo());
                    pm.setDesc(p.getDesc());
                    pm.setUrlprefix(p.getUrlprefix());
                    pm.setStatus(p.getStatus());
                    pm.setAuthStatus(p.getAuthorStatus());
                    pm.setVirtualUrl(p.getVirtualUrl());
                    pm.setAuthor(p.getAuthor());
                    pm.setAuth(p.getAuthority());
                    pm.setUsername(p.getUsername());
                    pms.add(pm);
                }
            }
        }
        return pms;
    }

    /**
     *  List数据类型转换
     */
    private List<ProjectDb> ChangeToDBs(List<ProjectVO> pds) {
        // 组装数据库数据
        List<ProjectDb> pms = new ArrayList<>();
        for (ProjectVO p : pds) {
            ProjectDb pm = new ProjectDb();
            pm.setPid(p.getId());
            pm.setName(p.getName());
            pm.setInfo(p.getInfo());
            pm.setDesc(p.getDesc());
            pm.setUrlprefix(p.getUrlprefix());
            pm.setStatus(p.getStatus());
            pm.setAuthorStatus(p.getAuthStatus());
            pm.setVirtualUrl(p.getVirtualUrl());
            pm.setAuthor(p.getAuthor());
            pm.setAuthority(p.getAuth());
            pm.setUsername(p.getUsername());
            pms.add(pm);
        }
        return pms;
    }

    /**
     *  清除所有数据
     * @return
     */
    public boolean delAll() {
        return mUserDao.deleteAll() && mJoinDao.deleteAll() && mProjectDao.deleteAll();
    }


}
