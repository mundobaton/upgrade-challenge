package com.upgrade.challenge.core.usecases.impl;

import com.upgrade.challenge.core.repositories.DateAvailableRepository;
import com.upgrade.challenge.core.usecases.UseCaseException;
import com.upgrade.challenge.core.usecases.ValidateDateAvailable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

public class DefaultValidateDateAvailableTest {

    private DateAvailableRepository repo;
    private ValidateDateAvailable instance;
    private static final String EMAIL = "some@email.com";

    @Before
    public void setup() {
        repo = Mockito.mock(DateAvailableRepository.class);
        instance = new DefaultValidateDateAvailable(repo);
    }

    @Test(expected = UseCaseException.class)
    public void testWithoutModel() {
        instance.apply(null);
    }

    @Test(expected = UseCaseException.class)
    public void testWithoutFrom() {
        instance.apply(ValidateDateAvailable.Model.builder().email(EMAIL).from(null).to(new Date()).build());
    }

    @Test(expected = UseCaseException.class)
    public void testWithoutTo() {
        instance.apply(ValidateDateAvailable.Model.builder().email(EMAIL).from(new Date()).to(null).build());
    }
}
