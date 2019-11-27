package com.upgrade.challenge.core.repositories;

import java.util.Date;

public interface ModifyReservationRepository {

    void update(Long reservationId, Date from, Date to);

}
