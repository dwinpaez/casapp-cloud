package ca.casapp.springcloud.msvc.clients.core.api.repository;


import ca.casapp.springcloud.msvc.clients.core.aplication.persistence.entity.ClientEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository for {@link ClientEntity}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 * @version 3.0-SNAPSHOT 2024/04/01
 * @since 1.8
 */
public interface ClientRepository {

    ClientEntity save(ClientEntity entity);

    ClientEntity updateNames(Long id, String firstName, String lastName);

    Optional<ClientEntity> findById(Long id);

    Optional<ClientEntity> findByEmail(String email);

    List<ClientEntity> findAll(Map<String, String> params);

    void deleteById(Long id);
}
