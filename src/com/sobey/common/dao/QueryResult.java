package com.sobey.common.dao;

import com.sobey.common.pager.Pager;

import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午11:35
 * To change this template use File | Settings | File Templates.
 */
public class QueryResult {
    private List<?> list;
    private Pager pager;

    public QueryResult() {
    }

    public QueryResult(List<?> list, Pager pager) {
        this.list = list;
        this.pager = pager;
    }

    public List<?> getList() {
        return list;
    }

    public QueryResult setList(List<?> list) {
        this.list = list;
        return this;
    }

    public Pager getPager() {
        return pager;
    }

    public QueryResult setPager(Pager pager) {
        this.pager = pager;
        return this;
    }
}
