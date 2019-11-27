package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.model.Reservation;
import com.upgrade.challenge.core.repositories.FindReservationRepository;
import com.upgrade.challenge.core.usecases.GetReservation;
import com.upgrade.challenge.core.usecases.ReservationNotFoundException;
import com.upgrade.challenge.core.usecases.UseCaseException;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

public class DefaultGetReservation implements GetReservation {

    private FindReservationRepository findReservationRepository;

    @Inject
    public DefaultGetReservation(FindReservationRepository findReservationRepository) {
        this.findReservationRepository = findReservationRepository;
    }

    @Override
    public Reservation apply(Model model) {
        try {
            validate(model);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error getting reservation", e);
        }

        Optional<com.upgrade.challenge.core.entities.Reservation> optEntity = findReservationRepository.find(model.getReservationId());
        if (!optEntity.isPresent()) {
            throw new ReservationNotFoundException(String.format("Reservation with id %d does not exist", model.getReservationId()));
        }

        com.upgrade.challenge.core.entities.Reservation entity = optEntity.get();
        return Reservation.builder().reservationId(model.getReservationId()).email(entity.getEmail()).fullName(entity.getFullName()).from(entity.getFrom()).to(entity.getTo()).build();
    }

    private void validate(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("Model is required");
        }
        if (Objects.isNull(model.getReservationId()) || model.getReservationId() <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }
    }
}
