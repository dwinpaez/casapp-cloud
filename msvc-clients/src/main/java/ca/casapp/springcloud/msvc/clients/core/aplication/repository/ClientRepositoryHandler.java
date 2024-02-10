package ca.casapp.springcloud.msvc.clients.core.aplication.repository;

import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import ca.casapp.springcloud.msvc.clients.core.api.repository.ClientRepository;
import ca.casapp.springcloud.msvc.clients.core.aplication.repository.mapper.ClientMapper;
import ca.casapp.springcloud.msvc.clients.core.aplication.repository.persistence.repository.ClientEntityRepository;
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
    private final ClientMapper mapper;

    public ClientRepositoryHandler(ClientEntityRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ClientDomain save(ClientDomain.CreateRequest domain) {
        return mapper.toDomain(repository.saveAndFlush(mapper.toEntity(domain)));
    }

    public Optional<ClientDomain> findById(Long id) {
        return repository.findById(id).map(entity -> mapper.toDomain(entity));
    }

    public List<ClientDomain> findAll(Map<String, String> params) {
        return mapper.toDomain(repository.findAll());
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
