package com.flightapp.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.flightapp.events.BookingCreatedEvent;

@Service
public class BookingEventProducer {
	private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public BookingEventProducer(KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingCreatedEvent(BookingCreatedEvent event) {
        kafkaTemplate.send("booking-created", event);
    }
}
