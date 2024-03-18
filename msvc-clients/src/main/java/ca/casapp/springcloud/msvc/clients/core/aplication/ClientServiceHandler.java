package ca.casapp.springcloud.msvc.clients.core.aplication;

import ca.casapp.springcloud.msvc.clients.core.api.ClientService;
import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import ca.casapp.springcloud.msvc.clients.core.api.repository.ClientRepository;
import ca.casapp.springcloud.msvc.clients.core.aplication.repository.mapper.ClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Handler for {@link ClientService}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
@Slf4j
@Service
@Transactional
public class ClientServiceHandler implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    public ClientServiceHandler(ClientRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ClientDomain> findAllClients() {
        log.debug("method: findAllClients()");
        try {
            return mapper.toDomain(repository.findAll(Collections.emptyMap()));
        } catch (Exception ex) {
            log.error("method: findAllClients() -> Exception: {}", ex);
            throw ex;
        }
    }

    @Override
    public Optional<ClientDomain> findClientById(Long id) {
        log.debug("method: findClientById({})", id);
        try {
            return repository.findById(id).map(mapper::toDomain);
        } catch (Exception ex) {
            log.error("method: findByActive({}) -> Exception: {}", id, ex);
            throw ex;
        }
    }

    @Override
    public ClientDomain createClient(ClientDomain.CreateRequest createRequest) {
        log.debug("method: createClient({})", createRequest);
        try {
            if (repository.findByEmail(createRequest.getEmail()).isPresent()) {

            }
            return mapper.toDomain(repository.save(mapper.toEntity(createRequest)));
        } catch (Exception ex) {
            log.error("method: createClient({}) -> Exception: {}", createRequest, ex);
            throw ex;
        }
    }

    @Override
    public ClientDomain updateClientName(ClientDomain.UpdateRequestName updateRequest) {
        log.debug("method: updateClientName({})", updateRequest);
        try {
            return mapper.toDomain(repository.updateNames(updateRequest.getId(), updateRequest.getFirstName(), updateRequest.getLastName()));
        } catch (Exception ex) {
            log.error("method: updateClientName({}) -> Exception: {}", updateRequest, ex);
            throw ex;
        }
    }

    @Override
    public void deleteClient(Long id) {
        log.debug("method: deleteClient({})", id);
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            log.error("method: deleteClient({}) -> Exception: {}", id, ex);
            throw ex;
        }
    }
}
