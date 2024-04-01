package ca.casapp.springcloud.msvc.clients.core.aplication.mapper;


import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import ca.casapp.springcloud.msvc.clients.core.aplication.persistence.entity.ClientEntity;
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

    public ClientDomain toDomain(ClientEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return ClientDomain.builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dateOfBirth(entity.getDateOfBirth())
                .email(entity.getEmail())
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
