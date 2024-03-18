package ca.casapp.springcloud.msvc.clients.core.api.domain;

import com.google.gson.Gson;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 * @version 2024/02/09
 */
@Builder
public record ClientDomain(
        long id,
        String email,
        String phone,
        String firstName,
        String lastName,
        Date dateOfBirth) implements Serializable {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Data
    @Builder
    public static class CreateRequest implements Serializable {

        @NotEmpty
        @Email
        private String email;
        @NotEmpty
        private String firstName;
        private String lastName;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Data
    @Builder
    public static class UpdateRequestName implements Serializable {

        @NotNull
        private Long id;
        @NotEmpty
        private String firstName;
        private String lastName;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
