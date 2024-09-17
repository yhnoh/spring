package org.example.springredissionlock;

public class RequestCountExceedException extends RuntimeException {

    public RequestCountExceedException(String message) {
        super(message);
    }
}
