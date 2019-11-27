package com.upgrade.challenge.core.usecases;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.function.Function;

public interface SaveCanceledReservation extends Function<SaveCanceledReservation.Model, Void> {

    @Builder
    @Data
    class Model {
        private Long reservationId;
        private String email;
        private Date from;
        private Date to;
    }
}
