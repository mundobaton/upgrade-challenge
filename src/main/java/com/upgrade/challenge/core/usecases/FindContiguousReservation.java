package com.upgrade.challenge.core.usecases;

import com.upgrade.challenge.core.model.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface FindContiguousReservation extends Function<FindContiguousReservation.Model, Optional<Reservation>> {

    @Builder
    @Getter
    class Model {
        private String email;
        private Date from;
        private Date to;
    }


}
