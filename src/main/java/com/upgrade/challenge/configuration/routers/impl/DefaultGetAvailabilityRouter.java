package com.upgrade.challenge.configuration.routers.impl;

import com.upgrade.challenge.configuration.routers.BaseRouter;
import com.upgrade.challenge.core.dtos.AvailabilityDTO;
import com.upgrade.challenge.entrypoints.GetAvailabilityEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Singleton
public class DefaultGetAvailabilityRouter implements BaseRouter<AvailabilityDTO> {

    private GetAvailabilityEndpoint getAvailabilityEndpoint;
    private SimpleDateFormat defaultDateFormat;

    @Inject
    public DefaultGetAvailabilityRouter(SimpleDateFormat defaultDateFormat, GetAvailabilityEndpoint getAvailabilityEndpoint) {
        this.defaultDateFormat = defaultDateFormat;
        this.getAvailabilityEndpoint = getAvailabilityEndpoint;
    }

    @Override
    public AvailabilityDTO execute(Request request, Response response) {

        Date from = extractDate(request.queryParams("from"));
        Date to = extractDate(request.queryParams("to"));

        return getAvailabilityEndpoint.execute(GetAvailabilityEndpoint.RequestModel.builder().from(from).to(to).build());
    }

    private Date extractDate(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return this.defaultDateFormat.parse(source);
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
    }
}
