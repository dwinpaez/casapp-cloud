package ca.casapp.springcloud.msvc.clients.core.aplication.persistence;

import ca.casapp.springcloud.msvc.clients.core.api.repository.ClientRepository;
import ca.casapp.springcloud.msvc.clients.core.aplication.persistence.entity.ClientEntity;
import ca.casapp.springcloud.msvc.clients.core.aplication.persistence.repository.ClientEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handler for {@link ClientRepository}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Repository
public class ClientRepositoryHandler implements ClientRepository {

    private final ClientEntityRepository repository;

    public ClientRepositoryHandler(ClientEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientEntity save(ClientEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public ClientEntity updateNames(Long id, String firstName, String lastName) {
        repository.updateClientName(id, firstName, lastName);
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Optional<ClientEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ClientEntity> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<ClientEntity> findAll(Map<String, String> params) {
        return repository.findAll();
    }


    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
