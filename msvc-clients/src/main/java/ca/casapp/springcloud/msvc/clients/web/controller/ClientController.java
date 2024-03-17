package ca.casapp.springcloud.msvc.clients.web.controller;

import ca.casapp.springcloud.msvc.clients.core.api.ClientService;
import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for {@link ClientDomain}.
 *
 * @author <a href="mailto:dwinpaez@gmail.com">Dwin Paez</a>
 * @version 3.0-SNAPSHOT 2021/04/01
 * @since 1.8
 */
@Slf4j
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDomain>> findAllClients() {
        log.debug("method: findAllClients()");
        return ResponseEntity.ok(clientService.findAllClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDomain> findClientById(@PathVariable("clientId") Long clientId) {
        log.debug("method: findClientById({})", clientId);
        final var optional = clientService.findClientById(clientId);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }

}
