package com.sobey.common.pager;

import com.sobey.common.util.PageInfo;

/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午11:41
 * To change this template use File | Settings | File Templates.
 */
public class Pager implements PageInfo {
    static final int DEFAULT_PAGE_SIZE = 20;
    static final int FIRST_PAGE_NUMBER = 1;

    private int pageNumber;
    private int pageSize;
    private int pageCount;
    private int recordCount;

    public Pager resetPageCount() {
        pageCount = -1;
        return this;
    }

    public int getPageCount() {
        if (pageCount < 0)
            pageCount = (int) Math.ceil((double) recordCount / pageSize);
        return pageCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public Pager setPageNumber(int pn) {
        pageNumber = pn > FIRST_PAGE_NUMBER ? pn : FIRST_PAGE_NUMBER;
        return this;
    }

    public Pager setPageSize(int pageSize) {
        this.pageSize = (pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE);
        return resetPageCount();
    }

    public Pager setRecordCount(int recordCount) {
        this.recordCount = recordCount > 0 ? recordCount : 0;
        this.pageCount = (int) Math.ceil((double) recordCount / pageSize);
        return this;
    }

    public int getOffset() {
        return pageSize * (pageNumber - 1);
    }


    @Override
    public String toString() {
        return String.format("size: %d, total: %d, page: %d/%d",
                pageSize,
                recordCount,
                pageNumber,
                this.getPageCount());
    }

    public boolean isFirst() {
        return pageNumber == 1;
    }

    public boolean isLast() {
        return pageNumber == pageCount;
    }


}
