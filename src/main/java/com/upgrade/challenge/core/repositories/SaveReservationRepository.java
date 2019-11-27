package com.upgrade.challenge.core.repositories;

import com.upgrade.challenge.core.entities.Reservation;

import java.util.Date;

public interface SaveReservationRepository {

    Reservation save(String email, String fullname, Date from, Date to);

}
