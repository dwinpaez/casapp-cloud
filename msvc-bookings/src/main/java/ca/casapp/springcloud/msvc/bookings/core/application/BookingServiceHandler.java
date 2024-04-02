package ca.casapp.springcloud.msvc.bookings.core.application;

import ca.casapp.springcloud.msvc.bookings.core.api.BookingService;
import ca.casapp.springcloud.msvc.bookings.core.api.domain.BookingDomain;
import ca.casapp.springcloud.msvc.bookings.core.api.repository.BookingRepository;
import ca.casapp.springcloud.msvc.bookings.core.application.mapper.BookingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Handler for {@link BookingService}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Slf4j
@Service
@Transactional
public class BookingServiceHandler implements BookingService {

    private final BookingRepository repository;
    private final BookingMapper mapper;

    public BookingServiceHandler(BookingRepository repository, BookingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BookingDomain> findAllBookings() {
        log.debug("method: findAllBookings()");
        try {
            return mapper.toDomain(repository.findAll(Collections.emptyMap()));
        } catch (Exception ex) {
            log.error("method: findAllClients() -> Exception: {}", ex);
            throw ex;
        }
    }

    @Override
    public Optional<BookingDomain> findBookingById(Long id) {
        log.debug("method: findBookingById({})", id);
        try {
            return repository.findById(id).map(mapper::toDomain);
        } catch (Exception ex) {
            log.error("method: findByActive({}) -> Exception: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public BookingDomain createBooking(BookingDomain.CreateRequest createRequest) {
        log.debug("method: createBooking({})", createRequest);
        try {
            return mapper.toDomain(repository.save(mapper.toEntity(createRequest)));
        } catch (Exception ex) {
            log.error("method: createBooking({}) -> Exception: {}", createRequest, ex);
            throw ex;
        }
    }

    @Override
    public List<BookingDomain> findBookingsByClient(Long clientId) {
        log.debug("method: findBookingsByClient({})", clientId);
        try {
            return mapper.toDomain(repository.findByClient(clientId));
        } catch (Exception ex) {
            log.error("method: findBookingsByClient({}) -> Exception: {}", clientId, ex);
            throw ex;
        }
    }

    @Override
    public void deleteBookingById(Long id) {
        log.debug("method: deleteBookingById({})", id);
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            log.error("method: deleteBookingById({}) -> Exception: {}", id, ex);
            throw ex;
        }
    }
}
