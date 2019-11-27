package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.model.Reservation;
import com.upgrade.challenge.core.repositories.ModifyReservationRepository;
import com.upgrade.challenge.core.usecases.*;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Objects;

public class DefaultModifyReservation implements ModifyReservation {

    private ValidateReservation validateReservation;
    private ValidateDateAvailable validateDateAvailable;
    private GetReservation getReservation;
    private ModifyReservationRepository modifyReservationRepository;


    @Inject
    public DefaultModifyReservation(ValidateReservation validateReservation, ValidateDateAvailable validateDateAvailable, ModifyReservationRepository modifyReservationRepository, GetReservation getReservation) {
        this.validateReservation = validateReservation;
        this.validateDateAvailable = validateDateAvailable;
        this.modifyReservationRepository = modifyReservationRepository;
        this.getReservation = getReservation;
    }

    @Override
    public synchronized Void apply(Model model) {
        try {
            validateModel(model);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error modifying reservation", e);
        }

        validateReservation.apply(ValidateReservation.Model.builder().email(model.getEmail()).from(model.getFrom()).to(model.getTo()).build());
        Reservation reservation = getReservation.apply(GetReservation.Model.builder().reservationId(model.getReservationId()).build());
        if (!reservation.getEmail().equals(model.getEmail())) {
            throw new ReservationNotFoundException(String.format("Reservation with id %d does not exist", model.getReservationId()));
        }

        Boolean isAvailable = validateDateAvailable.apply(ValidateDateAvailable.Model.builder().email(model.getEmail()).from(model.getFrom()).to(model.getTo()).build());
        if (!isAvailable) {
            throw new ReservationValidationException("Date is not available");
        }

        modifyReservationRepository.update(reservation.getReservationId(), model.getFrom(), model.getTo());

        return null;
    }

    private void validateModel(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("Model is required");
        }

        if (Objects.isNull(model.getReservationId())) {
            throw new IllegalArgumentException("Reservation id is required");
        }

        if (model.getReservationId() <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }
        if (StringUtils.isBlank(model.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }
        if (model.getFrom() == null) {
            throw new IllegalArgumentException("from is required");
        }

        if (model.getTo() == null) {
            throw new IllegalArgumentException("to is required");
        }

        if (model.getFrom().compareTo(model.getTo()) >= 0) {
            throw new IllegalArgumentException("invalid from and to dates");
        }
    }
}
