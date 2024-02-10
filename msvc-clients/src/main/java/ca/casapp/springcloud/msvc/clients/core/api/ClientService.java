package ca.casapp.springcloud.msvc.clients.core.api;

import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;

import java.util.Optional;

/**
 * Service for {@link ClientDomain}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 */
public interface ClientService {

    Optional<ClientDomain> findClientById(Integer id);

    ClientDomain createClient(ClientDomain.CreateRequest createRequest);

    ClientDomain updateClient(ClientDomain.UpdateRequest updateRequest);

    void deleteClient(ClientDomain.DeleteRequest deleteRequest);
}
