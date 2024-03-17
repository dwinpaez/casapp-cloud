package ca.casapp.springcloud.msvc.clients.core.aplication.repository.persistence.repository;

import ca.casapp.springcloud.msvc.clients.core.aplication.repository.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * JPA repositories for {@link ClientEntity}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Repository
public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {

    @Modifying
    @Query("UPDATE ClientEntity c " +
            "SET c.firstName = :firstName, c.lastName = :lastName " +
            "WHERE c.id = :id")
    void updateClientName(@Param(value = "id") long id,
                          @Param(value = "firstName") String firstName,
                          @Param(value = "lastName") String lastName);
}
