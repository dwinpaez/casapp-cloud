package ca.casapp.springcloud.msvc.bookings.core.api.repository;


import ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity.BookingEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository for {@link BookingEntity}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 * @version 3.0-SNAPSHOT 2024/04/01
 * @since 1.8
 */
public interface BookingRepository {

    BookingEntity save(BookingEntity entity);

    Optional<BookingEntity> findById(Long id);

    List<BookingEntity> findAll(Map<String, String> params);

    List<BookingEntity> findByClient(Long clientId);

    void deleteById(Long id);
}
