package com.moka.call.net;

/**
 * Created by su on 17-4-11.
 */

public class NetResponse<T> {
    private T result;
    private int code;
    private String message;
    private boolean parseSuccessful = true;

    NetResponse(T result, int code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

    NetResponse(T result, boolean parseSuccessful, int code, String message) {
        this.result = result;
        this.code = code;
        this.parseSuccessful = parseSuccessful;
        this.message = message;
    }

    public boolean isParseSuccessful() {
        return parseSuccessful;
    }

    public int getCode() {
        return code;
    }

    public T getResult() {
        return result;
    }

    /**
     * Returns true if the code is in [200..300), which means the request was successfully received,
     * understood, and accepted.
     */
    boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    /** Returns the HTTP status message or null if it is unknown. */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "NetResponse{" +
                "result=" + result +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", parseSuccessful=" + parseSuccessful +
                '}';
    }
}
