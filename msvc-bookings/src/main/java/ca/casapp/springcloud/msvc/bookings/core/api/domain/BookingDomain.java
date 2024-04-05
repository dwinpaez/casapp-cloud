package ca.casapp.springcloud.msvc.bookings.core.api.domain;

import ca.casapp.springcloud.msvc.bookings.core.api.type.BookingState;
import com.google.gson.Gson;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 * @version 2024/02/09
 */
@Builder
public record BookingDomain(
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

    @Data
    @Builder
    public static class CreateRequest implements Serializable {

        @NotEmpty
        private Long clientId;
        @NotEmpty
        private Double total;
        private Date startDate;
        private Date endDate;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
