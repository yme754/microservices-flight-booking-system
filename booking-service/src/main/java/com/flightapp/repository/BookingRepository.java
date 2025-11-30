package com.flightapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.flightapp.entity.Booking;

public interface BookingRepository extends ReactiveMongoRepository<Booking, String>{
	Optional<Booking> findByPnr(String pnr);
	Optional<Booking> findByEmail(String email);
}
