package ca.casapp.springcloud.msvc.bookings.core.application.persistence;

import ca.casapp.springcloud.msvc.bookings.core.api.repository.BookingRepository;
import ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity.BookingEntity;
import ca.casapp.springcloud.msvc.bookings.core.application.persistence.repository.BookingEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handler for {@link BookingRepository}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Repository
public class BookingRepositoryHandler implements BookingRepository {

    private final BookingEntityRepository repository;

    public BookingRepositoryHandler(BookingEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookingEntity save(BookingEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public Optional<BookingEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<BookingEntity> findAll(Map<String, String> params) {
        return repository.findAll();
    }

    @Override
    public List<BookingEntity> findByClient(Long clientId) {
        return repository.findByClientId(clientId);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
