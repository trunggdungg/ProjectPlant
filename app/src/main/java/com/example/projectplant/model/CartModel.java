package com.example.projectplant.model;

import java.util.List;

public class CartModel {
    private boolean success;
    private String message;
    private List<Cart> result;

    public CartModel(boolean success, String message, List<Cart> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public List<Cart> getResult() {
        return result;
    }

    public void setResult(List<Cart> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
