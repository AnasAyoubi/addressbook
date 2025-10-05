package ca.sysc4806;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AddressBookTest {

    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("addressbookPU");
    }

    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        if (em.isOpen()) em.close();
    }

    @AfterAll
    static void shutdown() {
        emf.close();
    }

    @Test
    void testPersistAddressBookWithBuddies() {
        AddressBook book = new AddressBook("My Contacts");
        book.addBuddy(new BuddyInfo("Anas", "613-1111"));
        book.addBuddy(new BuddyInfo("Ayoubi", "613-2222"));

        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();

        AddressBook found = em.find(AddressBook.class, book.getId());
        assertNotNull(found);
        assertEquals(2, found.getBuddies().size());
    }
}
