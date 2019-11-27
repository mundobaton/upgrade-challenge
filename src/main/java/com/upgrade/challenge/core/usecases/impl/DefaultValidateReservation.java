package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.model.Reservation;
import com.upgrade.challenge.core.usecases.FindContiguousReservation;
import com.upgrade.challenge.core.usecases.ReservationValidationException;
import com.upgrade.challenge.core.usecases.UseCaseException;
import com.upgrade.challenge.core.usecases.ValidateReservation;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class DefaultValidateReservation implements ValidateReservation {

    private static final long MAX_RESERVATION_DAYS = 3;
    private static final long MAX_RESERVATION_PRECEDENCE_DAYS = 30;
    private FindContiguousReservation findContiguousReservation;

    @Inject
    public DefaultValidateReservation(FindContiguousReservation findContiguousReservation) {
        this.findContiguousReservation = findContiguousReservation;
    }

    @Override
    public Void apply(Model model) {
        try {
            validateModel(model);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error validating reservation", e);
        }

        long daysBetween = ChronoUnit.DAYS.between(model.getFrom().toInstant(), model.getTo().toInstant());
        if (daysBetween > MAX_RESERVATION_DAYS) {
            throw new ReservationValidationException(String.format("Reservation must be placed for a max of %d days", MAX_RESERVATION_DAYS));
        }

        Date now = DateUtils.truncate(new Date(), Calendar.DATE);

        daysBetween = ChronoUnit.DAYS.between(now.toInstant(), model.getFrom().toInstant());
        if (daysBetween > MAX_RESERVATION_PRECEDENCE_DAYS) {
            throw new ReservationValidationException(String.format("Reservation must be placed up to %d in advance", MAX_RESERVATION_PRECEDENCE_DAYS));
        } else {
            if (daysBetween <= 0) {
                throw new ReservationValidationException(String.format("Reservation must be placed minimum 1 day ahead of arrival", MAX_RESERVATION_PRECEDENCE_DAYS));
            }
        }

        Optional<Reservation> reservation = findContiguousReservation.apply(FindContiguousReservation.Model.builder().email(model.getEmail()).from(model.getFrom()).to(model.getTo()).build());
        if (reservation.isPresent()) {
            throw new ReservationValidationException(String.format("You already have a contiguous reservation"));
        }

        return null;
    }

    private void validateModel(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("model is required");
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
