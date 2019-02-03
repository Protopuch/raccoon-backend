package com.raccon.backend.web;

@SuppressWarnings("unused")
class Response<T> {
    private Status status;
    private T result;
    private String message;

    Response(Status status, T result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    Response(Status status, T result) {
        this(status, result, null);
    }

    public Status getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    enum Status {
        OK,
        FAIL,
        HEALTH_CHECK_ETO_HOROSHECHNO
    }
}
