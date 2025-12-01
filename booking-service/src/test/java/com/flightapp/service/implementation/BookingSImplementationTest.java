package com.flightapp.service.implementation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import com.flightapp.config.Resilience4jTestConfig;
import com.flightapp.dto.FlightDTO;
import com.flightapp.entity.Booking;
import com.flightapp.feign.FlightClient;
import com.flightapp.kafka.BookingEventProducer;
import com.flightapp.repository.BookingRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Import(Resilience4jTestConfig.class)
public class BookingSImplementationTest {
	@Mock
    private BookingRepository bookingRepo;

    @Mock
    private FlightClient flightClient;
    
    @Mock
    private BookingEventProducer bookingEventProducer;

    @InjectMocks
    private BookingSImplementation bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        booking = new Booking();
        booking.setId(UUID.randomUUID().toString());
        booking.setFlightId("FL123");
        booking.setSeatCount(2);
        booking.setPassengerIds(List.of("P1", "P2"));
        booking.setSeatNumbers(List.of("1A", "1B"));
    }

    @Test
    void testCreateBooking() {
        when(bookingRepo.save(any(Booking.class))).thenReturn(Mono.just(booking));
        StepVerifier.create(bookingService.createBooking(booking))
                .expectNextMatches(b -> b.getPnr() != null).verifyComplete();
        verify(bookingRepo, times(1)).save(any(Booking.class));
    }

    @Test
    void testBookFlightSuccess() {
        FlightDTO flight = new FlightDTO();
        flight.setId("FL123");
        flight.setAvailableSeats(10);
        when(flightClient.getFlightById("FL123")).thenReturn(flight);
        when(bookingRepo.save(any(Booking.class))).thenReturn(Mono.just(booking));
        StepVerifier.create(bookingService.bookFlight(booking))
                .expectNextMatches(b -> b.getPnr() != null).verifyComplete();
        verify(flightClient, times(1)).updateFlight(eq("FL123"), any(FlightDTO.class));
        verify(bookingRepo, times(1)).save(any(Booking.class));
    }

    @Test
    void testBookFlight_FlightNotFound() {
        when(flightClient.getFlightById("FL123")).thenReturn(null);

        StepVerifier.create(bookingService.bookFlight(booking))
            .expectErrorMatches(ex -> ex instanceof RuntimeException
                    && ex.getMessage().equals("Flight not found"))
            .verify();
        
        verify(bookingRepo, never()).save(any());
    }


    @Test
    void testBookFlight_NotEnoughSeats() {
        FlightDTO flight = new FlightDTO();
        flight.setId("FL123");
        flight.setAvailableSeats(1);
        when(flightClient.getFlightById("FL123")).thenReturn(flight);
        StepVerifier.create(bookingService.bookFlight(booking))
                .expectErrorMatches(ex -> ex.getMessage().equals("Not enough seats available")).verify();
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void testGetBookingByPnr() {
        booking.setPnr("PNR-TEST");
        when(bookingRepo.findAll()).thenReturn(Flux.just(booking));
        StepVerifier.create(bookingService.getBookingByPnr("PNR-TEST"))
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepo.findAll()).thenReturn(Flux.just(booking));
        StepVerifier.create(bookingService.getAllBookings()).expectNext(booking).verifyComplete();
    }

    @Test
    void testDeleteBooking() {
        when(bookingRepo.deleteById("1")).thenReturn(Mono.empty());
        StepVerifier.create(bookingService.deleteBooking("1")).verifyComplete();
        verify(bookingRepo, times(1)).deleteById("1");
    }


    @Test
    void testUpdateSeatNumbers() {
        List<String> newSeats = Arrays.asList("2A", "2B");
        when(bookingRepo.findById("1")).thenReturn(Mono.just(booking));
        when(bookingRepo.save(any(Booking.class))).thenReturn(Mono.just(booking));
        StepVerifier.create(bookingService.updateSeatNumbers("1", newSeats)).expectNext(booking).verifyComplete();
    }

    @Test
    void testUpdatePassengerIds() {
        List<String> newPassengers = Arrays.asList("PX1", "PX2");
        when(bookingRepo.findById("1")).thenReturn(Mono.just(booking));
        when(bookingRepo.save(any(Booking.class))).thenReturn(Mono.just(booking));
        StepVerifier.create(bookingService.updatePassengerIds("1", newPassengers)).expectNext(booking).verifyComplete();
    }

    @Test
    void testUpdateTotalAmount() {
        when(bookingRepo.findById("1")).thenReturn(Mono.just(booking));
        when(bookingRepo.save(any(Booking.class))).thenReturn(Mono.just(booking));
        StepVerifier.create(bookingService.updateTotalAmount("1", 5000)).expectNext(booking).verifyComplete();
    }
}
