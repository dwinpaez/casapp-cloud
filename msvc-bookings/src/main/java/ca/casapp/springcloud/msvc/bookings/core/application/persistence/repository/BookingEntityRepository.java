package ca.casapp.springcloud.msvc.bookings.core.application.persistence.repository;

import ca.casapp.springcloud.msvc.bookings.core.application.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repositories for {@link BookingEntity}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Repository
public interface BookingEntityRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findByClientId(Long clientId);
}
