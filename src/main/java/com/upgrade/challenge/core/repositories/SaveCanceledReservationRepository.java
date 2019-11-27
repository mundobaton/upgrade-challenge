package com.upgrade.challenge.core.repositories;

import java.util.Date;

public interface SaveCanceledReservationRepository {

    void save(Long reservationId, String email, Date from, Date to, Date timestamp);

}
