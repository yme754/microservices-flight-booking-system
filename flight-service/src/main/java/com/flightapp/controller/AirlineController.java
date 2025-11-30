package com.flightapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.entity.Airline;
import com.flightapp.service.AirlineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/flight/airline")
@RequiredArgsConstructor
public class AirlineController {
	private final AirlineService airlineService;

    @GetMapping("/get")
    public Flux<Airline> getAllAirlines() {
        return airlineService.getAllAirlines();
    }

    @GetMapping("/get/{id}")
    public Mono<Airline> getById(@PathVariable String id) {
        return airlineService.getById(id);
    }
}
