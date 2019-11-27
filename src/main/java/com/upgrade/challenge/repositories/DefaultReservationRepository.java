package com.upgrade.challenge.repositories;

import com.upgrade.challenge.core.entities.Reservation;
import com.upgrade.challenge.core.repositories.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.cache2k.Cache;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DefaultReservationRepository extends BaseRepository implements SaveReservationRepository, FindContiguousReservationRepository, DateAvailableRepository, GetReservationsRepository, ModifyReservationRepository, FindReservationRepository, DeleteReservationRepository {

    private Cache<Long, Reservation> cache;

    @Inject
    public DefaultReservationRepository(QueryRunner queryRunner, Cache<Long, Reservation> cache) {
        super(queryRunner);
        this.cache = cache;
    }

    @Override
    public Reservation save(String email, String fullname, Date from, Date to) {
        String query = "INSERT INTO reservations(email, fullname, from_date, to_date) VALUES (?,?,?,?)";
        try {
            long reservationId = insert(query, (ResultSet rs) -> {
                return null;
            }, email, fullname, from, to);

            Reservation reservation = new Reservation();
            reservation.setReservationId(reservationId);
            reservation.setEmail(email);
            reservation.setFullName(fullname);
            reservation.setFrom(from);
            reservation.setTo(to);

            //Store the value into the cache
            cache.put(reservationId, reservation);

            return reservation;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Reservation> findContiguousReservation(String email, Date from, Date to) {
        Reservation reservation;
        String query = "SELECT reservation_id, fullname, from_date, to_date " +
                "FROM reservations WHERE email = ? " +
                "AND (from_date = ? OR to_date = ?)";
        try {
            reservation = select(query, (ResultSet rs) -> {
                Reservation result = null;
                if (rs.first()) {
                    result = new Reservation();
                    result.setReservationId(rs.getLong("reservation_id"));
                    result.setEmail(email);
                    result.setFullName(rs.getString("fullname"));
                    result.setFrom(rs.getDate("from_date"));
                    result.setTo(rs.getDate("to_date"));
                }
                return result;
            }, email, to, from);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(reservation);
    }

    @Override
    public Boolean dateAvailable(Date from, Date to, String email) {
        Boolean foundReservation;
        String query = "SELECT 1 " +
                "FROM reservations WHERE ((from_date < ? AND to_date > ?) OR (from_date < ? AND to_date > ?) OR (from_date = ? AND to_date = ?))";
        Object params[] = {from, from, to, to, from, to};
        if (!StringUtils.isBlank(email)) {
            params = Arrays.copyOf(params, params.length + 1);
            params[params.length - 1] = email;
            query = query.concat(" AND email != ?");
        }
        try {
            foundReservation = select(query, ResultSet::first, params);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return !foundReservation;
    }

    @Override
    public List<Reservation> getReservations(Date from, Date to) {
        List<Reservation> reservations;
        String query = "SELECT reservation_id, email, fullname, from_date, to_date FROM reservations WHERE from_date >= ? and to_date <= ? ORDER BY from_date ASC";
        try {
            reservations = select(query, (ResultSet rs) -> {
                List<Reservation> result = new ArrayList<>();
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getLong("reservation_id"));
                    reservation.setEmail("email");
                    reservation.setFullName(rs.getString("fullname"));
                    reservation.setFrom(rs.getDate("from_date"));
                    reservation.setTo(rs.getDate("to_date"));

                    result.add(reservation);
                }
                return result;
            }, from, to);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return reservations;
    }

    @Override
    public void update(Long reservationId, Date from, Date to) {
        String query = "UPDATE reservations SET from_date = ?, to_date = ? WHERE reservation_id = ?";
        try {
            int rows = update(query, from, to, reservationId);
            if (rows == 0) {
                throw new RepositoryException(String.format("No rows updated for reservationId %d", reservationId));
            }

            // Refresh cache
            Reservation reservation = cache.get(reservationId);
            reservation.setFrom(from);
            reservation.setTo(to);
            cache.put(reservationId, reservation);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Reservation> find(long reservationId) {
        Reservation reservation;
        reservation = cache.get(reservationId);

        if (reservation != null) {
            return Optional.of(reservation);
        }

        String query = "SELECT email, fullname, from_date, to_date FROM reservations where reservation_id = ?";
        try {
            reservation = select(query, (ResultSet rs) -> {
                Reservation result = null;
                if (rs.first()) {
                    result = new Reservation();
                    result.setReservationId(reservationId);
                    result.setEmail(rs.getString("email"));
                    result.setFullName(rs.getString("fullname"));
                    result.setFrom(rs.getDate("from_date"));
                    result.setTo(rs.getDate("to_date"));
                }
                return result;
            }, reservationId);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(reservation);
    }

    @Override
    public void delete(Long reservationId) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        try {
            update(query, reservationId);
            // remove the item from the cache
            cache.remove(reservationId);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}

