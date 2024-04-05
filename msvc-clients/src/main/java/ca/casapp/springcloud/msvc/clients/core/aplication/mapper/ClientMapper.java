package ca.casapp.springcloud.msvc.clients.core.aplication.mapper;


import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import ca.casapp.springcloud.msvc.clients.core.aplication.persistence.entity.ClientEntity;
import ca.casapp.springcloud.msvc.clients.core.aplication.rest.model.BookingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for {@link ClientDomain}.
 *
 * @author <a href="mailto:dwipaez@gmail.com">Dwim Paez</a>
 */
@Slf4j
@Component
public class ClientMapper {

    private final BookingMapper bookingMapper;

    public ClientMapper(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    public ClientDomain toDomain(ClientEntity entity) {
        return toDomain(entity, Collections.emptyList());
    }

    public ClientDomain toDomain(ClientEntity entity, List<BookingModel> bookings) {
        if (Objects.isNull(entity))
            return null;
        return ClientDomain.builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dateOfBirth(entity.getDateOfBirth())
                .email(entity.getEmail())
                .bookings(bookingMapper.toDomain(bookings))
                .build();
    }

    public List<ClientDomain> toDomain(List<ClientEntity> entities) {
        if (Objects.isNull(entities))
            return Collections.emptyList();
        return entities.stream().map(entity -> toDomain(entity)).collect(Collectors.toList());
    }

    public ClientEntity toEntity(final ClientDomain.CreateRequest createRequest) {
        ClientEntity entity = new ClientEntity();
        entity.setEmail(createRequest.getEmail());
        entity.setFirstName(createRequest.getFirstName());
        entity.setLastName(createRequest.getLastName());
        return entity;
    }

}
