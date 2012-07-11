package com.sobey.mcss.domain;

import com.sobey.mcss.domain.id.UserCpPK;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 11-6-9
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.IdClass(UserCpPK.class)
@javax.persistence.Table(name = "user_cp")
@Entity
public class UserCp {
    private int userId;

    @javax.persistence.Column(name = "userId", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int cpId;

    @javax.persistence.Column(name = "cpId", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getCpId() {
        return cpId;
    }

    public void setCpId(int cpId) {
        this.cpId = cpId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCp userCp = (UserCp) o;

        if (cpId != userCp.cpId) return false;
        if (userId != userCp.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + cpId;
        return result;
    }
}
