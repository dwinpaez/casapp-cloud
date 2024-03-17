package ca.casapp.springcloud.msvc.clients.core.api.repository;

import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import ca.casapp.springcloud.msvc.clients.core.aplication.repository.persistence.entity.ClientEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository for {@link ClientDomain}.
 *
 * @author <a href="mailto:dpoveda@gmail.com">Diego Poveda</a>
 * @version 3.0-SNAPSHOT 2021/04/01
 * @since 1.8
 */
public interface ClientRepository {

    ClientEntity save(ClientEntity entity);

    ClientEntity updateNames(Long id, String firstName, String lastName);

    Optional<ClientEntity> findById(Long id);

    List<ClientEntity> findAll(Map<String, String> params);

    void deleteById(Long id);
}
