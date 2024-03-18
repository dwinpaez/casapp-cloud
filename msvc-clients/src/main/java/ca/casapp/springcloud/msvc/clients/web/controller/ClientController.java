package ca.casapp.springcloud.msvc.clients.web.controller;

import ca.casapp.springcloud.msvc.clients.core.api.ClientService;
import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public ResponseEntity<ClientDomain> createClient(@Valid @RequestBody ClientDomain.CreateRequest createRequest) {
        log.debug("method: createClient({})", createRequest);
        final var client = clientService.createClient(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<ClientDomain> deleteClientById(@PathVariable("clientId") Long clientId) {
        log.debug("method: deleteClientById({})", clientId);
        final var optional = clientService.findClientById(clientId);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        clientService.deleteClient(optional.get().id());
        return ResponseEntity.noContent().build();
    }
}
