package ca.sysc4806;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(AddressBookRepository bookRepo) {
        return args -> {
            BuddyInfo anas = new BuddyInfo("Anas", "613-2222");
            BuddyInfo ayoubi = new BuddyInfo("Ayoubi", "613-1111");
            BuddyInfo alex = new BuddyInfo("Alex", "613-3333");

            AddressBook ab = new AddressBook("My Contacts");
            ab.addBuddy(anas);
            ab.addBuddy(ayoubi);
            ab.addBuddy(alex);

            // Persist only the address book
            bookRepo.save(ab);

            System.out.println("Saved AddressBook with " + ab.getBuddies().size() + " buddies!");
        };
    }
}
