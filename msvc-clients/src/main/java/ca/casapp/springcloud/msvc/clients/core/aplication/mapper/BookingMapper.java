package ca.casapp.springcloud.msvc.clients.core.aplication.mapper;


import ca.casapp.springcloud.msvc.clients.core.api.domain.BookingDomain;
import ca.casapp.springcloud.msvc.clients.core.aplication.rest.model.BookingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
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

    public BookingDomain toDomain(BookingModel model) {
        if (Objects.isNull(model))
            return null;
        return BookingDomain.builder()
                .id(model.id())
                .state(model.state())
                .startDate(model.startDate())
                .endDate(model.endDate())
                .total(model.total())
                .clientId(model.clientId())
                .build();
    }

    public List<BookingDomain> toDomain(List<BookingModel> models) {
        if (Objects.isNull(models))
            return Collections.emptyList();
        return models.stream().map(domain -> toDomain(domain)).collect(Collectors.toList());
    }
}
