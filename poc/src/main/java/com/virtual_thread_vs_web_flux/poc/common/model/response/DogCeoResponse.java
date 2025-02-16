package com.virtual_thread_vs_web_flux.poc.common.model.response;

public class DogCeoResponse extends ResponseCommon {
   private String message;
   private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
