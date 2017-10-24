package mobi.wonders.apps.android.budrest.api;

import com.woders.apiprovider.APICallBack;
import com.woders.apiprovider.APIType;
import com.woders.apiprovider.APIUtil;

import java.util.HashMap;
import java.util.Map;

import mobi.wonders.apps.android.budrest.MyApplication;
import mobi.wonders.apps.android.budrest.model.AccountVO;
import mobi.wonders.apps.android.budrest.model.GroupVO;
import mobi.wonders.apps.android.budrest.model.ProjectGroupVO;
import mobi.wonders.apps.android.budrest.model.ProjectMemberVO;
import mobi.wonders.apps.android.budrest.model.ProjectVO;
import mobi.wonders.apps.android.budrest.model.ProjectRequestVO;

/**
 * Created by jiangguangming on 2015/10/29.
 */
public class APIProvider {

    final static String PREFIX = "/budrest/app";
    //final static String SERVER = "192.168.1.47";
    final static String SERVER = "192.168.1.37";//jiangguangming服务器
    //final static String SERVER = "192.168.1.67";//zl服务器
    //final static String SERVER = "192.168.1.4";
    //final static String SERVER = "10.1.64.88";//外网服务器
    final static String PORT = "3000";

    //上海服务器
    /*final static String SERVER = "10.1.64.88";
    final static String PORT = "3001";*/

    public static String getBaseDir() {
        return "http://" + SERVER + ":" + PORT +  PREFIX;
    }

    /**
     * 1.1 注册
     */
    public static String registerUrl() {
        return getBaseDir() + "/account/register";
    }

    /**
     * 1.2 登陆
     */
    public static String loginURL() {
        return getBaseDir() + "/account/login";
    }

    /**
     * 1.3 获取用户信息
     */
    public static String getUserURL() {
        return getBaseDir() + "/account/accountinfo";
    }

    /**
     * 1.4 修改个人信息
     */
    public static String editUserURL() {
        return getBaseDir() + "/account/accountinfo";
    }

    /**
     * 1.5 退出登录
     */
    public static String logoutURL() {
        return getBaseDir() + "/account/logout";
    }

    /**
     * 2.1 新增项目
     */
    public static String addProjectsURL() {
        return getBaseDir() + "/projects";
    }

    /**
     * 2.2 批量获取项目
     */
    public static String getProjectsURL() {
        return getBaseDir() + "/projects";
    }

    /**
     * 2.3  修改项目
     */
    public static String editProjectURL(String pid) {
        return getBaseDir() + "/projects/" + pid;
    }

    /**
     * 2.4 删除项目
     */
    public static String deleteProjectURL(String pid) {
        return getBaseDir() + "/projects/" + pid;
    }

    /**
     * 2.5 更新项目虚拟部署地址
     */
    public static String updateURL(String name) {
        return getBaseDir() + "/projects/updateUrl/" + name;
    }

    /**
     * 2.6 获取项目详情
     */
    public static String getProjectDetailsURL(String pid) {
        return getBaseDir() + "/projects/" + pid;
    }

    /**
     * 3.1 测试API
     */
    public static String testApiURL() {
        return getBaseDir() + "/apitest";
    }

    /**
     * 4.1 获取项目下的用户
     */
    public static String authorityUserURL() {
        return getBaseDir() + "/authority";
    }

    /**
     * 4.2 新增该项目下的用户
     */
    public static String addauthorityUserURL() {
        return getBaseDir() + "/authority";
    }

    /**
     * 4.3	删除该项目下的用户
     */
    public static String deleteAuthorityUserURL(String uid) {
        //FIXME
        return getBaseDir() + "/authority/" + uid;
    }

    /**
     *5.1 获取项目分组
     */
    public static String getProjectGroupURL(String pid) {
        return getBaseDir() + "/groups/" + pid;
    }

    /**
     *5.2 新增项目分组
     */
    public static String addProjectGroupURL(String pid) {
        return getBaseDir() + "/groups/" + pid;
    }

    /**
     * 5.3 获取指定分组详情
     */
    public static String getGroupDetailsURL(String pid, int gid) {
        return getBaseDir() + "/groups/" + pid + " /" + gid;
    }

    /**
     * 5.4 修改分组名
     */
    public static String editProjectGroupURL(String pid, int gid) {
        return getBaseDir() + "/groups/" + pid + "  /" + gid;
    }

    /**
     * 5.5 删除项目分组
     */
    public static String deleteProjectGroupURL(String pid, int gid) {
        return getBaseDir() + "/groups/" + pid + " /" + gid;
    }

