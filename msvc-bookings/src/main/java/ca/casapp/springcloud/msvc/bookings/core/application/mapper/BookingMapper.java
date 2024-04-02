package ca.casapp.springcloud.msvc.bookings.core.application.mapper;


import ca.casapp.springcloud.msvc.bookings.core.api.domain.BookingDomain;
import ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity.BookingEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for {@link BookingDomain}.
 *
 * @author <a href="mailto:dwipaez@gmail.com">Dwim Paez</a>
 */
@Slf4j
@Component
public class BookingMapper {

    public BookingDomain toDomain(BookingEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return BookingDomain.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .state(entity.getState())
                .total(entity.getTotal())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .creationDate(entity.getCreationDate())
                .build();
    }

    public List<BookingDomain> toDomain(List<BookingEntity> entities) {
        if (Objects.isNull(entities))
            return Collections.emptyList();
        return entities.stream().map(entity -> toDomain(entity)).collect(Collectors.toList());
    }

    public BookingEntity toEntity(final BookingDomain.CreateRequest createRequest) {
        BookingEntity entity = new BookingEntity();
        entity.setClientId(createRequest.getClientId());
        entity.setTotal(createRequest.getTotal());
        entity.setStartDate(createRequest.getStartDate());
        entity.setEndDate(createRequest.getEndDate());
        entity.setCreationDate(new Date());
        return entity;
    }

}
