package com.sobey.mcss.domain;

import com.sobey.mcss.domain.id.HourstatitemPK;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-9
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.IdClass(HourstatitemPK.class)
@Entity
public class Hourstatitem {
    private String cp;

    @javax.persistence.Column(name = "CP", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
    @Id
    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    private String period;

    @javax.persistence.Column(name = "Period", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    private String type;

    @javax.persistence.Column(name = "Type", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
    @Id
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String subtype;

    @javax.persistence.Column(name = "SubType", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
    @Id
    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    private String item;

    @javax.persistence.Column(name = "Item", nullable = false, insertable = true, updatable = true, length = 150, precision = 0)
    @Id
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    private long total;

    @javax.persistence.Column(name = "total", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    private long count1;

    @javax.persistence.Column(name = "Count1", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount1() {
        return count1;
    }

    public void setCount1(long count1) {
        this.count1 = count1;
    }

    private long count2;

    @javax.persistence.Column(name = "Count2", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount2() {
        return count2;
    }

    public void setCount2(long count2) {
        this.count2 = count2;
    }

    private long count3;

    @javax.persistence.Column(name = "Count3", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount3() {
        return count3;
    }

    public void setCount3(long count3) {
        this.count3 = count3;
    }

    private long count4;

    @javax.persistence.Column(name = "Count4", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount4() {
        return count4;
    }

    public void setCount4(long count4) {
        this.count4 = count4;
    }

    private long count5;

    @javax.persistence.Column(name = "Count5", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount5() {
        return count5;
    }

    public void setCount5(long count5) {
        this.count5 = count5;
    }

    private long count6;

    @javax.persistence.Column(name = "Count6", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount6() {
        return count6;
    }

    public void setCount6(long count6) {
        this.count6 = count6;
    }

    private long count7;

    @javax.persistence.Column(name = "Count7", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount7() {
        return count7;
    }

    public void setCount7(long count7) {
        this.count7 = count7;
    }

    private long count8;

    @javax.persistence.Column(name = "Count8", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount8() {
        return count8;
    }

    public void setCount8(long count8) {
        this.count8 = count8;
    }

    private long count9;

    @javax.persistence.Column(name = "Count9", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount9() {
        return count9;
    }

    public void setCount9(long count9) {
        this.count9 = count9;
    }

    private long count10;

    @javax.persistence.Column(name = "Count10", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount10() {
        return count10;
    }

    public void setCount10(long count10) {
        this.count10 = count10;
    }

    private long count11;

    @javax.persistence.Column(name = "Count11", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount11() {
        return count11;
    }

    public void setCount11(long count11) {
        this.count11 = count11;
    }

    private long count12;

    @javax.persistence.Column(name = "Count12", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount12() {
        return count12;
    }

    public void setCount12(long count12) {
        this.count12 = count12;
    }

    private long count13;

    @javax.persistence.Column(name = "Count13", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount13() {
        return count13;
    }

    public void setCount13(long count13) {
        this.count13 = count13;
    }

    private long count14;

    @javax.persistence.Column(name = "Count14", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount14() {
        return count14;
    }

    public void setCount14(long count14) {
        this.count14 = count14;
    }

    private long count15;

    @javax.persistence.Column(name = "Count15", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount15() {
        return count15;
    }

    public void setCount15(long count15) {
        this.count15 = count15;
    }

    private long count16;

    @javax.persistence.Column(name = "Count16", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount16() {
        return count16;
    }

    public void setCount16(long count16) {
        this.count16 = count16;
    }

    private long count17;

    @javax.persistence.Column(name = "Count17", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount17() {
        return count17;
    }

    public void setCount17(long count17) {
        this.count17 = count17;
    }

    private long count18;

    @javax.persistence.Column(name = "Count18", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount18() {
        return count18;
    }

    public void setCount18(long count18) {
        this.count18 = count18;
    }

    private long count19;

    @javax.persistence.Column(name = "Count19", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount19() {
        return count19;
    }

    public void setCount19(long count19) {
        this.count19 = count19;
    }

    private long count20;

    @javax.persistence.Column(name = "Count20", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount20() {
        return count20;
    }

    public void setCount20(long count20) {
        this.count20 = count20;
    }

    private long count21;

    @javax.persistence.Column(name = "Count21", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount21() {
        return count21;
    }

    public void setCount21(long count21) {
        this.count21 = count21;
    }

    private long count22;

    @javax.persistence.Column(name = "Count22", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount22() {
        return count22;
    }

    public void setCount22(long count22) {
        this.count22 = count22;
    }

    private long count23;

    @javax.persistence.Column(name = "Count23", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount23() {
        return count23;
    }

    public void setCount23(long count23) {
        this.count23 = count23;
    }

    private long count24;

    @javax.persistence.Column(name = "Count24", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public long getCount24() {
        return count24;
    }

    public void setCount24(long count24) {
        this.count24 = count24;
    }

    private String expend1;

    @javax.persistence.Column(name = "Expend1", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @Basic
    public String getExpend1() {
        return expend1;
    }

    public void setExpend1(String expend1) {
        this.expend1 = expend1;
    }

    private String expend2;

    @javax.persistence.Column(name = "Expend2", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @Basic
    public String getExpend2() {
        return expend2;
    }

    public void setExpend2(String expend2) {
        this.expend2 = expend2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hourstatitem that = (Hourstatitem) o;

        if (count1 != that.count1) return false;
        if (count10 != that.count10) return false;
        if (count11 != that.count11) return false;
        if (count12 != that.count12) return false;
        if (count13 != that.count13) return false;
        if (count14 != that.count14) return false;
        if (count15 != that.count15) return false;
        if (count16 != that.count16) return false;
        if (count17 != that.count17) return false;
        if (count18 != that.count18) return false;
        if (count19 != that.count19) return false;
        if (count2 != that.count2) return false;
        if (count20 != that.count20) return false;
        if (count21 != that.count21) return false;
        if (count22 != that.count22) return false;
        if (count23 != that.count23) return false;
        if (count24 != that.count24) return false;
        if (count3 != that.count3) return false;
        if (count4 != that.count4) return false;
        if (count5 != that.count5) return false;
        if (count6 != that.count6) return false;
        if (count7 != that.count7) return false;
        if (count8 != that.count8) return false;
        if (count9 != that.count9) return false;
        if (total != that.total) return false;
        if (cp != null ? !cp.equals(that.cp) : that.cp != null) return false;
        if (expend1 != null ? !expend1.equals(that.expend1) : that.expend1 != null) return false;
        if (expend2 != null ? !expend2.equals(that.expend2) : that.expend2 != null) return false;
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
        result = 31 * result + (int) (total ^ (total >>> 32));
        result = 31 * result + (int) (count1 ^ (count1 >>> 32));
        result = 31 * result + (int) (count2 ^ (count2 >>> 32));
        result = 31 * result + (int) (count3 ^ (count3 >>> 32));
        result = 31 * result + (int) (count4 ^ (count4 >>> 32));
        result = 31 * result + (int) (count5 ^ (count5 >>> 32));
        result = 31 * result + (int) (count6 ^ (count6 >>> 32));
        result = 31 * result + (int) (count7 ^ (count7 >>> 32));
        result = 31 * result + (int) (count8 ^ (count8 >>> 32));
        result = 31 * result + (int) (count9 ^ (count9 >>> 32));
        result = 31 * result + (int) (count10 ^ (count10 >>> 32));
        result = 31 * result + (int) (count11 ^ (count11 >>> 32));
        result = 31 * result + (int) (count12 ^ (count12 >>> 32));
        result = 31 * result + (int) (count13 ^ (count13 >>> 32));
        result = 31 * result + (int) (count14 ^ (count14 >>> 32));
        result = 31 * result + (int) (count15 ^ (count15 >>> 32));
        result = 31 * result + (int) (count16 ^ (count16 >>> 32));
        result = 31 * result + (int) (count17 ^ (count17 >>> 32));
        result = 31 * result + (int) (count18 ^ (count18 >>> 32));
        result = 31 * result + (int) (count19 ^ (count19 >>> 32));
        result = 31 * result + (int) (count20 ^ (count20 >>> 32));
        result = 31 * result + (int) (count21 ^ (count21 >>> 32));
        result = 31 * result + (int) (count22 ^ (count22 >>> 32));
        result = 31 * result + (int) (count23 ^ (count23 >>> 32));
        result = 31 * result + (int) (count24 ^ (count24 >>> 32));
        result = 31 * result + (expend1 != null ? expend1.hashCode() : 0);
        result = 31 * result + (expend2 != null ? expend2.hashCode() : 0);
        return result;
    }
}
