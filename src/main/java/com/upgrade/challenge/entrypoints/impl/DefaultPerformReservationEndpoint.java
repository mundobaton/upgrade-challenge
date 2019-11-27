package com.upgrade.challenge.entrypoints.impl;

import com.upgrade.challenge.core.dtos.ReservationDTO;
import com.upgrade.challenge.core.model.Reservation;
import com.upgrade.challenge.core.usecases.SaveReservation;
import com.upgrade.challenge.core.usecases.ReservationValidationException;
import com.upgrade.challenge.entrypoints.BadRequestEndpointException;
import com.upgrade.challenge.entrypoints.PerformReservationEndpoint;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Date;

public class DefaultPerformReservationEndpoint implements PerformReservationEndpoint {

    private SaveReservation saveReservation;

    @Inject
    public DefaultPerformReservationEndpoint(SaveReservation saveReservation) {
        this.saveReservation = saveReservation;
    }

    @Override
    public ReservationDTO execute(RequestModel requestModel) {
        try {
            validateParams(requestModel.getEmail(), requestModel.getFullname(), requestModel.getFrom(), requestModel.getTo());
            Reservation res = saveReservation.apply(SaveReservation.Model.builder().email(requestModel.getEmail()).fullname(requestModel.getFullname()).from(requestModel.getFrom()).to(requestModel.getTo()).build());
            return ReservationDTO.builder().reservationId(res.getReservationId()).build();
        } catch (IllegalArgumentException | ReservationValidationException e) {
            throw new BadRequestEndpointException(e);
        }
    }

    private void validateParams(String email, String fullName, Date from, Date to) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("email is required");
        }
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("fullname is required");
        }

        if (from == null) {
            throw new IllegalArgumentException("from is required");
        }

        if (to == null) {
            throw new IllegalArgumentException("to is required");
        }

        if (from.compareTo(to) >= 0) {
            throw new IllegalArgumentException("invalid from and to dates");
        }
    }
}
