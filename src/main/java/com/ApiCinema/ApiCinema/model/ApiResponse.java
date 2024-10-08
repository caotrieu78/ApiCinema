package com.ApiCinema.ApiCinema.model;

public class ApiResponse {

    private int status;
    private Object content;
    private String message;

    // Constructor với tất cả các tham số
    public ApiResponse(int status, String message, Object content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }

    // Constructor chỉ với status và message
    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.content = null; // content có thể null nếu không cần thiết
    }

    // Constructor chỉ với status và content
    public ApiResponse(int status, Object content) {
        this.status = status;
        this.content = content;
        this.message = null; // message có thể null nếu không cần thiết
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
