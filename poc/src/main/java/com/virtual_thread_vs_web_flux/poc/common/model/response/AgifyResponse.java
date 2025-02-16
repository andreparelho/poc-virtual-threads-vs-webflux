package com.virtual_thread_vs_web_flux.poc.common.model.response;

public class AgifyResponse extends ResponseCommon {
    private String count;
    private String name;
    private String age;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}