package mobi.wonders.apps.android.budrest.model;

/**
 * Created by ygl on 2015/12/29.
 */
public class ProjectRequestVO {
    private String pid;
    private int gid;
    private String name;
    private String description;
    private String urlsuffix;
    private String url;
    private String method;
    private String data;
    private int ability;
    private String output;
    private String outputsuccess;
    private String outputerror;
    private String detail;
    private int status;
    private int deployStatus;

    public String getOutputerror() {
        return outputerror;
    }

    public void setOutputerror(String outputerror) {
        this.outputerror = outputerror;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlsuffix() {
        return urlsuffix;
    }

    public void setUrlsuffix(String urlsuffix) {
        this.urlsuffix = urlsuffix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getAbility() {
        return ability;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutputsuccess() {
        return outputsuccess;
    }

    public void setOutputsuccess(String outputsuccess) {
        this.outputsuccess = outputsuccess;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeployStatus() {
        return deployStatus;
    }

    public void setDeployStatus(int deployStatus) {
        this.deployStatus = deployStatus;
    }
}
