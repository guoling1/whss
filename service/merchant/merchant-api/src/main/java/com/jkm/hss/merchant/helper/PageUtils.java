package com.jkm.hss.merchant.helper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页
 * Created by lt on 2016/12/8.
 */
public class PageUtils<E> implements Serializable{

    private int pageSize = 5; //每页显示多少条记录
    private int totalPage;
    private int totalCount;
    private int start = 1;    //默认从第一页开始
    private int pageNo;
    private List<E> records = Collections.emptyList();

    public int getStart() {
        start = (getPageNo() - 1) * getPageSize();
        if (start < 0) {
            start = 0;
        }
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<E> getRecords() {
        return records;
    }

    public void setRecord(List<E> records) {
        this.records = records;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPage() {
        return (int) Math.ceil(totalCount * 1.0 / pageSize);
    }

    public int getPageNo() {
        if (pageNo <= 0)
            pageNo = 1;
        if (pageNo > getTotalPage())
            pageNo = getTotalPage();
        return pageNo;
    }

    @Override
    public String toString() {
        return "Page [pageSize=" + pageSize + ", totalPage=" + getTotalPage()
                + ", totalCount=" + totalCount + ", pageNo=" + pageNo
                + ", records=" + records + "]";
    }
}
