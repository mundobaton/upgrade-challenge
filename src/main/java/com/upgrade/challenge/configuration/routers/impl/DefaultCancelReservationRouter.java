package com.upgrade.challenge.configuration.routers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.challenge.configuration.routers.BaseRouter;
import com.upgrade.challenge.core.exceptions.BadRequestException;
import com.upgrade.challenge.core.exceptions.InternalServerErrorException;
import com.upgrade.challenge.core.exceptions.NotFoundException;
import com.upgrade.challenge.entrypoints.BadRequestEndpointException;
import com.upgrade.challenge.entrypoints.CancelReservationEndpoint;
import com.upgrade.challenge.entrypoints.NotFoundEndpointException;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.Map;

public class DefaultCancelReservationRouter implements BaseRouter<Void> {

    private static final String PARAM_RESERVATION_ID = ":reservationId";
    private static final String PARAM_EMAIL = "email";
    private CancelReservationEndpoint cancelReservationEndpoint;
    private ObjectMapper objectMapper;

    @Inject
    public DefaultCancelReservationRouter(ObjectMapper objectMapper, CancelReservationEndpoint cancelReservationEndpoint) {
        this.objectMapper = objectMapper;
        this.cancelReservationEndpoint = cancelReservationEndpoint;
    }

    @Override
    public Void execute(Request request, Response response) {
        String reservationId = request.params(PARAM_RESERVATION_ID);
        try {
            Map<String, String> params = objectMapper.readValue(request.body(), Map.class);
            String email = params.get(PARAM_EMAIL);

            cancelReservationEndpoint.execute(CancelReservationEndpoint.RequestModel.builder().reservationId(Long.parseLong(reservationId)).email(email).build());
        } catch (IllegalArgumentException | BadRequestEndpointException e) {
            throw new BadRequestException(e);
        } catch (NotFoundEndpointException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }

        return null;
    }
}
