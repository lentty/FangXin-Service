package com.cqfangxin.domain;

public class HomepageVO {
    private int numOfBrand;
    private int numOfProduct;
    private int numOfPromotedProduct;
    private int numOfUser;

    public int getNumOfBrand() {
        return numOfBrand;
    }

    public void setNumOfBrand(int numOfBrand) {
        this.numOfBrand = numOfBrand;
    }

    public int getNumOfProduct() {
        return numOfProduct;
    }

    public void setNumOfProduct(int numOfProduct) {
        this.numOfProduct = numOfProduct;
    }

    public int getNumOfUser() {
        return numOfUser;
    }

    public void setNumOfUser(int numOfUser) {
        this.numOfUser = numOfUser;
    }

    public int getNumOfPromotedProduct() {
        return numOfPromotedProduct;
    }

    public void setNumOfPromotedProduct(int numOfPromotedProduct) {
        this.numOfPromotedProduct = numOfPromotedProduct;
    }

    @Override
    public String toString(){
        return "HomepageVO{" +
                "numOfBrand=" + numOfBrand +
                ", numOfProduct=" + numOfProduct +
                ", numOfPromotedProduct=" + numOfPromotedProduct +
                ", numOfUser=" + numOfUser +
                '}';
    }
}
