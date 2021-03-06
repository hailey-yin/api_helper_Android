package bean;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "LATE_COLLECTION_DB".
 */
public class LateCollectionDb {

    private Long id;
    private String username;
    private String pid;
    private Integer flag;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public LateCollectionDb() {
    }

    public LateCollectionDb(Long id) {
        this.id = id;
    }

    public LateCollectionDb(Long id, String username, String pid, Integer flag) {
        this.id = id;
        this.username = username;
        this.pid = pid;
        this.flag = flag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
