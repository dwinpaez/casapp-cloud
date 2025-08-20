package ca.casapp.springcloud.msvc.clients;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MsvcClientsApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void testDiscoveryClientBean() {
        assertThat(context.getBean(DiscoveryClient.class)).isNotNull();
    }

    @Test
    void testFeignClientBean() {
        assertThat(context.containsBean("feignContext")).isTrue();
    }

}
