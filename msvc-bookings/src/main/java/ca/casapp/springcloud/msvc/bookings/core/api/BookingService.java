package ca.casapp.springcloud.msvc.bookings.core.api;

import ca.casapp.springcloud.msvc.bookings.core.api.domain.BookingDomain;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link BookingDomain}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
public interface BookingService {

    List<BookingDomain> findAllBookings();

    Optional<BookingDomain> findClientById(Long id);

    BookingDomain createBooking(BookingDomain.CreateRequest createRequest);

    void deleteBooking(Long id);
}
