package com.galaxyt.sagittarius.client.utils.http;

public class Response {

    private final int code;

    private final String body;

    public Response(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
