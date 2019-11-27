package com.upgrade.challenge.core.repositories;

import com.upgrade.challenge.core.entities.Reservation;

import java.util.Date;
import java.util.Optional;

public interface FindContiguousReservationRepository {

    Optional<Reservation> findContiguousReservation(String email, Date from, Date to);

}
