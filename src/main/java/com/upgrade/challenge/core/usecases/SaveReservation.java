package com.upgrade.challenge.core.usecases;

import com.upgrade.challenge.core.model.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.function.Function;
import java.util.function.Supplier;

public interface SaveReservation extends Function<SaveReservation.Model, Reservation> {

    @Builder
    @Getter
    class Model {
        private String email;
        private String fullname;
        private Date from;
        private Date to;
    }

}
