package com.upgrade.challenge.core.model;

import lombok.Data;

import java.util.List;

@Data
public class Availability {

    private List<AvailabilityItem> reservations;

}
