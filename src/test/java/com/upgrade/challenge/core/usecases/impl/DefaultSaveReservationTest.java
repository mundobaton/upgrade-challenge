package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.SaveReservationRepository;
import com.upgrade.challenge.core.usecases.*;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

public class DefaultSaveReservationTest {

    private ValidateDateAvailable validateDateAvailable;
    private ValidateReservation validateReservation;
    private SaveReservationRepository repo;
    private SaveReservation instance;
    private static final String EMAIL = "some@email.com";
    private static final String FULLNAME = "John Doe";
    private static final Long RESERVATION_ID = 1L;
    private static final Date NOW = DateUtils.truncate(new Date(), Calendar.DATE);
    private Date tomorrow;

    @Before
    public void setup() {
        validateDateAvailable = Mockito.mock(ValidateDateAvailable.class);
        validateReservation = Mockito.mock(ValidateReservation.class);
        repo = Mockito.mock(SaveReservationRepository.class);
        instance = new DefaultSaveReservation(validateReservation, repo, validateDateAvailable);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        tomorrow = DateUtils.truncate(cal.getTime(), Calendar.DATE);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutModel() {
        instance.apply(null);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutEmail() {
        instance.apply(SaveReservation.Model.builder().email(null).fullname(FULLNAME).from(NOW).to(tomorrow).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutFullname() {
        instance.apply(SaveReservation.Model.builder().email(EMAIL).fullname(null).from(NOW).to(tomorrow).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutFrom() {
        instance.apply(SaveReservation.Model.builder().email(EMAIL).fullname(FULLNAME).from(null).to(tomorrow).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutTo() {
        instance.apply(SaveReservation.Model.builder().email(EMAIL).fullname(FULLNAME).from(NOW).to(null).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithInvalidDates() {
        instance.apply(SaveReservation.Model.builder().email(EMAIL).fullname(FULLNAME).from(tomorrow).to(NOW).build());
    }
}
