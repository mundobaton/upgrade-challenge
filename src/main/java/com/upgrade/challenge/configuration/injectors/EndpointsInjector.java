package com.upgrade.challenge.configuration.injectors;

import com.google.inject.AbstractModule;
import com.upgrade.challenge.configuration.routers.impl.DefaultModifyReservationRouter;
import com.upgrade.challenge.entrypoints.CancelReservationEndpoint;
import com.upgrade.challenge.entrypoints.GetAvailabilityEndpoint;
import com.upgrade.challenge.entrypoints.ModifyReservationEndpoint;
import com.upgrade.challenge.entrypoints.PerformReservationEndpoint;
import com.upgrade.challenge.entrypoints.impl.DefaultCancelReservationEndpoint;
import com.upgrade.challenge.entrypoints.impl.DefaultGetAvailabilityEndpoint;
import com.upgrade.challenge.entrypoints.impl.DefaultModifyReservationEndpoint;
import com.upgrade.challenge.entrypoints.impl.DefaultPerformReservationEndpoint;

public class EndpointsInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(GetAvailabilityEndpoint.class).to(DefaultGetAvailabilityEndpoint.class);
        bind(PerformReservationEndpoint.class).to(DefaultPerformReservationEndpoint.class);
        bind(ModifyReservationEndpoint.class).to(DefaultModifyReservationEndpoint.class);
        bind(CancelReservationEndpoint.class).to(DefaultCancelReservationEndpoint.class);
    }
}
