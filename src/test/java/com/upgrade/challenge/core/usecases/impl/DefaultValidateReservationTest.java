package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.usecases.FindContiguousReservation;
import com.upgrade.challenge.core.usecases.UseCaseException;
import com.upgrade.challenge.core.usecases.ValidateReservation;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

public class DefaultValidateReservationTest {

    private FindContiguousReservation findContiguousReservation;
    private ValidateReservation instance;
    private final static String EMAIL = "some@email.com";
    private final static Date NOW = DateUtils.truncate(new Date(), Calendar.DATE);
    private Date nextDay;

    @Before
    public void setup() {
        findContiguousReservation = Mockito.mock(FindContiguousReservation.class);
        instance = new DefaultValidateReservation(findContiguousReservation);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        nextDay = DateUtils.truncate(cal.getTime(), Calendar.DATE);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutModel() {
        instance.apply(null);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutFrom() {
        instance.apply(ValidateReservation.Model.builder().email(EMAIL).from(null).to(nextDay).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutTo() {
        instance.apply(ValidateReservation.Model.builder().email(EMAIL).from(NOW).to(null).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithInvalidDates() {
        instance.apply(ValidateReservation.Model.builder().email(EMAIL).from(nextDay).to(NOW).build());
    }
}
