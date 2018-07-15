package com.cqfangxin.domain;

import java.util.List;

public class TableDataResult<T> {
    /**
     * 页数
     */
    private int numOfPage;
    /**
     * 数据总数
     */
    private int totalCount;

    private List<T> data ;

    public TableDataResult(){

    }

    public TableDataResult(int numOfPage, int totalCount, List<T> data) {
        this.numOfPage = numOfPage;
        this.totalCount = totalCount;
        this.data = data;
    }

    public int getNumOfPage() {
        return numOfPage;
    }

    public void setNumOfPage(int numOfPage) {
        this.numOfPage = numOfPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
