package ca.casapp.springcloud.msvc.clients.core.aplication.repository.persistence.repository;

import ca.casapp.springcloud.msvc.clients.core.aplication.repository.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repositories for {@link ClientEntity}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Repository
public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {
}
