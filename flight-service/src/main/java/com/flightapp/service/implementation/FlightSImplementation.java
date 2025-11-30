package com.flightapp.service.implementation;

import java.util.List;

import com.flightapp.entity.Flight;
import com.flightapp.entity.Seat;
import com.flightapp.repository.FlightRepository;
import com.flightapp.repository.SeatRepository;
import com.flightapp.service.FlightService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FlightSImplementation implements FlightService{
	private final FlightRepository flightRepo;
    private final SeatRepository seatRepo;
    
    public FlightSImplementation(FlightRepository flightRepo, SeatRepository seatRepo) {
        this.flightRepo = flightRepo;
        this.seatRepo = seatRepo;
    }

    @Override
    public Mono<Flight> getFlightById(String id) {
        return flightRepo.findById(id);
    }

    @Override
    public Mono<Void> updateFlight(String id, Flight flight) {
        flight.setId(id);
        return flightRepo.save(flight).then();
    }

    @Override
    public Flux<Seat> getSeatsByFlightId(String flightId) {
        return seatRepo.findByFlightId(flightId);
    }

    @Override
    public Mono<Void> updateSeats(String flightId, List<Seat> seats) {
        return seatRepo.findByFlightId(flightId).flatMap(seatRepo::delete)
                .thenMany(Flux.fromIterable(seats)).flatMap(seatRepo::save).then();
    }

    @Override
    public Mono<Flight> reduceAvailableSeats(String flightId, int seatCount) {
        return flightRepo.findById(flightId)
                .flatMap(f -> {
                    if (f.getAvailableSeats() < seatCount) 
                    	return Mono.error(new RuntimeException("Not enough seats available"));
                    f.setAvailableSeats(f.getAvailableSeats() - seatCount);
                    return flightRepo.save(f);
                });
    }

    @Override
    public Mono<Flight> increaseAvailableSeats(String flightId, int seatCount) {
        return flightRepo.findById(flightId)
                .flatMap(f -> {f.setAvailableSeats(f.getAvailableSeats() + seatCount);
                    return flightRepo.save(f);
                });
    }
}
