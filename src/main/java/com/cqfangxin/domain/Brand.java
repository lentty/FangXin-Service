package com.cqfangxin.domain;

import java.util.List;

public class Brand {
    private Integer id;
    private String name;
    private List<Category> cateList;
    private Integer status;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }


    public List<Category> getCateList() {
        return cateList;
    }

    public void setCateList(List<Category> cateList) {
        this.cateList = cateList;
    }
}
