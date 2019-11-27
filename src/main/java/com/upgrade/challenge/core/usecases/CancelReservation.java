package com.upgrade.challenge.core.usecases;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

public interface CancelReservation extends Function<CancelReservation.Model, Void> {

    @Builder
    @Getter
    class Model {
        private Long reservationId;
        private String email;
    }


}