    /**
     * 6.1 添加项目API
     */
    public static String addProjectRestURL(String pid) {
        return getBaseDir() + "/request/" + pid;
    }

    /**
     * 6.2 获取项目指定分组下所有API
     */
    public  static String getProjectRestURL(String pid) {
        return getBaseDir() + "/request/" + pid;
    }

    /**
     * 6.3 获取项目指定的API
     */
    public static String getTargetProjectApiURL(String pid, String rid) {
        return getBaseDir() + "/request/" + pid + "/" + rid;
    }

    /**
     * 6.4 修改项目的API
     */
    public static String editProjectApiURL(String pid, String rid) {
        return getBaseDir() + "/request/" + pid + "/" + rid;
    }

    /**
     * 6.5 修改API的分组
     */
    public static String editProjectApiGroupURL(String pid, String rid) {
        return getBaseDir() + "/request/" + pid + "/" + rid + "/group";
    }

    /**
     * 6.6 删除项目API
     */
    public static String deleteProjectApiURL(String pid,String rid) {
        return getBaseDir() + "/request/" + pid + "/" + rid;
    }

    /**
     * 7.1 FIXME 绑定路由
     */
    public static String bindingURL(){
        return getBaseDir() + "/virtual/binding";
    }

    /**
     * 7.2 FIXME 解绑路由
     */
    public static String nobindingURL() {
        return getBaseDir() + "/virtual/nobinding";
    }

    /**
     * 7.3 FIXME 返回查询条件中的分组信息
     */
    public static String projectQueryURL() {
        return getBaseDir() + "/projects/query";
    }

    /**
     * 7.4 FIXME 根据条件查询API
     */
    public static String requestsProjectURL() {
        return getBaseDir() + "/projects/requests";
    }


    private static APIProvider mInstance;

    public static APIProvider getInstance() {
        if (mInstance == null) {
            synchronized (APIProvider.class) {
                if (mInstance == null) {
                    mInstance = new APIProvider();
                }
            }
        }
        return mInstance;
    }

    /**
     * api 1.1
     * 注册
     */
    public void register(String name, String pwd, APICallBack callBack) {
        String url = registerUrl();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("name",name);
        params.put("pwd",pwd);
        APIUtil.loadJsonPost(url, params, APIType.NONE, callBack, Object.class);
    }

    /**
     * api 1.2
     * 登陆
     */
    public void login(String name,String pwd,APICallBack callBack) {
        String url = loginURL();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("name",name);
        params.put("pwd",pwd);
        APIUtil.loadJsonPost(url, params, APIType.NONE, callBack, Object.class);

    }

    /**
     * api 1.3
     * 获取用户信息
     */
    public void getUserInfo(APICallBack callBack) {
        String url = getUserURL();
        APIUtil.loadJsonGet(url, null, APIType.OBJECT, callBack, AccountVO.class);
    }

    /**
     * api 1.4
     * 修改个人信息（密码）
     */
    public void editUserInfo(String oldPassword, String newPassword, APICallBack callBack) {
        String url = editUserURL();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oldpwd", oldPassword);
        params.put("newpwd", newPassword);
        APIUtil.loadJsonPut(url, params, APIType.NONE, callBack, Object.class);
    }

    /**
     * api 1.5
     * 退出登录
     */
    public void logout(APICallBack callBack) {
        String url = logoutURL();
        APIUtil.loadJsonGet(url, null, APIType.NONE, callBack, Object.class);
    }

    /**
     * api 2.1
     *  创建项目
     */
    public void createProject(String name, String urlprefix, String info, String authStatus, String desc, APICallBack callBack) {
        String url = addProjectsURL();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("name",name);
        if(!"".equals(urlprefix))
            params.put("urlprefix",urlprefix);
        if(!"".equals(info))
            params.put("info",info);
        if(!"".equals(authStatus))
            params.put("authStatus", authStatus);
        if(!"".equals(desc))
            params.put("desc", desc);
        if(!"".equals(MyApplication.uid))
            params.put("userId",MyApplication.uid);
        APIUtil.loadJsonPost(url, params, APIType.OBJECT, callBack, ProjectVO.class);
    }

    /**
     * API2.2 批量获取项目信息
     */
    public void getProjects(int type, APICallBack callBack) {
        String url = getProjectsURL();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("type",type);
        APIUtil.loadJsonGet(url, params, APIType.LIST, callBack, ProjectVO.class);
    }

