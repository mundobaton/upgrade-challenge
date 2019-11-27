package com.upgrade.challenge.configuration.routers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.challenge.configuration.routers.BaseRouter;
import com.upgrade.challenge.core.dtos.ReservationDTO;
import com.upgrade.challenge.core.exceptions.BadRequestException;
import com.upgrade.challenge.core.exceptions.InternalServerErrorException;
import com.upgrade.challenge.core.exceptions.NotFoundException;
import com.upgrade.challenge.entrypoints.BadRequestEndpointException;
import com.upgrade.challenge.entrypoints.ModifyReservationEndpoint;
import com.upgrade.challenge.entrypoints.NotFoundEndpointException;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Singleton
public class DefaultModifyReservationRouter implements BaseRouter<ReservationDTO> {

    private static final String RESERVATION_ID_PARAM = ":reservationId";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";

    private ObjectMapper objectMapper;
    private SimpleDateFormat defaultFormat;
    private ModifyReservationEndpoint modifyReservationEndpoint;

    @Inject
    public DefaultModifyReservationRouter(ObjectMapper objectMapper, SimpleDateFormat defaultFormat, ModifyReservationEndpoint modifyReservationEndpoint) {
        this.objectMapper = objectMapper;
        this.defaultFormat = defaultFormat;
        this.modifyReservationEndpoint = modifyReservationEndpoint;
    }

    @Override
    public ReservationDTO execute(Request request, Response response) {
        String reservationId = request.params(RESERVATION_ID_PARAM);

        try {
            Map<String, String> params = objectMapper.readValue(request.body(), Map.class);

            String email = params.get(PARAM_EMAIL);
            String fromStr = params.get(PARAM_FROM);
            String toStr = params.get(PARAM_TO);

            Date from = extractDate(fromStr);
            Date to = extractDate(toStr);

            return modifyReservationEndpoint.execute(ModifyReservationEndpoint.RequestModel.builder().reservationId(Long.parseLong(reservationId)).email(email).from(from).to(to).build());

        } catch (IllegalArgumentException | BadRequestEndpointException e) {
            throw new BadRequestException(e);
        } catch (NotFoundEndpointException nfe) {
            throw new NotFoundException(nfe.getMessage());
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
