package ca.casapp.springcloud.msvc.bookings.web.controller;

import ca.casapp.springcloud.msvc.bookings.core.api.BookingService;
import ca.casapp.springcloud.msvc.bookings.core.api.domain.BookingDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for {@link BookingDomain}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 * @version 3.0-SNAPSHOT 2021/04/01
 * @since 1.8
 */
@Slf4j
@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingDomain>> findAllBookings() {
        log.debug("method: findAllBookings()");
        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDomain> findBookingById(@PathVariable("bookingId") Long bookingId) {
        log.debug("method: findBookingById({})", bookingId);
        final var optional = bookingService.findBookingById(bookingId);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }

    @GetMapping("/my-bookings/{clientId}")
    public ResponseEntity<List<BookingDomain>> findBookinsByClient(@PathVariable("clientId") Long clientId) {
        log.debug("method: findBookinsByClient({})", clientId);
        return ResponseEntity.ok(bookingService.findBookingsByClient(clientId));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<BookingDomain> deleteBookingById(@PathVariable("bookingId") Long bookingId) {
        log.debug("method: deleteBookingById({})", bookingId);
        final var optional = bookingService.findBookingById(bookingId);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookingService.deleteBookingById(optional.get().id());
        return ResponseEntity.noContent().build();
    }
}
