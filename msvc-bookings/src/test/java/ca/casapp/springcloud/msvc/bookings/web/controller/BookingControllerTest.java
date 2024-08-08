package ca.casapp.springcloud.msvc.bookings.web.controller;

import ca.casapp.springcloud.msvc.bookings.core.api.BookingService;
import ca.casapp.springcloud.msvc.bookings.core.api.domain.BookingDomain;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private BookingService bookingService;

    @Test
    void testFindAllBookings() throws Exception {
        BookingDomain booking1 = mock(BookingDomain.class);
        BookingDomain booking2 = mock(BookingDomain.class);

        var bookings = Arrays.asList(booking1, booking2);
        when(bookingService.findAllBookings()).thenReturn(bookings);

        mockMvc.perform(get("/booking").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(bookings))));

        verify(bookingService, times(1)).findAllBookings();
    }

    @Test
    void testFindBookingById() throws Exception {
        Long bookingId = 1L;
        BookingDomain booking = mock(BookingDomain.class);
        when(bookingService.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/booking/{bookingId}", bookingId).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(booking))));

        verify(bookingService, times(1)).findBookingById(bookingId);
    }

    @Test
    void testFindBookingByIdNotFound() throws Exception {
        Long bookingId = 1L;
        when(bookingService.findBookingById(bookingId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/booking/{bookingId}", bookingId).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).findBookingById(bookingId);
    }

    @Test
    void testFindBookinsByClient() throws Exception {
        Long clientId = 1L;
        BookingDomain booking = mock(BookingDomain.class);
        when(bookingService.findBookingsByClient(clientId)).thenReturn(List.of(booking));

        mockMvc.perform(get("/booking/my-bookings/{clientId}", clientId).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(objectMapper.writeValueAsString(booking))));

        verify(bookingService, times(1)).findBookingsByClient(clientId);
    }

    @Test
    void testDeleteBookingById() throws Exception {
        Long bookingId = 1L;
        doNothing().when(bookingService).deleteBookingById(bookingId);

        BookingDomain booking = mock(BookingDomain.class);
        when(bookingService.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        mockMvc.perform(delete("/booking/{bookingId}", bookingId).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(bookingService, times(1)).deleteBookingById(booking.id());
    }

    @Test
    void testDeleteBookingByIdNotFound() throws Exception {
        Long bookingId = 1L;
        when(bookingService.findBookingById(bookingId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/booking/{bookingId}", bookingId).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).findBookingById(bookingId);
    }
}