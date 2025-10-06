// src/test/java/ca/sysc4806/AddressBookApiIT.java
package ca.sysc4806;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookApiIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void createMyContacts_addThreeBuddies_thenFetchMatches() {
        // 1) Create “My Contacts”
        AddressBook book = new AddressBook("My Contacts");
        ResponseEntity<AddressBook> created =
                rest.postForEntity(url("/addressbooks"), book, AddressBook.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);
        AddressBook saved = created.getBody();
        assertThat(saved).isNotNull();
        Long id = saved.getId();
        assertThat(id).isNotNull();

        // 2) Add your three buddies
        rest.postForEntity(url("/addressbooks/" + id + "/buddies"),
                new BuddyInfo("Anas", "613-2222"), AddressBook.class);
        rest.postForEntity(url("/addressbooks/" + id + "/buddies"),
                new BuddyInfo("Ayoubi", "613-1111"), AddressBook.class);
        rest.postForEntity(url("/addressbooks/" + id + "/buddies"),
                new BuddyInfo("Alex", "613-3333"), AddressBook.class);

        // 3) Fetch book and verify exact names/numbers
        ResponseEntity<AddressBook> fetched =
                rest.getForEntity(url("/addressbooks/" + id), AddressBook.class);

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().getLabel()).isEqualTo("My Contacts");
        assertThat(fetched.getBody().getBuddies())
                .extracting(BuddyInfo::getName)
                .containsExactlyInAnyOrder("Anas", "Ayoubi", "Alex");
        assertThat(fetched.getBody().getBuddies())
                .extracting(BuddyInfo::getPhoneNumber)
                .containsExactlyInAnyOrder("613-2222", "613-1111", "613-3333");

        // 4) (Optional) Verify the HTML page shows them too
        ResponseEntity<String> page = rest.getForEntity(url("/view/" + id), String.class);
        assertThat(page.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(page.getBody()).contains(
                "Address Book",
                "Anas - 613-2222",
                "Ayoubi - 613-1111",
                "Alex - 613-3333"
        );
    }
}
