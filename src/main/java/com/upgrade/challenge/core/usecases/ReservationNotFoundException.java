package com.upgrade.challenge.core.usecases;

public class ReservationNotFoundException extends UseCaseException {

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
