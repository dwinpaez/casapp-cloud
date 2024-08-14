package ca.casapp.springcloud.msvc.bookings.core.api.domain;

import ca.casapp.springcloud.msvc.bookings.core.api.type.BookingState;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingDomainTest {

    private final Gson gson = new Gson();

    @Test
    public void testBookingDomainCreation() {
        BookingDomain bookingDomain = BookingDomain.builder()
                .id(1L)
                .clientId(100L)
                .state(BookingState.ACCEPTED)
                .total(200.0)
                .startDate(new Date())
                .endDate(new Date())
                .creationDate(new Date())
                .build();

        assertNotNull(bookingDomain);
        assertEquals(1L, bookingDomain.id());
        assertEquals(100L, bookingDomain.clientId());
        assertEquals(BookingState.ACCEPTED, bookingDomain.state());
        assertEquals(200.0, bookingDomain.total());
        assertNotNull(bookingDomain.startDate());
        assertNotNull(bookingDomain.endDate());
        assertNotNull(bookingDomain.creationDate());
    }

    @Test
    public void testBookingDomainToString() {
        BookingDomain bookingDomain = BookingDomain.builder()
                .id(1L)
                .clientId(100L)
                .state(BookingState.ACCEPTED)
                .total(200.0)
                .startDate(new Date())
                .endDate(new Date())
                .creationDate(new Date())
                .build();

        String jsonString = bookingDomain.toString();
        BookingDomain deserialized = gson.fromJson(jsonString, BookingDomain.class);

        assertEquals(bookingDomain.toString(), deserialized.toString());
    }

    @Test
    public void testCreateRequestCreation() {
        BookingDomain.CreateRequest createRequest = BookingDomain.CreateRequest.builder()
                .clientId(100L)
                .total(200.0)
                .startDate(new Date())
                .endDate(new Date())
                .build();

        assertNotNull(createRequest);
        assertEquals(100L, createRequest.getClientId());
        assertEquals(200.0, createRequest.getTotal());
        assertNotNull(createRequest.getStartDate());
        assertNotNull(createRequest.getEndDate());
    }

    @Test
    public void testCreateRequestToString() {
        BookingDomain.CreateRequest createRequest = BookingDomain.CreateRequest.builder()
                .clientId(100L)
                .total(200.0)
                .startDate(new Date())
                .endDate(new Date())
                .build();

        String jsonString = createRequest.toString();
        BookingDomain.CreateRequest deserialized = gson.fromJson(jsonString, BookingDomain.CreateRequest.class);

        assertEquals(createRequest.toString(), deserialized.toString());
    }
}
