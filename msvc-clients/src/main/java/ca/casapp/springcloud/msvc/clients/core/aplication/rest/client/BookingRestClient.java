package ca.casapp.springcloud.msvc.clients.core.aplication.rest.client;

import ca.casapp.springcloud.msvc.clients.core.aplication.rest.model.BookingModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Service for {@link BookingModel}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */

@Component
@FeignClient(name = "msvc-bookings", url = "${feign.client.msvc-bookings.url:}")
public interface BookingRestClient {

    @GetMapping("/booking/my-bookings/{clientId}")
    List<BookingModel> findBookingsByClient(@PathVariable Long clientId);
}
