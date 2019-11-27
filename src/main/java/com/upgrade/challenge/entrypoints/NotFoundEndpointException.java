package com.upgrade.challenge.entrypoints;

public class NotFoundEndpointException extends EndpointException {

    public static final String NOT_FOUND = "not_found";

    public NotFoundEndpointException(String errorMessage) {
        super(NOT_FOUND, errorMessage);
    }

}
