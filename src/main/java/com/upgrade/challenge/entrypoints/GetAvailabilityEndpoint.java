package com.upgrade.challenge.entrypoints;

import com.upgrade.challenge.core.dtos.AvailabilityDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public interface GetAvailabilityEndpoint {

    AvailabilityDTO execute(RequestModel requestModel);

    @Builder
    @Getter
    @Setter
    class RequestModel {
        Date from;
        Date to;
    }

}
