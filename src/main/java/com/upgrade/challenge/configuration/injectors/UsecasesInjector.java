package com.upgrade.challenge.configuration.injectors;

import com.google.inject.AbstractModule;
import com.upgrade.challenge.core.usecases.*;
import com.upgrade.challenge.core.usecases.impl.*;

public class UsecasesInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(SaveReservation.class).to(DefaultSaveReservation.class);
        bind(ValidateReservation.class).to(DefaultValidateReservation.class);
        bind(FindContiguousReservation.class).to(DefaultFindContiguousReservation.class);
        bind(ValidateDateAvailable.class).to(DefaultValidateDateAvailable.class);
        bind(GetAvailability.class).to(DefaultGetAvailability.class);
        bind(ModifyReservation.class).to(DefaultModifyReservation.class);
        bind(GetReservation.class).to(DefaultGetReservation.class);
        bind(CancelReservation.class).to(DefaultCancelReservation.class);
        bind(DeleteReservation.class).to(DefaultDeleteReservation.class);
        bind(SaveCanceledReservation.class).to(DefaultSaveCanceledReservation.class);
    }
}
