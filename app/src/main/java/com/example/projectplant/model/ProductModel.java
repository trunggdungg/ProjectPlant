package com.example.projectplant.model;

import java.util.List;

public class ProductModel {
    private boolean success;
    private String message;
    private List<Product> result;

    public ProductModel(boolean success, List<Product> result, String message) {
        this.success = success;
        this.result = result;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Product> getResult() {
        return result;
    }

    public void setResult(List<Product> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
