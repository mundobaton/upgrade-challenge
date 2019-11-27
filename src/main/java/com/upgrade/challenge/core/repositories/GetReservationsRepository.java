package com.upgrade.challenge.core.repositories;

import com.upgrade.challenge.core.entities.Reservation;

import java.util.Date;
import java.util.List;

public interface GetReservationsRepository {

    List<Reservation> getReservations(Date from, Date to);

}
