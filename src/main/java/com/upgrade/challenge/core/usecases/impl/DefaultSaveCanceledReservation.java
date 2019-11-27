package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.SaveCanceledReservationRepository;
import com.upgrade.challenge.core.usecases.SaveCanceledReservation;
import com.upgrade.challenge.core.usecases.UseCaseException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.Objects;

public class DefaultSaveCanceledReservation implements SaveCanceledReservation {

    private SaveCanceledReservationRepository saveCanceledReservationRepository;

    @Inject
    public void DefaultSaveCanceledReservation(SaveCanceledReservationRepository saveCanceledReservationRepository) {
        this.saveCanceledReservationRepository = saveCanceledReservationRepository;
    }

    @Override
    public Void apply(Model model) {
        try {
            validateModel(model);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error saving canceled reservation", e);
        }

        saveCanceledReservationRepository.save(model.getReservationId(), model.getEmail(), model.getFrom(), model.getTo(), new Date());
        return null;
    }

    private void validateModel(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("Model is required");
        }
        if (Objects.isNull(model.getReservationId())) {
            throw new IllegalArgumentException("Reservation id is required");
        }
        if (StringUtils.isBlank(model.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }
        if (Objects.isNull(model.getFrom())) {
            throw new IllegalArgumentException("Date from is required");
        }

        if (Objects.isNull(model.getTo())) {
            throw new IllegalArgumentException("Date to is required");
        }
    }
}
