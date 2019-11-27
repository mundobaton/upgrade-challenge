package com.upgrade.challenge.configuration.routers;

import spark.Request;
import spark.Response;

public interface BaseRouter<T> {
    T execute(Request request, Response response);
}
