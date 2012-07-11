package com.sobey.mcss.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-16
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Broadbandstat {
    private String id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String cp;

    @javax.persistence.Column(name = "CP", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    @Basic
    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    private String type;

    @javax.persistence.Column(name = "Type", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
    @Basic
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String datetime;

    @javax.persistence.Column(name = "DateTime", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    private long broadband;

    @javax.persistence.Column(name = "BroadBand", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getBroadband() {
        return broadband;
    }

    public void setBroadband(long broadband) {
        this.broadband = broadband;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Broadbandstat that = (Broadbandstat) o;

        if (broadband != that.broadband) return false;
        if (id != that.id) return false;
        if (cp != null ? !cp.equals(that.cp) : that.cp != null) return false;
        if (datetime != null ? !datetime.equals(that.datetime) : that.datetime != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (cp != null ? cp.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        result = 31 * result + (int) (broadband ^ (broadband >>> 32));
        return result;
    }
}
