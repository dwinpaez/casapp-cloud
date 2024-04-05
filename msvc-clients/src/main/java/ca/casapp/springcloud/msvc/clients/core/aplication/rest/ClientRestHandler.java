package ca.casapp.springcloud.msvc.clients.core.aplication.rest;

import ca.casapp.springcloud.msvc.clients.core.api.rest.BookingRest;
import ca.casapp.springcloud.msvc.clients.core.aplication.rest.client.BookingRestClient;
import ca.casapp.springcloud.msvc.clients.core.aplication.rest.model.BookingModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handler for {@link BookingRest}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Component
public class ClientRestHandler implements BookingRest {

    private final BookingRestClient restClient;

    public ClientRestHandler(BookingRestClient repository) {
        this.restClient = repository;
    }

    @Override
    public List<BookingModel> findBookingsByClient(Long clientId) {
        return restClient.findBookingsByClient(clientId);
    }
}
