package mobi.wonders.apps.android.budrest.model;

/**
 * Created by jiangguangming on 2015/11/24.
 */

public class ProjectModel {
    private String id;//项目id
    private String name;//项目名称
    private String info;//项目简介
    private String desc;//项目描述
    private String urlprefix;//项目路径前缀
    private int status;//项目状态
//    private String version;//项目默认版本
    private int authStatus;//项目权限
    private String virtualUrl;//项目虚拟部署地址
    private String author;//项目创建者
    private int auth;//当前用户对该项目的权限
    private String username;//项目创建者用户名

    public String getid() {
        return id;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrlprefix() {
        return urlprefix;
    }

    public void setUrlprefix(String urlprefix) {
        this.urlprefix = urlprefix;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getVirtualUrl() {
        return virtualUrl;
    }

    public void setVirtualUrl(String virtualUrl) {
        this.virtualUrl = virtualUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
