package com.faifan143.exchange.models;

public class RequestModel {
    private String productCategory;
    private String productDescription;
    private String productDesiredExchangeCategory;
    private String productImage;
    private String productPrice;

    private String productKey;
    private String proposedProductKey;

    private String proposedCategory;
    private String proposedDescription;
    private String proposedDesiredExchangeCategory;
    private String proposedImage;
    private String proposedPrice;

    private String receiver;
    private String sender;

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

    public String getProductDesiredExchangeCategory() {
        return productDesiredExchangeCategory;
    }

    public void setProductDesiredExchangeCategory(String productDesiredExchangeCategory) {
        this.productDesiredExchangeCategory = productDesiredExchangeCategory;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProposedProductKey() {
        return proposedProductKey;
    }

    public void setProposedProductKey(String proposedProductKey) {
        this.proposedProductKey = proposedProductKey;
    }

    public String getProposedCategory() {
        return proposedCategory;
    }

    public void setProposedCategory(String proposedCategory) {
        this.proposedCategory = proposedCategory;
    }

    public String getProposedDescription() {
        return proposedDescription;
    }

    public void setProposedDescription(String proposedDescription) {
        this.proposedDescription = proposedDescription;
    }

    public String getProposedDesiredExchangeCategory() {
        return proposedDesiredExchangeCategory;
    }

    public void setProposedDesiredExchangeCategory(String proposedDesiredExchangeCategory) {
        this.proposedDesiredExchangeCategory = proposedDesiredExchangeCategory;
    }

    public String getProposedImage() {
        return proposedImage;
    }

    public void setProposedImage(String proposedImage) {
        this.proposedImage = proposedImage;
    }

    public String getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(String proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public RequestModel(String productCategory, String productDescription, String productDesiredExchangeCategory, String productImage, String productPrice, String productKey, String proposedProductKey, String proposedCategory, String proposedDescription, String proposedDesiredExchangeCategory, String proposedImage, String proposedPrice, String receiver, String sender) {
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productDesiredExchangeCategory = productDesiredExchangeCategory;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productKey = productKey;
        this.proposedProductKey = proposedProductKey;
        this.proposedCategory = proposedCategory;
        this.proposedDescription = proposedDescription;
        this.proposedDesiredExchangeCategory = proposedDesiredExchangeCategory;
        this.proposedImage = proposedImage;
        this.proposedPrice = proposedPrice;
        this.receiver = receiver;
        this.sender = sender;
    }

    public RequestModel() {
    }

}

