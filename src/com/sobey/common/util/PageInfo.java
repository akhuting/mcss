package com.sobey.common.util;

/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午11:41
 * To change this template use File | Settings | File Templates.
 */
public interface PageInfo {

    int getPageCount();


    int getPageNumber();


    PageInfo setPageNumber(int pageNumber);


    int getPageSize();


    PageInfo setPageSize(int pageSize);


    int getRecordCount();

    PageInfo setRecordCount(int recordCount);

    int getOffset();


    boolean isFirst();


    boolean isLast();
}
