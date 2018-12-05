package com.mydeerlet.im.bean;

public class HttpResult<T> {

    private Integer state;
    private T data;
    private String message;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean SUCCESS(){
        return this.state==1000;
    }


    @Override
    public String toString() {
        return "HttpResult{" +
                "state=" + state +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
