package ca.sysc4806;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookApiIT {

    @LocalServerPort
    int port;

    @Autowired TestRestTemplate rest;

    @Autowired AddressBookRepository bookRepo;
    @Autowired BuddyInfoRepository buddyRepo;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void setupData() {
        // keep tests independent: start from a clean DB
        buddyRepo.deleteAll();
        bookRepo.deleteAll();

        BuddyInfo anas   = new BuddyInfo("Anas",   "613-2222");
        BuddyInfo ayoubi = new BuddyInfo("Ayoubi", "613-1111");
        BuddyInfo alex   = new BuddyInfo("Alex",   "613-3333");

        AddressBook ab = new AddressBook("My Contacts");
        ab.addBuddy(anas);
        ab.addBuddy(ayoubi);
        ab.addBuddy(alex);
        bookRepo.save(ab);
    }

    @Test
    void getAllAddressBooks_returnsSeededBook() {
        ResponseEntity<AddressBook[]> resp =
                rest.getForEntity(url("/addressbooks"), AddressBook[].class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        AddressBook[] books = resp.getBody();
        assertThat(books).isNotNull();
        assertThat(books).hasSize(1);
        assertThat(books[0].getLabel()).isEqualTo("My Contacts");
        // buddies get serialized too
        assertThat(books[0].getBuddies()).extracting(BuddyInfo::getName)
                .containsExactlyInAnyOrder("Anas", "Ayoubi", "Alex");
    }

    @Test
    void getOneAddressBook_returnsExactBuddies() {
        Long id = bookRepo.findAll().iterator().next().getId();

        ResponseEntity<AddressBook> resp =
                rest.getForEntity(url("/addressbooks/" + id), AddressBook.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        AddressBook book = resp.getBody();
        assertThat(book).isNotNull();
        assertThat(book.getLabel()).isEqualTo("My Contacts");
        assertThat(book.getBuddies()).extracting(BuddyInfo::getPhoneNumber)
                .containsExactlyInAnyOrder("613-2222", "613-1111", "613-3333");
    }

    @Test
    void createBook_and_addBuddy_viaHttp() {
        // create book
        AddressBook newBook = new AddressBook("Friends");
        ResponseEntity<AddressBook> created =
                rest.postForEntity(url("/addressbooks"), newBook, AddressBook.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long newId = created.getBody().getId();

        // add buddy
        BuddyInfo sam = new BuddyInfo("Sam", "555-0000");
        ResponseEntity<AddressBook> afterAdd =
                rest.postForEntity(url("/addressbooks/" + newId + "/buddies"), sam, AddressBook.class);

        assertThat(afterAdd.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<BuddyInfo> buddies = afterAdd.getBody().getBuddies();
        assertThat(buddies).extracting(BuddyInfo::getName).contains("Sam");
    }
}
