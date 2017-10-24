package mobi.wonders.apps.android.budrest.model;

import android.content.Intent;

import java.util.List;
import java.util.Map;

import javax.sql.StatementEvent;

/**
 * @author zenglan
 * @class AccountVO
 * @brief API 1.3
 * @date 2015/11/13.
 */
public class AccountVO {

    private String created_at;//用户创建时间
    private String username;//用户名

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
