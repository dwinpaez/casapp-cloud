package ca.casapp.springcloud.msvc.bookings.core.application;

import ca.casapp.springcloud.msvc.bookings.core.api.domain.BookingDomain;
import ca.casapp.springcloud.msvc.bookings.core.api.repository.BookingRepository;
import ca.casapp.springcloud.msvc.bookings.core.api.type.BookingState;
import ca.casapp.springcloud.msvc.bookings.core.application.mapper.BookingMapper;
import ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity.BookingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceHandlerTest {

    @Mock
    private BookingRepository repository;
    @Mock
    private BookingMapper mapper;
    @InjectMocks
    private BookingServiceHandler bookingServiceHandler;

    private BookingEntity bookingEntity;
    private BookingDomain bookingDomain;
    private BookingDomain.CreateRequest createRequest;
    private List<BookingEntity> bookingEntities;
    private List<BookingDomain> bookingDomains;

    @BeforeEach
    void setUp() {
        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);

        bookingDomain = new BookingDomain(1L, 1L, BookingState.CREATED, 100.0D, new Date(), new Date(), new Date());

        createRequest = BookingDomain.CreateRequest.builder().clientId(1L).total(100.0D).build();

        bookingEntities = List.of(bookingEntity);
        bookingDomains = List.of(bookingDomain);
    }

    @Test
    void findAllBookingsSuccess() {
        when(repository.findAll(Collections.emptyMap())).thenReturn(bookingEntities);
        when(mapper.toDomain(bookingEntities)).thenReturn(bookingDomains);

        List<BookingDomain> result = bookingServiceHandler.findAllBookings();

        assertEquals(bookingDomains, result);
        verify(repository, times(1)).findAll(Collections.emptyMap());
        verify(mapper, times(1)).toDomain(bookingEntities);
    }

    @Test
    public void testFindAllBookingsException() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(repository.findAll(Collections.emptyMap())).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookingServiceHandler.findAllBookings());

        assertEquals(exception, thrown);
        verify(repository, times(1)).findAll(Collections.emptyMap());
        verify(mapper, never()).toDomain(anyList());
    }

    @Test
    public void testFindBookingByIdSuccess() {
        Long bookingId = 1L;
        when(repository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(mapper.toDomain(bookingEntity)).thenReturn(bookingDomain);

        Optional<BookingDomain> result = bookingServiceHandler.findBookingById(bookingId);

        assertTrue(result.isPresent());
        assertEquals(bookingDomain, result.get());
        verify(repository, times(1)).findById(bookingId);
        verify(mapper, times(1)).toDomain(bookingEntity);
    }

    @Test
    public void testFindBookingByIdNotFound() {
        Long bookingId = 1L;
        when(repository.findById(bookingId)).thenReturn(Optional.empty());

        Optional<BookingDomain> result = bookingServiceHandler.findBookingById(bookingId);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(bookingId);
        verify(mapper, never()).toDomain(any(BookingEntity.class));
    }

    @Test
    public void testFindBookingByIdException() {
        Long bookingId = 1L;
        RuntimeException exception = new RuntimeException("Test exception");
        when(repository.findById(bookingId)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookingServiceHandler.findBookingById(bookingId));

        assertEquals(exception, thrown);
        verify(repository, times(1)).findById(bookingId);
        verify(mapper, never()).toDomain(any(BookingEntity.class));
    }

    @Test
    public void testCreateBookingSuccess() {
        when(mapper.toEntity(createRequest)).thenReturn(bookingEntity);
        when(repository.save(bookingEntity)).thenReturn(bookingEntity);
        when(mapper.toDomain(bookingEntity)).thenReturn(bookingDomain);

        BookingDomain result = bookingServiceHandler.createBooking(createRequest);

        assertEquals(bookingDomain, result);
        verify(mapper, times(1)).toEntity(createRequest);
        verify(repository, times(1)).save(bookingEntity);
        verify(mapper, times(1)).toDomain(bookingEntity);
    }

    @Test
    public void testCreateBookingException() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(mapper.toEntity(createRequest)).thenReturn(bookingEntity);
        when(repository.save(bookingEntity)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookingServiceHandler.createBooking(createRequest));

        assertEquals(exception, thrown);
        verify(mapper).toEntity(createRequest);
        verify(repository).save(bookingEntity);
        verify(mapper, never()).toDomain(any(BookingEntity.class));
    }

    @Test
    public void testFindBookingsByClientSuccess() {
        Long clientId = 1L;
        when(repository.findByClient(clientId)).thenReturn(bookingEntities);
        when(mapper.toDomain(bookingEntities)).thenReturn(bookingDomains);

        List<BookingDomain> result = bookingServiceHandler.findBookingsByClient(clientId);

        assertEquals(bookingDomains, result);
        verify(repository, times(1)).findByClient(clientId);
        verify(mapper, times(1)).toDomain(bookingEntities);
    }

    @Test
    public void testFindBookingsByClientException() {
        Long clientId = 1L;
        RuntimeException exception = new RuntimeException("Test exception");
        when(repository.findByClient(clientId)).thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookingServiceHandler.findBookingsByClient(clientId));

        assertEquals(exception, thrown);
        verify(repository, times(1)).findByClient(clientId);
        verify(mapper, never()).toDomain(anyList());
    }

    @Test
    public void testDeleteBookingByIdSuccess() {
        Long bookingId = 1L;

        doNothing().when(repository).deleteById(bookingId);

        assertDoesNotThrow(() -> bookingServiceHandler.deleteBookingById(bookingId));

        verify(repository, times(1)).deleteById(bookingId);
    }

    @Test
    public void testDeleteBookingByIdException() {
        Long bookingId = 1L;
        RuntimeException exception = new RuntimeException("Test exception");

        doThrow(exception).when(repository).deleteById(bookingId);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookingServiceHandler.deleteBookingById(bookingId));

        assertEquals(exception, thrown);
        verify(repository, times(1)).deleteById(bookingId);
    }
}