    /**
     * API2.3 修改项目信息
     */
    public void editProject(String pid, String name, String urlprefix, String info, String authStatus, String desc, APICallBack callBack) {
        String url = editProjectURL(pid);
        Map<String,Object> params = new HashMap<String,Object>();
        if(!"".equals(name))
            params.put("name",name);
        if(!"".equals(urlprefix))
            params.put("urlprefix",urlprefix);
        if(!"".equals(info))
            params.put("info",info);
        if(!"".equals(authStatus))
            params.put("authStatus", authStatus);
        if(!"".equals(desc))
            params.put("desc", desc);
        if(!"".equals(MyApplication.uid))
            params.put("userId",MyApplication.uid);
        APIUtil.loadJsonPut(url, params, APIType.OBJECT, callBack, ProjectVO.class);
    }

    /**
     * API2.4
     * 删除项目
     */
    public void deleteProject(String pid, APICallBack callBack) {
        String url = deleteProjectURL(pid);
        APIUtil.loadJsonDelete(url, null, APIType.NONE, callBack, Object.class);
    }

    /**
     * API2.5
     * 更新项目虚拟部署地址
     */
    public void updateUrl(String name, APICallBack callBack) {
        String url = updateURL(name);
        String urlParams = "?name=" + name;
        url += urlParams.substring(0, urlParams.length() - 1);
        APIUtil.loadJsonGet(url, null, APIType.OBJECT, callBack, ProjectVO.class);
    }

    /**
     * API2.6
     * 获取项目详情
     */
    public void getPorjectDetailsUrl(String pid, APICallBack callBack) {
        String url = getProjectDetailsURL(pid);
        APIUtil.loadJsonGet(url, null, APIType.OBJECT, callBack, ProjectVO.class);
    }

    /**
     * API3.1
     * 测试API
     */
    public void testApiUrl(String testurl, String testmethod, String testdata, APICallBack callBack) {
        String url = testApiURL();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("testurl",testurl);
        params.put("testmethod",testmethod);
        if(!"".equals(testdata))
            params.put("testdata",testdata);
        APIUtil.loadJsonGet(url, params, APIType.MAP, callBack, Object.class);
    }

    /**
     * API4.1
     * 获取项目下的用户
     */
    public void authorityUserUrl(String pid, APICallBack callBack) {
        String url = authorityUserURL();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pid", pid);
        APIUtil.loadJsonGet(url, params, APIType.LIST, callBack, ProjectMemberVO.class);
    }

    /**
     * API4.2
     * 新增该项目下的用户
     */
    public void addauthorityUserUrl(String name, int auth, String pid, APICallBack callBack) {
        String url = addauthorityUserURL();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("auth", auth);
        params.put("pid", pid);
        APIUtil.loadJsonPost(url, params, APIType.NONE, callBack, Object.class);
    }

    /**
     * API4.3
     * 删除该项目下的用户
     */
    public void deleteAuthorityUserUrl(String uid, String pid, APICallBack callBack) {
        String url = deleteAuthorityUserURL(uid);
        String urlParams = "?uid=" + uid;
        url += urlParams.substring(0, urlParams.length());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pid", pid);
        APIUtil.loadJsonDelete(url, params, APIType.NONE, callBack, Object.class);
    }

    /**
     * API5.1
     * 获取项目API分组
     */
    public void getProjectGroupUrl(String pid, APICallBack callBack) {
        String url = getProjectGroupURL(pid);
        String urlParams = "?pid=" + pid;
        url += urlParams.substring(0, urlParams.length());
        APIUtil.loadJsonGet(url, null, APIType.LIST, callBack, ProjectGroupVO.class);
    }

    /**
     * API5.2
     * 新增项目分组
     */
    public void addProjectGroupUrl(String pid, String name, APICallBack callBack) {
        String url = addProjectGroupURL(pid);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        APIUtil.loadJsonPost(url, params, APIType.OBJECT, callBack, GroupVO.class);
    }

    /**
     * API5.3
     * 获取指定分组详情
     */
    public void getGroupDetailsUrl(String pid, int gid, APICallBack callBack) {
        String url = getGroupDetailsURL(pid, gid);
        String urlParams = "?pid=" + pid + "&gid=" + gid;
        url += urlParams.substring(0, urlParams.length());
        APIUtil.loadJsonGet(url, null, APIType.OBJECT, callBack, ProjectGroupVO.class);
    }

