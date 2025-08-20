package ca.casapp.springcloud.msvc.clients.web.controller;

import ca.casapp.springcloud.msvc.clients.core.api.ClientService;
import ca.casapp.springcloud.msvc.clients.core.api.domain.ClientDomain;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;

import static java.util.Map.entry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@TestPropertySource(properties = {
        "client.message=Hello from client",
        "MY_POD_NAME=client-1",
        "MY_POD_IP=127.0.0.1"
        , "spring.profiles.active=dev"
        , "spring.profiles.default=prod"
})
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ClientService clientService;

    @Test
    void findAllClients() throws Exception {
        ClientDomain client1 = new ClientDomain(1L, "john.doe@example.com", "450-5028511", "John", "Doe", null, null);
        ClientDomain client2 = new ClientDomain(2L, "jane.doe@example.com", "450-5028511", "Jane", "Doe", null, null);

        var clients = Arrays.asList(client1, client2);
        when(clientService.findAllClients()).thenReturn(clients);

        Map<String, Object> map = Map.ofEntries(
                entry("clients", clients),
                entry("pod_name", "client-1"),
                entry("pod_ip", "127.0.0.1"),
                entry("active-profiles", "dev"),
                entry("default-profiles", "prod"),
                entry("message", "Hello from client")
        );

        mockMvc.perform(get("/client").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients", hasSize(2)))
                .andExpect(jsonPath("$.clients[0].id", is(1)))
                .andExpect(jsonPath("$.clients[0].firstName", is("John")))
                .andExpect(jsonPath("$.pod_name", is("client-1")))
                .andExpect(jsonPath("$.pod_ip", is("127.0.0.1")))
                .andExpect(jsonPath("$.message", is("Hello from client")));

        verify(clientService, times(1)).findAllClients();
    }

    @Test
    void findClientById() {
    }

    @Test
    void createClient() {
    }

    @Test
    void deleteClientById() {
    }
}