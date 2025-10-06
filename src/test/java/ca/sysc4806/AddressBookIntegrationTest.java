// src/test/java/ca/sysc4806/AddressBookIntegrationTest.java
package ca.sysc4806;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookIntegrationTest {

    @LocalServerPort int port;
    TestRestTemplate rest = new TestRestTemplate();

    @Test
    void getAllAddressBooks_returnsSeededBook() {
        String url = "http://localhost:" + port + "/addressbooks";
        String body = rest.getForObject(url, String.class);
        assertThat(body).contains("My Contacts","Anas","Ayoubi","Alex");
    }
}
