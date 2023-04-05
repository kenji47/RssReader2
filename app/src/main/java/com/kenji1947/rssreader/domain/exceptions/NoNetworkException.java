package com.kenji1947.rssreader.domain.exceptions;

public class NoNetworkException extends RuntimeException {

    public NoNetworkException() {
        super();
    }

    public NoNetworkException(String message) {
        super(message);
    }

    public NoNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

}
