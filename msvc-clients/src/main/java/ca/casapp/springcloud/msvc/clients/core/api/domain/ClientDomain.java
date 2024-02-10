package ca.casapp.springcloud.msvc.clients.core.api.domain;

import com.google.gson.Gson;
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

        private String email;
        private String firstName;
        private String lastName;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Data
    @Builder
    public static class UpdateRequest implements Serializable {

        private Long id;
        private String firstName;
        private String lastName;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Data
    @Builder
    public static class DeleteRequest implements Serializable {

        private Long id;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
