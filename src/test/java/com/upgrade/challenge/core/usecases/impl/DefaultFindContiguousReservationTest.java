package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.FindContiguousReservationRepository;
import com.upgrade.challenge.core.usecases.FindContiguousReservation;
import com.upgrade.challenge.core.usecases.UseCaseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

public class DefaultFindContiguousReservationTest {

    private FindContiguousReservationRepository repo;
    private DefaultFindContiguousReservation instance;

    private static final String EMAIL = "some@email.com";
    private static final Date NOW = new Date();

    @Before
    public void setup() {
        repo = Mockito.mock(FindContiguousReservationRepository.class);
        instance = new DefaultFindContiguousReservation(repo);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutModel() {
        instance.apply(null);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutEmail() {
        instance.apply(FindContiguousReservation.Model.builder().email(null).from(new Date()).to(new Date()).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutFromDate() {
        instance.apply(FindContiguousReservation.Model.builder().email(EMAIL).from(null).to(new Date()).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutToDate() {
        instance.apply(FindContiguousReservation.Model.builder().email(EMAIL).from(new Date()).to(null).build());
    }
}
