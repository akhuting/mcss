package com.sobey.mcss.domain.id;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Yanggang.
 * Date: 11-1-7
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public class UploadstatPK implements Serializable {
    private String cp;

    @Id
    @Column(name = "CP", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    private String taskid;

    @Id
    @Column(name = "TaskId", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadstatPK that = (UploadstatPK) o;

        if (cp != null ? !cp.equals(that.cp) : that.cp != null) return false;
        if (taskid != null ? !taskid.equals(that.taskid) : that.taskid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cp != null ? cp.hashCode() : 0;
        result = 31 * result + (taskid != null ? taskid.hashCode() : 0);
        return result;
    }
}
