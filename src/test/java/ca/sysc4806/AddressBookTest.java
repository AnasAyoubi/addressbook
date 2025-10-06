package ca.sysc4806;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AddressBookTest {

    @Autowired
    private AddressBookRepository bookRepo;

    @Test
    void testAddBuddy() {
        AddressBook book = new AddressBook("Friends");
        BuddyInfo buddy = new BuddyInfo("John", "613-5555");
        book.addBuddy(buddy);
        bookRepo.save(book);

        AddressBook found = bookRepo.findById(book.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getBuddies().size()).isEqualTo(1);
    }
}
