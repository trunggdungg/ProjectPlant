package com.example.projectplant.model;

import java.util.List;

public class UserModel {
    private boolean success;
    private String message;
    private List<User> result;

    public UserModel(boolean success, String message, List<User> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getResult() {
        return result;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", result=" + (result != null ? result.toString() : "null") +
                '}';
    }
}
