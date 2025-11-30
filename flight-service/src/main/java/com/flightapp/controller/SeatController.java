package com.flightapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.entity.Seat;
import com.flightapp.service.SeatService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
	private final SeatService seatService;

    @GetMapping("/flight/{flightId}")
    public Flux<Seat> getSeatsByFlightId(@PathVariable String flightId) {
        return seatService.getSeatsByFlightId(flightId);
    }

    @PutMapping("/flights/{id}/seats")
    public Mono<Void> updateSeats(@PathVariable String id, @RequestBody List<Seat> seats) {
        return seatService.updateSeats(id, seats);
    }
}
