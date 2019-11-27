package com.upgrade.challenge.entrypoints.impl;

import com.upgrade.challenge.core.dtos.AvailabilityDTO;
import com.upgrade.challenge.core.dtos.AvailabilityItemDTO;
import com.upgrade.challenge.core.model.Availability;
import com.upgrade.challenge.core.usecases.GetAvailability;
import com.upgrade.challenge.entrypoints.BadRequestEndpointException;
import com.upgrade.challenge.entrypoints.GetAvailabilityEndpoint;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DefaultGetAvailabilityEndpoint implements GetAvailabilityEndpoint {

    private GetAvailability getAvailability;

    @Inject
    public DefaultGetAvailabilityEndpoint(GetAvailability getAvailability) {
        this.getAvailability = getAvailability;
    }

    @Override
    public AvailabilityDTO execute(RequestModel requestModel) {
        try {
            fillDefaults(requestModel);
            validateModel(requestModel);
            Availability availability = getAvailability.apply(GetAvailability.Model.builder().from(requestModel.getFrom()).to(requestModel.getTo()).build());
            AvailabilityDTO availabilityDTO = new AvailabilityDTO();
            availabilityDTO.setAvailability(new ArrayList<>());

            availability.getReservations().forEach(r -> {
                AvailabilityItemDTO itemDTO = new AvailabilityItemDTO();
                itemDTO.setFrom(r.getFrom());
                itemDTO.setTo(r.getTo());
                availabilityDTO.getAvailability().add(itemDTO);
            });

            return availabilityDTO;
        } catch (IllegalArgumentException e) {
            throw new BadRequestEndpointException(e);
        }
    }

    void fillDefaults(RequestModel requestModel) {
        if (Objects.isNull(requestModel)) {
            throw new IllegalArgumentException("Request model is required");
        }

        if (Objects.isNull(requestModel.getFrom()) && Objects.isNull(requestModel.getTo())) {
            Calendar nowCal = Calendar.getInstance();
            nowCal.add(Calendar.DATE, 1);
            Date now = DateUtils.truncate(nowCal.getTime(), Calendar.DATE);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 1);
            Date defaultTo = DateUtils.truncate(cal.getTime(), Calendar.DATE);

            requestModel.setFrom(now);
            requestModel.setTo(defaultTo);
        }
    }

    void validateModel(RequestModel requestModel) {
        if (Objects.isNull(requestModel.getFrom())) {
            throw new IllegalArgumentException("from date is required");
        }

        if (Objects.isNull(requestModel.getTo())) {
            throw new IllegalArgumentException("to date is required");
        }

        if (requestModel.getFrom().compareTo(requestModel.getTo()) >= 0) {
            throw new IllegalArgumentException("Invalid from and to dates");
        }
    }
}
