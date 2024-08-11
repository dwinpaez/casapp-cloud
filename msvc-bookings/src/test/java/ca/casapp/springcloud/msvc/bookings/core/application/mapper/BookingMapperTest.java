package ca.casapp.springcloud.msvc.bookings.core.application.mapper;

import ca.casapp.springcloud.msvc.bookings.core.api.domain.BookingDomain;
import ca.casapp.springcloud.msvc.bookings.core.api.type.BookingState;
import ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity.BookingEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private final BookingMapper mapper = new BookingMapper();

    @Test
    public void testToDomainWithValidEntity() {
        BookingEntity entity = new BookingEntity();
        entity.setId(1L);
        entity.setClientId(2L);
        entity.setState(BookingState.CREATED);
        entity.setTotal(100.0D);
        entity.setStartDate(new Date());
        entity.setEndDate(new Date());
        entity.setCreationDate(new Date());

        BookingDomain domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.id());
        assertEquals(entity.getClientId(), domain.clientId());
        assertEquals(entity.getState(), domain.state());
        assertEquals(entity.getTotal(), domain.total());
        assertEquals(entity.getStartDate(), domain.startDate());
        assertEquals(entity.getEndDate(), domain.endDate());
        assertEquals(entity.getCreationDate(), domain.creationDate());
    }

    @Test
    public void testToDomainWithNullEntity() {
        BookingDomain domain = mapper.toDomain((BookingEntity) null);

        assertNull(domain);
    }

    @Test
    public void testToDomainWithValidEntities() {
        BookingEntity entity1 = new BookingEntity();
        entity1.setId(1L);
        entity1.setClientId(2L);
        entity1.setState(BookingState.CREATED);
        entity1.setTotal(100.0);
        entity1.setStartDate(new Date());
        entity1.setEndDate(new Date());
        entity1.setCreationDate(new Date());

        BookingEntity entity2 = new BookingEntity();
        entity2.setId(2L);
        entity2.setClientId(3L);
        entity2.setState(BookingState.ACCEPTED);
        entity2.setTotal(200.0);
        entity2.setStartDate(new Date());
        entity2.setEndDate(new Date());
        entity2.setCreationDate(new Date());

        List<BookingEntity> entities = List.of(entity1, entity2);

        List<BookingDomain> domains = mapper.toDomain(entities);

        assertNotNull(domains);
        assertEquals(2, domains.size());

        BookingDomain domain1 = domains.get(0);
        assertEquals(entity1.getId(), domain1.id());
        assertEquals(entity1.getClientId(), domain1.clientId());
        assertEquals(entity1.getState(), domain1.state());
        assertEquals(entity1.getTotal(), domain1.total());
        assertEquals(entity1.getStartDate(), domain1.startDate());
        assertEquals(entity1.getEndDate(), domain1.endDate());
        assertEquals(entity1.getCreationDate(), domain1.creationDate());

        BookingDomain domain2 = domains.get(1);
        assertEquals(entity2.getId(), domain2.id());
        assertEquals(entity2.getClientId(), domain2.clientId());
        assertEquals(entity2.getState(), domain2.state());
        assertEquals(entity2.getTotal(), domain2.total());
        assertEquals(entity2.getStartDate(), domain2.startDate());
        assertEquals(entity2.getEndDate(), domain2.endDate());
        assertEquals(entity2.getCreationDate(), domain2.creationDate());
    }

    @Test
    public void testToDomainWithNullEntities() {
        List<BookingDomain> domains = mapper.toDomain((List<BookingEntity>) null);

        assertNotNull(domains);
        assertTrue(domains.isEmpty());
    }

    @Test
    public void testToDomainWithEmptyEntities() {
        List<BookingEntity> entities = Collections.emptyList();

        List<BookingDomain> domains = mapper.toDomain(entities);

        assertNotNull(domains);
        assertTrue(domains.isEmpty());
    }

    @Test
    public void testToEntityWithValidCreateRequest() {
        BookingDomain.CreateRequest createRequest = BookingDomain.CreateRequest.builder()
                .clientId(2L)
                .total(100.0)
                .startDate(new Date())
                .endDate(new Date())
                .build();

        BookingEntity entity = mapper.toEntity(createRequest);

        assertNotNull(entity);
        assertEquals(createRequest.getClientId(), entity.getClientId());
        assertEquals(createRequest.getTotal(), entity.getTotal());
        assertEquals(createRequest.getStartDate(), entity.getStartDate());
        assertEquals(createRequest.getEndDate(), entity.getEndDate());
        assertNotNull(entity.getCreationDate());
    }

    @Test
    public void testToEntityWithNullCreateRequest() {
        assertThrows(NullPointerException.class, () -> mapper.toEntity(null));
    }
}