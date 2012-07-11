package com.sobey.mcss.domain;

import com.sobey.mcss.domain.id.UploadstatPK;

import javax.persistence.Entity;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-9
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.IdClass(UploadstatPK.class)
@Entity
public class Uploadstat {
    private String cp;

    @javax.persistence.Column(name = "CP", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
    @javax.persistence.Id
    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    private String taskid;

    @javax.persistence.Column(name = "TaskId", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
    @javax.persistence.Id
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    private String begintime;

    @javax.persistence.Column(name = "BeginTime", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @javax.persistence.Basic
    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    private String endtime;

    @javax.persistence.Column(name = "EndTime", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @javax.persistence.Basic
    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    private String dataflow;

    @javax.persistence.Column(name = "DataFlow", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @javax.persistence.Basic
    public String getDataflow() {
        return dataflow;
    }

    public void setDataflow(String dataflow) {
        this.dataflow = dataflow;
    }

    private String client;

    @javax.persistence.Column(name = "Client", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @javax.persistence.Basic
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Uploadstat that = (Uploadstat) o;

        if (begintime != null ? !begintime.equals(that.begintime) : that.begintime != null) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (cp != null ? !cp.equals(that.cp) : that.cp != null) return false;
        if (dataflow != null ? !dataflow.equals(that.dataflow) : that.dataflow != null) return false;
        if (endtime != null ? !endtime.equals(that.endtime) : that.endtime != null) return false;
        if (taskid != null ? !taskid.equals(that.taskid) : that.taskid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cp != null ? cp.hashCode() : 0;
        result = 31 * result + (taskid != null ? taskid.hashCode() : 0);
        result = 31 * result + (begintime != null ? begintime.hashCode() : 0);
        result = 31 * result + (endtime != null ? endtime.hashCode() : 0);
        result = 31 * result + (dataflow != null ? dataflow.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        return result;
    }
}
