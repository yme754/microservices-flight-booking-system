package com.flightapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.entity.Flight;
import com.flightapp.entity.Seat;
import com.flightapp.service.FlightService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/flight/flights")
public class FlightController {
	private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{id}")
    public Mono<Flight> getFlightById(@Valid @PathVariable String id) {
        return flightService.getFlightById(id);
    }

    @PutMapping("/{id}")
    public Mono<Void> updateFlight(@PathVariable String id, @RequestBody Flight flight) {
        return flightService.updateFlight(id, flight);
    }

    @GetMapping("/{id}/seats")
    public Flux<Seat> getSeatsByFlight(@PathVariable String id) {
        return flightService.getSeatsByFlightId(id);
    }

    @PutMapping("/{id}/seats")
    public Mono<Void> updateSeats(@PathVariable String id, @RequestBody List<Seat> seats) {
        return flightService.updateSeats(id, seats);
    }
}
