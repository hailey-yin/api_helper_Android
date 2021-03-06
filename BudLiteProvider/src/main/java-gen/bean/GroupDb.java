package bean;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "GROUP_DB".
 */
public class GroupDb {

    private Long id;
    private String pid;
    private Integer gid;
    private String name;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public GroupDb() {
    }

    public GroupDb(Long id) {
        this.id = id;
    }

    public GroupDb(Long id, String pid, Integer gid, String name) {
        this.id = id;
        this.pid = pid;
        this.gid = gid;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
