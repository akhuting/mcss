package com.sobey.mcss.domain.id;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-2-28
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
public class UrldaystatitemPK implements Serializable {
    private String cp;

    @Id
    @Column(name = "CP", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    private String period;

    @Id
    @Column(name = "Period", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    private String type;

    @Id
    @Column(name = "Type", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String subtype;

    @Id
    @Column(name = "SubType", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    private String item;

    @Id
    @Column(name = "Item", nullable = false, insertable = true, updatable = true, length = 150, precision = 0)
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrldaystatitemPK that = (UrldaystatitemPK) o;

        if (cp != null ? !cp.equals(that.cp) : that.cp != null) return false;
        if (item != null ? !item.equals(that.item) : that.item != null) return false;
        if (period != null ? !period.equals(that.period) : that.period != null) return false;
        if (subtype != null ? !subtype.equals(that.subtype) : that.subtype != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cp != null ? cp.hashCode() : 0;
        result = 31 * result + (period != null ? period.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (subtype != null ? subtype.hashCode() : 0);
        result = 31 * result + (item != null ? item.hashCode() : 0);
        return result;
    }
}
