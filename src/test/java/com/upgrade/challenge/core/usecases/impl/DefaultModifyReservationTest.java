package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.ModifyReservationRepository;
import com.upgrade.challenge.core.usecases.*;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

public class DefaultModifyReservationTest {

    private ValidateReservation validateReservation;
    private ValidateDateAvailable validateDateAvailable;
    private GetReservation getReservation;
    private ModifyReservationRepository repo;
    private ModifyReservation instance;
    private static final String EMAIL = "some@email.com";
    private static final Long RESERVATION_ID = 1L;
    private static final Date NOW = DateUtils.truncate(new Date(), Calendar.DATE);
    private Date tomorrow;

    @Before
    public void setup() {
        validateReservation = Mockito.mock(ValidateReservation.class);
        validateDateAvailable = Mockito.mock(ValidateDateAvailable.class);
        getReservation = Mockito.mock(GetReservation.class);
        repo = Mockito.mock(ModifyReservationRepository.class);
        instance = new DefaultModifyReservation(validateReservation, validateDateAvailable, repo, getReservation);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        tomorrow = DateUtils.truncate(cal.getTime(), Calendar.DATE);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutModel() {
        instance.apply(null);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutReservationId() {
        instance.apply(ModifyReservation.Model.builder().reservationId(null).email(EMAIL).from(new Date()).to(new Date()).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithInvalidReservationId() {
        instance.apply(ModifyReservation.Model.builder().reservationId(-1L).email(EMAIL).from(new Date()).to(new Date()).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutEmail() {
        instance.apply(ModifyReservation.Model.builder().reservationId(RESERVATION_ID).email(null).from(new Date()).to(new Date()).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutFrom() {
        instance.apply(ModifyReservation.Model.builder().reservationId(RESERVATION_ID).email(EMAIL).from(null).to(new Date()).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutTo() {
        instance.apply(ModifyReservation.Model.builder().reservationId(RESERVATION_ID).email(EMAIL).from(new Date()).to(null).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithInvalidDates() {
        instance.apply(ModifyReservation.Model.builder().reservationId(RESERVATION_ID).email(null).from(tomorrow).to(NOW).build());
    }
}
