package com.upgrade.challenge.entrypoints.impl;

import com.upgrade.challenge.core.dtos.ReservationDTO;
import com.upgrade.challenge.core.usecases.ModifyReservation;
import com.upgrade.challenge.core.usecases.ReservationNotFoundException;
import com.upgrade.challenge.core.usecases.ReservationValidationException;
import com.upgrade.challenge.entrypoints.BadRequestEndpointException;
import com.upgrade.challenge.entrypoints.ModifyReservationEndpoint;
import com.upgrade.challenge.entrypoints.NotFoundEndpointException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.Objects;

public class DefaultModifyReservationEndpoint implements ModifyReservationEndpoint {

    private ModifyReservation modifyReservation;

    @Inject
    public DefaultModifyReservationEndpoint(ModifyReservation modifyReservation) {
        this.modifyReservation = modifyReservation;
    }

    @Override
    public ReservationDTO execute(RequestModel requestModel) {
        try {
            validateParams(requestModel.getReservationId(), requestModel.getEmail(), requestModel.getFrom(), requestModel.getTo());
            modifyReservation.apply(ModifyReservation.Model.builder().reservationId(requestModel.getReservationId()).email(requestModel.getEmail()).from(requestModel.getFrom()).to(requestModel.getTo()).build());
            return ReservationDTO.builder().reservationId(requestModel.getReservationId()).build();

        } catch (IllegalArgumentException | ReservationValidationException e) {
            throw new BadRequestEndpointException(e);
        } catch (ReservationNotFoundException e) {
            throw new NotFoundEndpointException(e.getMessage());
        }
    }

    private void validateParams(Long reservationId, String email, Date from, Date to) {
        if (Objects.isNull(reservationId)) {
            throw new IllegalArgumentException("Reservation id is required");
        }
        if (reservationId <= 0) {
            throw new IllegalArgumentException("Invalid reservation id");
        }
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email is required");
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
