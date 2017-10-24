package mobi.wonders.apps.android.budrest.model;

import java.util.List;

/**
 * API 5.1
 * Created by jiangguangming on 2015/11/23.
 */
public class ProjectGroupVO {
    private String id;
    private String name;
    private List<ProjectsRequest> requests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProjectsRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<ProjectsRequest> requests) {
        this.requests = requests;
    }
}
