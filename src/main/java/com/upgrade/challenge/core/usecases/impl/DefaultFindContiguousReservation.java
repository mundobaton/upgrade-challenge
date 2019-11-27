package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.model.Reservation;
import com.upgrade.challenge.core.repositories.FindContiguousReservationRepository;
import com.upgrade.challenge.core.usecases.FindContiguousReservation;
import com.upgrade.challenge.core.usecases.UseCaseException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

public class DefaultFindContiguousReservation implements FindContiguousReservation {

    private FindContiguousReservationRepository findContiguousReservationRepository;

    @Inject
    public DefaultFindContiguousReservation(FindContiguousReservationRepository findContiguousReservationRepository) {
        this.findContiguousReservationRepository = findContiguousReservationRepository;
    }

    @Override
    public Optional<Reservation> apply(Model model) {
        try {
            validateModel(model);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error finding reservation by date", e);
        }

        Optional<com.upgrade.challenge.core.entities.Reservation> optionalReservation = findContiguousReservationRepository.findContiguousReservation(model.getEmail(), model.getFrom(), model.getTo());
        if (!optionalReservation.isPresent()) {
            return Optional.empty();
        }

        com.upgrade.challenge.core.entities.Reservation entity = optionalReservation.get();
        return Optional.of(Reservation.builder().reservationId(entity.getReservationId()).email(model.getEmail()).fullName(entity.getFullName()).from(entity.getFrom()).to(entity.getTo()).build());
    }

    private void validateModel(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("model is required");
        }
        if (StringUtils.isBlank(model.getEmail())) {
            throw new IllegalArgumentException("email is required");
        }
        if (Objects.isNull(model.getFrom())) {
            throw new IllegalArgumentException("from date is required");
        }
        if (Objects.isNull(model.getTo())) {
            throw new IllegalArgumentException("to date is required");
        }
    }
}
