package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.model.Reservation;
import com.upgrade.challenge.core.repositories.SaveReservationRepository;
import com.upgrade.challenge.core.usecases.*;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Objects;

public class DefaultSaveReservation implements SaveReservation {

    private ValidateDateAvailable validateDateAvailable;
    private ValidateReservation validateReservation;
    private SaveReservationRepository saveReservationRepository;

    @Inject
    public DefaultSaveReservation(ValidateReservation validateReservation, SaveReservationRepository saveReservationRepository, ValidateDateAvailable validateDateAvailable) {
        this.validateReservation = validateReservation;
        this.saveReservationRepository = saveReservationRepository;
        this.validateDateAvailable = validateDateAvailable;
    }

    @Override
    public synchronized Reservation apply(Model model) {
        try {
            validateModel(model);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error performing reservation", e);
        }

        validateReservation.apply(ValidateReservation.Model.builder().email(model.getEmail()).from(model.getFrom()).to(model.getTo()).build());
        Boolean isAvailable = validateDateAvailable.apply(ValidateDateAvailable.Model.builder().email(null).from(model.getFrom()).to(model.getTo()).build());
        if (!isAvailable) {
            throw new ReservationValidationException("date is not available");
        }

        com.upgrade.challenge.core.entities.Reservation entity = saveReservationRepository.save(model.getEmail(), model.getFullname(), model.getFrom(), model.getTo());
        return com.upgrade.challenge.core.model.Reservation.builder()
                .reservationId(entity.getReservationId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .from(entity.getFrom())
                .to(entity.getTo()).build();
    }

    private void validateModel(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("model is required");
        }
        if (StringUtils.isBlank(model.getEmail())) {
            throw new IllegalArgumentException("email is required");
        }
        if (StringUtils.isBlank(model.getFullname())) {
            throw new IllegalArgumentException("fullname is required");
        }
        if (Objects.isNull(model.getFrom())) {
            throw new IllegalArgumentException("from is required");
        }
        if (Objects.isNull(model.getTo())) {
            throw new IllegalArgumentException("to is required");
        }
        if (model.getFrom().compareTo(model.getTo()) >= 0) {
            throw new IllegalArgumentException("invalid from and to dates");
        }
    }
}
