package mobi.wonders.apps.android.budrest.model;

import java.util.List;

/**
 * Created by qinyuanmao on 15/12/8.
 */
public class ApiSearchModel {

    /**
     * code : 1
     * msg : success
     * result : [{"_id":"563af87aed92eea16308c98b","name":"测试API","gid":28,"pid":"8","method":"get","deployStatus":0,"urlsuffix":"api/apitest","gname":"API测试模块"}]
     */

    private int code;
    private String msg;
    /**
     * _id : 563af87aed92eea16308c98b
     * name : 测试API
     * gid : 28
     * pid : 8
     * method : get
     * deployStatus : 0
     * urlsuffix : api/apitest
     * gname : API测试模块
     */

    private List<ResultEntity> result;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(List<ResultEntity> result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<ResultEntity> getResult() {
        return result;
    }

    public static class ResultEntity {
        private String _id;
        private String name;
        private int gid;
        private String pid;
        private String method;
        private int deployStatus;
        private String urlsuffix;
        private String gname;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public void setDeployStatus(int deployStatus) {
            this.deployStatus = deployStatus;
        }

        public void setUrlsuffix(String urlsuffix) {
            this.urlsuffix = urlsuffix;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String get_id() {
            return _id;
        }

        public String getName() {
            return name;
        }

        public int getGid() {
            return gid;
        }

        public String getPid() {
            return pid;
        }

        public String getMethod() {
            return method;
        }

        public int getDeployStatus() {
            return deployStatus;
        }

        public String getUrlsuffix() {
            return urlsuffix;
        }

        public String getGname() {
            return gname;
        }
    }
}
