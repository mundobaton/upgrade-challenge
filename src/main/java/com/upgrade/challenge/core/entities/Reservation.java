package com.upgrade.challenge.core.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class Reservation implements Serializable {

    private Long reservationId;
    private String email;
    private String fullName;
    private Date from;
    private Date to;

}
