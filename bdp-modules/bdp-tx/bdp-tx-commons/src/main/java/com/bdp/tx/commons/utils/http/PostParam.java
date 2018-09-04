package com.bdp.tx.commons.utils.http;

public class PostParam {

    private String name;
    private String value;

    public String getName() {
        if (name == null)
            return "";
        else
            return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        if (value == null)
            return "";
        else
            return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PostParam() {
    }

    public PostParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

}