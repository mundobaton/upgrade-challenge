package com.upgrade.challenge.core.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class Reservation {

    private Long reservationId;
    private String email;
    private String fullName;
    private Date from;
    private Date to;

}
