package com.flightapp.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.flightapp.entity.Booking;
import com.flightapp.feign.FlightClient;
import com.flightapp.repository.BookingRepository;
import com.flightapp.service.BookingService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class BookingSImplementation implements BookingService{
	private final BookingRepository bookingRepo;

	private final FlightClient flightClient;

	public BookingSImplementation(BookingRepository bookingRepo, FlightClient flightClient) {
	    this.bookingRepo = bookingRepo;
	    this.flightClient = flightClient;
	}


    @Override
    public Mono<Booking> createBooking(Booking booking) {
        booking.setId(UUID.randomUUID().toString());
        booking.setPnr("PNR-" + booking.getId().substring(0, 6).toUpperCase());
        return bookingRepo.save(booking);
    }
    
    @Override
    public Mono<Booking> bookFlight(Booking bookingRequest) {

        return Mono.fromCallable(() -> flightClient.getFlightById(bookingRequest.getFlightId()))
                .flatMap(flight -> {
                    if (flight == null) 
                    	return Mono.error(new RuntimeException("Flight not found"));
                    if (flight.getAvailableSeats() < bookingRequest.getSeatCount()) return Mono.error(new RuntimeException("Not enough seats available"));
                    flight.setAvailableSeats(flight.getAvailableSeats() - bookingRequest.getSeatCount());
                    flightClient.updateFlight(flight.getId(), flight);
                    bookingRequest.setId(UUID.randomUUID().toString());
                    bookingRequest.setPnr("PNR-" + bookingRequest.getId().substring(0, 6).toUpperCase());
                    return bookingRepo.save(bookingRequest);
                });
    }

    @Override
    public Mono<Booking> getBookingByPnr(String pnr) {
        return bookingRepo.findAll()
                .filter(b -> pnr.equals(b.getPnr()))
                .singleOrEmpty();
    }

    @Override
    public Flux<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    @Override
    public Mono<Void> deleteBooking(String id) {
        return bookingRepo.deleteById(id);
    }

    @Override
    public Mono<Booking> updateSeatNumbers(String bookingId, List<String> seatNumbers) {
        return bookingRepo.findById(bookingId)
                .flatMap(b -> {b.setSeatNumbers(seatNumbers);
                    return bookingRepo.save(b);
                });
    }

    @Override
    public Mono<Booking> updatePassengerIds(String bookingId, List<String> passengerIds) {
        return bookingRepo.findById(bookingId)
                .flatMap(b -> {b.setPassengerIds(passengerIds);
                    return bookingRepo.save(b);
                });
    }

    @Override
    public Mono<Booking> updateTotalAmount(String bookingId, float amount) {
        return bookingRepo.findById(bookingId)
                .flatMap(b -> {b.setTotalAmount(amount);
                    return bookingRepo.save(b);
                });
    }
}
