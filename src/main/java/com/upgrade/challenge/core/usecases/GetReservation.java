package com.upgrade.challenge.core.usecases;

import com.upgrade.challenge.core.model.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

public interface GetReservation extends Function<GetReservation.Model, Reservation> {

    @Builder
    @Getter
    class Model {
        Long reservationId;
    }

}
