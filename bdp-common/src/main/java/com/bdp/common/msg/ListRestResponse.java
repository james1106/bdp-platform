package com.bdp.common.msg;

public class ListRestResponse<T> {
    String msg;
    T result;
    int count;



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ListRestResponse<T> count(int count) {
        this.setCount(count);
        return this;
    }

    public ListRestResponse<T> count(Long count) {
        this.setCount(count.intValue());
        return this;
    }

    public ListRestResponse<T> msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public ListRestResponse<T> result(T result) {
        this.setResult(result);
        return this;
    }

}
