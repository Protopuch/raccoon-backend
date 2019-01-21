package com.raccoon.backend.api.response;

@SuppressWarnings("unused")
public class Response<T> {
    private Status status;
    private T result;
    private String message;

    public Response(Status status, T result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public Response(Status status, T result) {
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

    public enum Status {
        OK,
        FAIL
    }
}