    /**
     * API5.4
     * 修改分组名
     */
    public void editProjectGroupUrl(String pid, int gid, String name, APICallBack callBack) {
        String url = editProjectGroupURL(pid, gid);
        String urlParams = "?pid=" + pid + "&gid=" + gid;
        url += urlParams.substring(0, urlParams.length());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        APIUtil.loadJsonGet(url, params, APIType.MAP, callBack, Object.class);
    }

    /**
     * API5.5
     * 删除项目分组
     */
    public void deleteProjectGroupUrl(String pid, int gid, APICallBack callBack) {
        String url = deleteProjectGroupURL(pid, gid);
        String urlParams = "?pid=" + pid + "&gid=" + gid;
        url += urlParams.substring(0, urlParams.length());
        APIUtil.loadJsonDelete(url, null, APIType.NONE, callBack, Object.class);
    }

    /**
     * API6.1
     * 添加项目API
     */
    public void addProjectRestUrl(String pid, int groupid, String name, String description, String urlsuffix, String method, String data,
                                  int ability, String output, String outputsuccess, String outputerror, String detail, int status, APICallBack callBack) {
        String url = addProjectRestURL(pid);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupid", groupid);
        params.put("name", name);
        if(!"".equals(description))
            params.put("description", description);
        params.put("urlsuffix", urlsuffix);
        params.put("method", method);
        if(!"".equals(data))
            params.put("data", data);
        if(!"".equals(ability))
            params.put("ability", ability);
        params.put("output", output);
        if(!"".equals(outputsuccess))
            params.put("outputsuccess", outputsuccess);
        if(!"".equals(outputerror))
            params.put("outputerror", outputerror);
        if(!"".equals(detail))
            params.put("detail", detail);
        params.put("status", status);
        APIUtil.loadJsonPost(url, params, APIType.OBJECT, callBack, ProjectRequestVO.class);
    }

    /**
     * API6.2
     * 获取项目指定分组下的所有API
     */
    public void getProjectRestUrl(String pid, int groupid, APICallBack callBack ) {
        String url = getProjectRestURL(pid);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupid", groupid);
        APIUtil.loadJsonGet(url, params, APIType.LIST, callBack, ProjectRequestVO.class);
    }

    /**
     * API6.3
     * 获取项目指定API
     */
    public void getTargetProjectApiUrl(String pid, String rid, APICallBack callBack) {
        String url = getTargetProjectApiURL(pid, rid);
        String urlParams = "?pid=" + pid + "&rid=" + rid;
        url += urlParams.substring(0, urlParams.length());
        APIUtil.loadJsonGet(url, null, APIType.OBJECT, callBack, ProjectRequestVO.class);
    }

    /**
     * API6.4
     * 修改项目API
     */
    public void editProjectApiUrl(String pid, String rid, int groupid, String name, String description, String urlsuffix, String method, String data,
                                  int ability, String output, String outputsuccess, String outputerror, String detail, int status, APICallBack callBack) {
        String url = editProjectApiURL(pid, rid);
        String urlParams = "?pid=" + pid + "&rid=" + rid;
        url += urlParams.substring(0, urlParams.length());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupid", groupid);
        params.put("name", name);
        if(!"".equals(description))
            params.put("description", description);
        params.put("urlsuffix", urlsuffix);
        params.put("method", method);
        if(!"".equals(data))
            params.put("data", data);
        if(!"".equals(ability))
            params.put("ability", ability);
        params.put("output", output);
        if(!"".equals(outputsuccess))
            params.put("outputsuccess", outputsuccess);
        if(!"".equals(outputerror))
            params.put("outputerror", outputerror);
        if(!"".equals(detail))
            params.put("detail", detail);
        params.put("status", status);
        APIUtil.loadJsonPut(url, params, APIType.OBJECT, callBack, ProjectRequestVO.class);
    }

    /**
     * API6.5
     * 删除API的分组
     */
    public void editProjectApiGroupUrl(String pid, String rid, int groupid, APICallBack callBack) {
        String url = editProjectApiGroupURL(pid, rid);
        String urlParams = "?pid=" + pid + "&rid=" + rid;
        url += urlParams.substring(0, urlParams.length());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupid", groupid);
        APIUtil.loadJsonPut(url, params, APIType.OBJECT, callBack, ProjectRequestVO.class);
    }

    /**
     * API6.6
     * 删除项目API
     */
    public void deleteProjectApiURL(String pid, String rid, APICallBack callBack) {
        String url = editProjectApiURL(pid, rid);
        String urlParams = "?pid=" + pid + "&rid=" + rid;
        url += urlParams.substring(0, urlParams.length());
        APIUtil.loadJsonDelete(url, null, APIType.NONE, callBack, Object.class);
    }

}
