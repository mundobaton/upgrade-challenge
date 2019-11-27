package com.upgrade.challenge.core.usecases;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

public interface DeleteReservation extends Function<DeleteReservation.Model, Void> {

    @Builder
    @Getter
    class Model {
        private Long reservationId;
    }

}
