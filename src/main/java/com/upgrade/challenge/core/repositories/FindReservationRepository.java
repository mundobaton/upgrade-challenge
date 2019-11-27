package com.upgrade.challenge.core.repositories;

import com.upgrade.challenge.core.entities.Reservation;

import java.util.Optional;

public interface FindReservationRepository {

    Optional<Reservation> find(long reservationId);

}
