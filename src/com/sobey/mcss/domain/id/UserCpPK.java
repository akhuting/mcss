package com.sobey.mcss.domain.id;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 11-6-9
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
public class UserCpPK implements Serializable {
    private int userId;

    @Id
    @Column(name = "userId", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int cpId;

    @Id
    @Column(name = "cpId", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
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

        UserCpPK userCpPK = (UserCpPK) o;

        if (cpId != userCpPK.cpId) return false;
        if (userId != userCpPK.userId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + cpId;
        return result;
    }
}
