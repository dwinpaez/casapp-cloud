package ca.casapp.springcloud.msvc.clients.core.api.rest;

import ca.casapp.springcloud.msvc.clients.core.aplication.rest.model.BookingModel;

import java.util.List;

/**
 * Service for {@link BookingModel}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
public interface BookingRest {
    List<BookingModel> findBookingsByClient(Long clientId);
}
