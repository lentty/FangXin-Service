package com.cqfangxin.domain;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {
    /**
     * 默认每页显示数
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 默认页数
     */
    public static final int PAGE_NUM = 1;

    /**
     * 页数
     */
    private int draw;

    /**
     * 每页显示数
     */
    private int length = PAGE_SIZE;

    /**
     * 总页数
     */
    private int totalPageNum;

    /**
     * 记录总数
     */
    private int totalCount;

    /**
     * 分页信息
     */
    private List<T> rows = new ArrayList<T>();

    /**
     * 分页计算起始值
     */
    private int start;

    /**
     * 分页计算结束值  暂时没用
     */
    private int endIndex;

    private Integer brandId;

    private Integer cateId;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public void setPageNum(int pageNum) {
        if(pageNum <= 0){
            pageNum = PAGE_NUM;
        }
        if(pageNum > totalPageNum){
            pageNum = totalPageNum;
        }
        // 分页开始值 关键
        if(pageNum == 0){
            start = 0;
        }else{
            start = (pageNum - 1) * length;
        }
        this.draw = pageNum;
    }

    public int getStart() {
        // 分页开始值 关键
        if (draw == 0) {
            start = 0;
        } else {
            start = (draw - 1) * length;
        }
        return start;
    }

    public void setPageSize(int pageSize) {
        if(pageSize <= 0){
            pageSize = PAGE_SIZE;
        }
        // 计算最大页数
        int pageCount = totalCount / pageSize;
        if(totalCount % pageSize == 0){
            totalPageNum = pageCount;
        }else{
            totalPageNum = pageCount + 1;
        }
        this.length = pageSize;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if(totalCount > 0){
            if(draw <= 0){
                draw = PAGE_NUM;
            }
            // 计算最大页数
            int pageCount = totalCount / PAGE_SIZE;
            if(totalCount % PAGE_SIZE == 0){
                totalPageNum = pageCount;
            }else{
                totalPageNum = pageCount + 1;
            }
        }else{
            totalPageNum = 0;
        }

        if(draw > totalPageNum){
            draw = totalPageNum;
        }
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
