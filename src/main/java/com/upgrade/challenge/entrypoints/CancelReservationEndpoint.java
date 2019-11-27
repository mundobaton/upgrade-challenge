package com.upgrade.challenge.entrypoints;

import lombok.Builder;
import lombok.Getter;

public interface CancelReservationEndpoint {

    Void execute(RequestModel model);

    @Builder
    @Getter
    class RequestModel {
        private Long reservationId;
        private String email;
    }

}
