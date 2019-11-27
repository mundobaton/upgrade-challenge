package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.FindReservationRepository;
import com.upgrade.challenge.core.usecases.GetReservation;
import com.upgrade.challenge.core.usecases.ReservationNotFoundException;
import com.upgrade.challenge.core.usecases.UseCaseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class DefaultGetReservationTest {

    private FindReservationRepository repo;
    private GetReservation instance;

    @Before
    public void setup() {
        repo = Mockito.mock(FindReservationRepository.class);
        instance = new DefaultGetReservation(repo);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutModel() {
        instance.apply(null);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutReservationId() {
        instance.apply(GetReservation.Model.builder().build());
    }

    @Test(expected = ReservationNotFoundException.class)
    public void testGetReservationNotFound() {
        Long reservationId = 1L;

        Mockito.when(repo.find(reservationId)).thenReturn(Optional.empty());
        instance.apply(GetReservation.Model.builder().reservationId(reservationId).build());
    }
}
