package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.DateAvailableRepository;
import com.upgrade.challenge.core.usecases.ValidateDateAvailable;
import com.upgrade.challenge.core.usecases.UseCaseException;

import javax.inject.Inject;
import java.util.Objects;

public class DefaultValidateDateAvailable implements ValidateDateAvailable {

    private DateAvailableRepository dateAvailableRepository;

    @Inject
    public DefaultValidateDateAvailable(DateAvailableRepository dateAvailableRepository) {
        this.dateAvailableRepository = dateAvailableRepository;
    }

    @Override
    public Boolean apply(Model model) {
        try {
            validateModel(model);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("error checking date available", e);
        }

        return dateAvailableRepository.dateAvailable(model.getFrom(), model.getTo(), model.getEmail());
    }

    void validateModel(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("model is required");
        }
        if (Objects.isNull(model.getFrom())) {
            throw new IllegalArgumentException("from date is required");
        }
        if (Objects.isNull(model.getTo())) {
            throw new IllegalArgumentException("to date is required");
        }
    }
}
