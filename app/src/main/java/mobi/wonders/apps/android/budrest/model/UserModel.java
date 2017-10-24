package mobi.wonders.apps.android.budrest.model;

/**
 * Created by jiangguangming on 2015/11/24.
 */
public class UserModel{
    private String name;
    private String password;

    public UserModel(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
