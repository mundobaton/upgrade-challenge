package com.upgrade.challenge.repositories;

import com.upgrade.challenge.core.repositories.RepositoryException;
import com.upgrade.challenge.core.repositories.SaveCanceledReservationRepository;
import org.apache.commons.dbutils.QueryRunner;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DefaultCanceledReservationRepository extends BaseRepository implements SaveCanceledReservationRepository {

    @Inject
    DefaultCanceledReservationRepository(QueryRunner queryRunner) {
        super(queryRunner);
    }

    @Override
    public void save(Long reservationId, String email, Date from, Date to, Date timestamp) {
        String query = "INSERT INTO canceled_reservations(reservation_id, email, from_date, to_date, timestamp) VALUES (?,?,?,?,?)";
        try {
            insert(query, (ResultSet rs) -> {
                return null;
            }, reservationId, email, from, to, timestamp);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
