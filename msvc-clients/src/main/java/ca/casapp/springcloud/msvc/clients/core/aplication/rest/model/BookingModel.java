package ca.casapp.springcloud.msvc.clients.core.aplication.rest.model;

import ca.casapp.springcloud.msvc.clients.core.api.type.BookingState;
import com.google.gson.Gson;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 * @version 2024/02/09
 */
@Builder
public record BookingModel(
        long id,
        long clientId,
        BookingState state,
        Double total,
        Date startDate,
        Date endDate,
        Date creationDate

) implements Serializable {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
