package mobi.wonders.apps.android.budrest.model;

/**
 * @author zenglan
 * @class ProjectsRequest
 * @brief API 7.4
 * @date 2015/11/16.
 */
public class ProjectsRequest {
    private String _id;
    private String name;//测试API
    private int gid;//分组id
    private String pid;//项目id
    private int deployStatus;
    private String urlsuffix;
    private String gname;//分组名称

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getDeployStatus() {
        return deployStatus;
    }

    public void setDeployStatus(int deployStatus) {
        this.deployStatus = deployStatus;
    }

    public String getUrlsuffix() {
        return urlsuffix;
    }

    public void setUrlsuffix(String urlsuffix) {
        this.urlsuffix = urlsuffix;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }
}
