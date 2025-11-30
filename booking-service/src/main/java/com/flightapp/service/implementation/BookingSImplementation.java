package com.flightapp.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.flightapp.entity.Booking;
import com.flightapp.repository.BookingRepository;
import com.flightapp.service.BookingService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class BookingSImplementation implements BookingService{
	private final BookingRepository bookingRepo;

    public BookingSImplementation(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Mono<Booking> createBooking(Booking booking) {
        booking.setId(UUID.randomUUID().toString());
        booking.setPnr("PNR-" + booking.getId().substring(0, 6).toUpperCase());
        return bookingRepo.save(booking);
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
