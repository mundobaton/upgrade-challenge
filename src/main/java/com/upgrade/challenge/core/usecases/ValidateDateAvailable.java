package com.upgrade.challenge.core.usecases;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.function.Function;

public interface ValidateDateAvailable extends Function<ValidateDateAvailable.Model, Boolean> {

    @Builder
    @Getter
    class Model {
        private String email;
        private Date from;
        private Date to;
    }

}
