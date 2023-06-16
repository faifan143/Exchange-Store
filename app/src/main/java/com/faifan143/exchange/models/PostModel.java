package com.faifan143.exchange.models;

public class PostModel {
    private String exchangerName;
    private String exchangerEmail;
    private String exchangerNumber;
    private String exchangerToken;
    private String productCategory;
    private String productPrice;
    private String productDescription;
    private String productImage;
    private String desiredExchangeCategory;
    private String key;

    public String getExchangerName() {
        return exchangerName;
    }

    public void setExchangerName(String exchangerName) {
        this.exchangerName = exchangerName;
    }

    public String getExchangerEmail() {
        return exchangerEmail;
    }

    public void setExchangerEmail(String exchangerEmail) {
        this.exchangerEmail = exchangerEmail;
    }

    public String getExchangerNumber() {
        return exchangerNumber;
    }

    public void setExchangerNumber(String exchangerNumber) {
        this.exchangerNumber = exchangerNumber;
    }

    public String getExchangerToken() {
        return exchangerToken;
    }

    public void setExchangerToken(String exchangerToken) {
        this.exchangerToken = exchangerToken;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDesiredExchangeCategory() {
        return desiredExchangeCategory;
    }

    public void setDesiredExchangeCategory(String desiredExchangeCategory) {
        this.desiredExchangeCategory = desiredExchangeCategory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public PostModel(String key , String exchangerName, String exchangerEmail, String exchangerNumber, String exchangerToken, String productCategory, String productDescription, String productImage, String desiredExchangeCategory , String productPrice) {
        this.exchangerName = exchangerName;
        this.exchangerEmail = exchangerEmail;
        this.exchangerNumber = exchangerNumber;
        this.exchangerToken = exchangerToken;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.desiredExchangeCategory = desiredExchangeCategory;
        this.productPrice=productPrice;
        this.key = key;
    }

    public PostModel() {
    }
}
