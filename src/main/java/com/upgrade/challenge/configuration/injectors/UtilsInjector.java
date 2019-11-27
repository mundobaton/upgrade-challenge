package com.upgrade.challenge.configuration.injectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;

public class UtilsInjector extends AbstractModule {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    @Provides
    @Singleton
    public SimpleDateFormat provideDefaultDateFormat() {
        return new SimpleDateFormat(DATE_FORMAT);
    }
}
