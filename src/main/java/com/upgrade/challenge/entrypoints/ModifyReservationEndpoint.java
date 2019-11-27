package com.upgrade.challenge.entrypoints;

import com.upgrade.challenge.core.dtos.ReservationDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

public interface ModifyReservationEndpoint {

    ReservationDTO execute(RequestModel requestModel);

    @Builder
    @Getter
    class RequestModel {
        private Long reservationId;
        private String email;
        private Date from;
        private Date to;
    }

}
