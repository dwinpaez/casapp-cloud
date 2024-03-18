package ca.casapp.springcloud.msvc.clients.core.api;

import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link ClientDomain}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
public interface ClientService {

    List<ClientDomain> findAllClients();

    Optional<ClientDomain> findClientById(Long id);

    ClientDomain createClient(ClientDomain.CreateRequest createRequest);

    ClientDomain updateClientName(ClientDomain.UpdateRequestName updateRequest);

    void deleteClient(Long id);
}
