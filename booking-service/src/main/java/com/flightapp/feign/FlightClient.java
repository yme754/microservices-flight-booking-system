package com.flightapp.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.flightapp.entity.Flight;
import com.flightapp.entity.Seat;

@FeignClient(name = "flight-service")
public interface FlightClient {
	
	@GetMapping("/flights/{flightId}")
    Flight getFlightById(@PathVariable String flightId);

    @PutMapping("/flights/{id}")
    void updateFlight(@PathVariable String id, @RequestBody Flight flight);

    @GetMapping("/flights/{id}/seats")
    List<Seat> getSeatsByFlightId(@PathVariable String id);

    @PutMapping("/flights/{id}/seats")
    void updateSeats(@PathVariable String id, @RequestBody List<Seat> seats);
}
