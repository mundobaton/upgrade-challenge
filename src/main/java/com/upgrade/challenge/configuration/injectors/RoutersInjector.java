package com.upgrade.challenge.configuration.injectors;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.upgrade.challenge.configuration.routers.BaseRouter;
import com.upgrade.challenge.configuration.routers.impl.DefaultCancelReservationRouter;
import com.upgrade.challenge.configuration.routers.impl.DefaultGetAvailabilityRouter;
import com.upgrade.challenge.configuration.routers.impl.DefaultModifyReservationRouter;
import com.upgrade.challenge.configuration.routers.impl.DefaultPerformReservationRouter;
import com.upgrade.challenge.core.dtos.AvailabilityDTO;
import com.upgrade.challenge.core.dtos.ReservationDTO;

public class RoutersInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<BaseRouter<AvailabilityDTO>>() {}).to(new TypeLiteral<DefaultGetAvailabilityRouter>() {});
        bind(new TypeLiteral<BaseRouter<ReservationDTO>>() {}).annotatedWith(Names.named("performReservation")).to(new TypeLiteral<DefaultPerformReservationRouter>() {});
        bind(new TypeLiteral<BaseRouter<ReservationDTO>>() {}).annotatedWith(Names.named("modifyReservation")).to(new TypeLiteral<DefaultModifyReservationRouter>() {});
        bind(new TypeLiteral<BaseRouter<Void>>() {}).to(new TypeLiteral<DefaultCancelReservationRouter>() {});
    }
}
