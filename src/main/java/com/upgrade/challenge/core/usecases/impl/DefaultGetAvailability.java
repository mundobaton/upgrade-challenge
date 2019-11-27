package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.entities.Reservation;
import com.upgrade.challenge.core.model.Availability;
import com.upgrade.challenge.core.model.AvailabilityItem;
import com.upgrade.challenge.core.repositories.GetReservationsRepository;
import com.upgrade.challenge.core.usecases.GetAvailability;
import com.upgrade.challenge.core.usecases.UseCaseException;

import javax.inject.Inject;
import java.util.*;

public class DefaultGetAvailability implements GetAvailability {

    private GetReservationsRepository getReservationsRepository;

    @Inject
    public DefaultGetAvailability(GetReservationsRepository getReservationsRepository) {
        this.getReservationsRepository = getReservationsRepository;
    }

    @Override
    public Availability apply(Model model) {
        try {
            validateModel(model);
            List<Reservation> reservations = getReservationsRepository.getReservations(model.getFrom(), model.getTo());
            return composeResult(model, reservations);
        } catch (IllegalArgumentException e) {
            throw new UseCaseException("Error getting availability", e);
        }
    }

    private Availability composeResult(Model model, List<Reservation> reservations) {
        Availability availability = new Availability();
        availability.setReservations(new ArrayList<>());
        AvailabilityItem item = new AvailabilityItem();
        item.setFrom(model.getFrom());

        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);

            item.setTo(r.getFrom());
            if (item.getFrom().compareTo(item.getTo()) != 0) {
                availability.getReservations().add(item);
            }
            item = new AvailabilityItem();
            item.setFrom(r.getTo());
        }

        item.setTo(model.getTo());
        if (item.getFrom().compareTo(item.getTo()) != 0) {
            availability.getReservations().add(item);
        }

        return availability;
    }

    void validateModel(Model model) {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("Model is required");
        }

        if (Objects.isNull(model.getFrom())) {
            throw new IllegalArgumentException("From date is required");
        }

        if (Objects.isNull(model.getTo())) {
            throw new IllegalArgumentException("To date is required");
        }
    }
}
