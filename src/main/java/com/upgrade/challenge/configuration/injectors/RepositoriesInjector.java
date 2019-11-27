package com.upgrade.challenge.configuration.injectors;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.upgrade.challenge.core.entities.Reservation;
import com.upgrade.challenge.core.repositories.*;
import com.upgrade.challenge.repositories.DefaultCanceledReservationRepository;
import com.upgrade.challenge.repositories.DefaultReservationRepository;
import org.apache.commons.dbutils.QueryRunner;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

public class RepositoriesInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(SaveReservationRepository.class).to(DefaultReservationRepository.class);
        bind(FindContiguousReservationRepository.class).to(DefaultReservationRepository.class);
        bind(DateAvailableRepository.class).to(DefaultReservationRepository.class);
        bind(GetReservationsRepository.class).to(DefaultReservationRepository.class);
        bind(ModifyReservationRepository.class).to(DefaultReservationRepository.class);
        bind(FindReservationRepository.class).to(DefaultReservationRepository.class);
        bind(DeleteReservationRepository.class).to(DefaultReservationRepository.class);
        bind(SaveCanceledReservationRepository.class).to(DefaultCanceledReservationRepository.class);
    }

    @Provides
    @Singleton
    @Inject
    QueryRunner provideQueryRunner(final DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Provides
    @Singleton
    Cache<Long, Reservation> provideReservationsCache() {
        return new Cache2kBuilder<Long, Reservation>() {}.name("reservationsCache").eternal(true).build();
    }
}
