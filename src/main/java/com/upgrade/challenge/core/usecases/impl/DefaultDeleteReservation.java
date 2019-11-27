package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.DeleteReservationRepository;
import com.upgrade.challenge.core.usecases.DeleteReservation;
import com.upgrade.challenge.core.usecases.UseCaseException;

import javax.inject.Inject;
import java.util.Objects;

public class DefaultDeleteReservation implements DeleteReservation {

    private DeleteReservationRepository deleteReservationRepository;

    @Inject
    public DefaultDeleteReservation(DeleteReservationRepository deleteReservationRepository) {
        this.deleteReservationRepository = deleteReservationRepository;
    }

    @Override
    public Void apply(Model model) {
        try {
            validateModel(model.getReservationId());
            deleteReservationRepository.delete(model.getReservationId());
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error removing reservation", e);
        }

        return null;
    }

    private void validateModel(Long reservationId) {
        if (Objects.isNull(reservationId)) {
            throw new IllegalArgumentException("Reservation id is required");
        }
        if (reservationId <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }
    }
}
