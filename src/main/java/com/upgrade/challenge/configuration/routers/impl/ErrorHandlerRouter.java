package com.upgrade.challenge.configuration.routers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.challenge.configuration.Config;
import com.upgrade.challenge.configuration.Main;
import com.upgrade.challenge.core.exceptions.APIException;
import com.upgrade.challenge.core.exceptions.InternalServerErrorException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import spark.Request;
import spark.Response;

import static spark.Spark.exception;
import static spark.Spark.notFound;

@Slf4j
public class ErrorHandlerRouter {

    /**
     * Register basic error handle.
     * If you need custom handling extend this class and register it to the application module.
     */
    public void register() {
        exception(APIException.class, this::apiException);
        exception(Exception.class, this::genericError);

        notFound(this::handleNotFound);
    }

    /**
     * API Exception handler.
     *
     * @param e
     * @param request
     * @param response
     */
    protected void apiException(APIException e, Request request, Response response) {
        respond(response, getErrorResponse(e.getStatusCode(), e.getErrorCode(), e, true));
    }

    /**
     * Generic Error
     *
     * @param e
     * @param request
     * @param response
     */
    protected void genericError(Exception e, Request request, Response response) {
        log.error("Responding with error", e);
        respond(response, getErrorResponse(500, "runtime_error", e, true));
    }

    /**
     * Not found route
     *
     * @param req
     * @param res
     * @return
     */
    public Object handleNotFound(Request req, Response res) {
        return respond(res, new ErrorResponse(404, "not_found", req.url()));
    }


    /**
     * Respond with the json error.
     *
     * @param response
     * @param errorResponse
     * @return
     */
    protected String respond(Response response, ErrorResponse errorResponse) {
        response.status(errorResponse.statusCode);
        response.header("Content-Type", "application/json");
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            response.body(json);
            return json;
        } catch (Exception e) {
            throw new InternalServerErrorException("Application not properly configured!");
        }
    }

    protected ErrorResponse getErrorResponse(int statusCode, String errorCode, Throwable error, Boolean showStackTrace) {
        return new ErrorResponse(statusCode, errorCode, error, showStackTrace);
    }

    protected ObjectMapper objectMapper = Config.getInjector(Main.APP).getInstance(ObjectMapper.class);


    /**
     * Represent an Error Response based on an Exception.
     */
    @Getter
    public static class ErrorResponse {

        private final Integer statusCode;
        private final String code;
        private final String message;
        private final String stacktrace;

        public ErrorResponse(Integer statusCode, String errorCode, String errorMessage) {
            this.statusCode = statusCode;
            this.message = errorMessage;
            this.code = errorCode;
            this.stacktrace = null;
        }

        /**
         * @param statusCode     Response status code
         * @param errorCode
         * @param exception      Exception to make the response
         * @param showStackTrace Display or not the stacktrace. For security issues
         */
        public ErrorResponse(int statusCode, String errorCode, Throwable exception, Boolean showStackTrace) {
            this.statusCode = statusCode;
            this.code = errorCode;
            this.message = exception.getMessage();
            this.stacktrace = showStackTrace ? ExceptionUtils.getStackTrace(exception) : null;
        }

    }
}