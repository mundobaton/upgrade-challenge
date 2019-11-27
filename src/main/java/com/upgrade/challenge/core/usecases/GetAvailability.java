package com.upgrade.challenge.core.usecases;

import com.upgrade.challenge.core.model.Availability;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.function.Function;
import java.util.function.Supplier;

public interface GetAvailability extends Function<GetAvailability.Model, Availability> {

    @Builder
    @Getter
    class Model {
        private Date from;
        private Date to;
    }
}
