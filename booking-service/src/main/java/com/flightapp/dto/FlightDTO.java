package com.flightapp.dto;

import lombok.Data;

@Data
public class FlightDTO {
	private String id;
    private int availableSeats;
    private String fromPlace;
    private String toPlace;
    private String airlineId;
}
