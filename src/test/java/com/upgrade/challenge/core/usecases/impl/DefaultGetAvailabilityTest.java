package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.GetReservationsRepository;
import com.upgrade.challenge.core.usecases.GetAvailability;
import com.upgrade.challenge.core.usecases.UseCaseException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

public class DefaultGetAvailabilityTest {

    private GetReservationsRepository repo;
    private GetAvailability instance;
    private Date from;
    private Date to;

    @Before
    public void setup() {

        to = DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DATE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -60);
        from = DateUtils.truncate(cal.getTime(), Calendar.DATE);

        repo = Mockito.mock(GetReservationsRepository.class);
        instance = new DefaultGetAvailability(repo);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutModel() {
        instance.apply(null);
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutFromDate() {
        instance.apply(GetAvailability.Model.builder().from(null).to(to).build());
    }

    @Test(expected = UseCaseException.class)
    public void testExecuteWithoutToDate() {
        instance.apply(GetAvailability.Model.builder().from(from).to(null).build());
    }
}
