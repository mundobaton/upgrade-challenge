package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.model.Reservation;
import com.upgrade.challenge.core.usecases.*;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Objects;

public class DefaultCancelReservation implements CancelReservation {

    private GetReservation getReservation;
    private DeleteReservation deleteReservation;
    private SaveCanceledReservation saveCanceledReservation;

    @Inject
    public DefaultCancelReservation(GetReservation getReservation, DeleteReservation deleteReservation, SaveCanceledReservation saveCanceledReservation) {
        this.getReservation = getReservation;
        this.deleteReservation = deleteReservation;
        this.saveCanceledReservation = saveCanceledReservation;
    }

    @Override
    public Void apply(Model model) {
        try {
            validateParams(model.getReservationId(), model.getEmail());
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error canceling reservation");
        }

        Reservation reservation = getReservation.apply(GetReservation.Model.builder().reservationId(model.getReservationId()).build());
        if (!reservation.getEmail().equals(model.getEmail())) {
            throw new ReservationNotFoundException(String.format("Reservation with id %d does not exist", model.getReservationId()));
        }

        saveCanceledReservation.apply(SaveCanceledReservation.Model.builder().reservationId(model.getReservationId()).email(reservation.getEmail()).from(reservation.getFrom()).to(reservation.getTo()).build());
        deleteReservation.apply(DeleteReservation.Model.builder().reservationId(model.getReservationId()).build());

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
