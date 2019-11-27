package com.upgrade.challenge.configuration.routers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.challenge.configuration.routers.BaseRouter;
import com.upgrade.challenge.core.dtos.ReservationDTO;
import com.upgrade.challenge.core.exceptions.BadRequestException;
import com.upgrade.challenge.core.exceptions.InternalServerErrorException;
import com.upgrade.challenge.entrypoints.BadRequestEndpointException;
import com.upgrade.challenge.entrypoints.PerformReservationEndpoint;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Singleton
public class DefaultPerformReservationRouter implements BaseRouter<ReservationDTO> {

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_FULLNAME = "fullname";
    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";
    private ObjectMapper objectMapper;
    private SimpleDateFormat defaultFormat;
    private PerformReservationEndpoint performReservationEndpoint;

    @Inject
    public DefaultPerformReservationRouter(ObjectMapper objectMapper, SimpleDateFormat defaultDateFormat, PerformReservationEndpoint performReservationEndpoint) {
        this.objectMapper = objectMapper;
        this.defaultFormat = defaultDateFormat;
        this.performReservationEndpoint = performReservationEndpoint;
    }

    @Override
    public ReservationDTO execute(Request request, Response response) {

        try {
            Map<String, String> params = objectMapper.readValue(request.body(), Map.class);

            String email = params.get(PARAM_EMAIL);
            String fullName = params.get(PARAM_FULLNAME);
            String fromStr = params.get(PARAM_FROM);
            String toStr = params.get(PARAM_TO);

            Date from = extractDate(fromStr);
            Date to = extractDate(toStr);

            return performReservationEndpoint.execute(PerformReservationEndpoint.RequestModel.builder().email(email).fullname(fullName).from(from).to(to).build());

        } catch (IllegalArgumentException | BadRequestEndpointException e) {
            throw new BadRequestException(e);
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
    }

    private Date extractDate(String source) {
        try {
            return this.defaultFormat.parse(source);
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
    }
}
