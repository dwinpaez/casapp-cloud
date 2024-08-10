package ca.casapp.springcloud.msvc.bookings.core.application.persistence;

import ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity.BookingEntity;
import ca.casapp.springcloud.msvc.bookings.core.application.persistence.repository.BookingEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class BookingRepositoryHandlerTest {

    @Mock
    private BookingEntityRepository repository;
    BookingRepositoryHandler bookingRepositoryHandler;

    @BeforeEach
    void setUp() {
        initMocks(this);
        bookingRepositoryHandler = new BookingRepositoryHandler(repository);
    }

    @Test
    void save() {
        BookingEntity entity = new BookingEntity();
        entity.setId(1L);

        when(repository.saveAndFlush(entity)).thenReturn(entity);
        BookingEntity savedEntity = bookingRepositoryHandler.save(entity);

        assertEquals(savedEntity, entity);

        verify(repository, times(1)).saveAndFlush(entity);
    }

    @Test
    void findById() {
        BookingEntity entity = new BookingEntity();
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        Optional<BookingEntity> foundEntity = bookingRepositoryHandler.findById(1L);

        assertEquals(foundEntity.orElseThrow(), entity);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findAll() {
        BookingEntity entity = new BookingEntity();
        entity.setId(1L);

        when(repository.findAll()).thenReturn(List.of(entity));

        List<BookingEntity> entities = bookingRepositoryHandler.findAll(Map.of());
        assertEquals(entities, List.of(entity));
        assertFalse(entities.isEmpty());

        verify(repository, times(1)).findAll();
    }

    @Test
    void findByClient() {
        BookingEntity entity = new BookingEntity();
        entity.setId(1L);

        when(repository.findByClientId(1L)).thenReturn(List.of(entity));

        List<BookingEntity> entities = bookingRepositoryHandler.findByClient(1L);
        assertEquals(entities, List.of(entity));
        assertFalse(entities.isEmpty());

        verify(repository, times(1)).findByClientId(1L);
    }

    @Test
    void deleteById() {
        doNothing().when(repository).deleteById(1L);

        bookingRepositoryHandler.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}