package com.upgrade.challenge.entrypoints;

import com.upgrade.challenge.core.dtos.ReservationDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

public interface PerformReservationEndpoint {

    ReservationDTO execute(RequestModel requestModel);

    @Builder
    @Getter
    class RequestModel {
        private String email;
        private String fullname;
        private Date from;
        private Date to;
    }
}
