package com.flightemail.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.flightapp.events.BookingCreatedEvent;
import com.flightemail.service.EmailSenderService;

@Service
public class BookingEventConsumer {
	private final EmailSenderService emailSenderService;

    public BookingEventConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "booking-created", groupId = "emailGroup")
    public void consume(BookingCreatedEvent event) {
        System.out.println("Received event: " + event);
        String subject = "Booking Confirmation - " + event.getPnr();
        String message = "Your booking is confirmed!\nPNR: " + event.getPnr();
        emailSenderService.sendEmail(event.getEmail(), subject, message);
    }
}
