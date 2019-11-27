package com.upgrade.challenge.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.upgrade.challenge.configuration.injectors.*;
import com.upgrade.challenge.configuration.routers.BaseRouter;
import com.upgrade.challenge.configuration.routers.impl.ErrorHandlerRouter;
import com.upgrade.challenge.core.dtos.AvailabilityDTO;
import com.upgrade.challenge.core.dtos.ReservationDTO;
import lombok.extern.slf4j.Slf4j;
import spark.ResponseTransformer;
import spark.Route;

import static spark.Spark.*;

@Slf4j
public class Main {

    private static final int DEFAULT_PORT = 8080;
    public static final String APP = "app";
    private static final String DEFAULT_CONTENT_TYPE = "application/json";

    private ResponseTransformer responseTransformer;

    public static void main(String[] args) {
        new Main().init();
    }

    private void init() {
        configureServer();
        configureInjectors();
        configureDependencies();
        configureRoutes();
    }

    private void configureServer() {
        port(DEFAULT_PORT);

        after((request, response) -> {
            response.type(DEFAULT_CONTENT_TYPE);
        });
    }

    private void configureInjectors() {
        Config.addInjector(APP, Guice.createInjector(
                new RoutersInjector(),
                new EndpointsInjector(),
                new UsecasesInjector(),
                new RepositoriesInjector(),
                new DatabaseInjector(),
                new UtilsInjector()
        ));
    }

    private void configureDependencies() {
        this.responseTransformer = new JsonTransformer(Config.getInjector(APP).getInstance(ObjectMapper.class));
    }

    private void configureRoutes() {
        Config.getInjector(APP).getInstance(ErrorHandlerRouter.class).register();
        get("/availability", this.buildRoute(Key.get(new TypeLiteral<BaseRouter<AvailabilityDTO>>() {
        })), this.responseTransformer);
        put("/reservations", this.buildRoute(Key.get(new TypeLiteral<BaseRouter<ReservationDTO>>() {
        }, Names.named("performReservation"))), this.responseTransformer);
        post("/reservations/:reservationId", this.buildRoute(Key.get(new TypeLiteral<BaseRouter<ReservationDTO>>() {
        }, Names.named("modifyReservation"))), this.responseTransformer);
        delete("/reservations/:reservationId", this.buildRoute(Key.get(new TypeLiteral<BaseRouter<Void>>() {
        })), this.responseTransformer);
    }

    /**
     * Build a new route for each request.
     *
     * @param key
     * @param <T> Type that needs to extend a Base Router.
     * @return
     */
    private <T extends BaseRouter> Route buildRoute(Key<T> key) {
        log.debug("get new instance of " + key.getClass().getName());
        return (request, response) -> Config.getInjector(APP).getInstance(key).execute(request, response);
    }
}