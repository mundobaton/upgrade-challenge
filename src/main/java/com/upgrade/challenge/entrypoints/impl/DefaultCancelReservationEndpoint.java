package com.upgrade.challenge.entrypoints.impl;

import com.upgrade.challenge.core.usecases.CancelReservation;
import com.upgrade.challenge.core.usecases.ReservationNotFoundException;
import com.upgrade.challenge.entrypoints.BadRequestEndpointException;
import com.upgrade.challenge.entrypoints.CancelReservationEndpoint;
import com.upgrade.challenge.entrypoints.NotFoundEndpointException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Objects;

public class DefaultCancelReservationEndpoint implements CancelReservationEndpoint {

    private CancelReservation cancelReservation;

    @Inject
    public DefaultCancelReservationEndpoint(CancelReservation cancelReservation) {
        this.cancelReservation = cancelReservation;
    }

    @Override
    public Void execute(RequestModel model) {
        try {
            validateParams(model.getReservationId(), model.getEmail());
        } catch (IllegalArgumentException e) {
            throw new BadRequestEndpointException(e);
        }

        try {
            cancelReservation.apply(CancelReservation.Model.builder().reservationId(model.getReservationId()).email(model.getEmail()).build());
        } catch (ReservationNotFoundException nfe) {
            throw new NotFoundEndpointException(nfe.getMessage());
        }

        return null;
    }

    private void validateParams(Long reservationId, String email) {
        if (Objects.isNull(reservationId)) {
            throw new IllegalArgumentException("Reservation id is required");
        }
        if (reservationId <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }

        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email is required");
        }
    }
}
