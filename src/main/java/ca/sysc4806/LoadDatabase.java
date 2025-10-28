package ca.sysc4806;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(AddressBookRepository bookRepo) {
        return args -> {
            // Create buddies (email left null)
            BuddyInfo anas = new BuddyInfo("Anas", "613-1111");
            BuddyInfo ayoubi = new BuddyInfo("Ayoubi", "613-2222");
            BuddyInfo alex = new BuddyInfo("Alex", "613-3333");

            // Create the book and attach buddies
            AddressBook ab = new AddressBook("My Contacts");   // or use default ctor + setLabel
            ab.addBuddy(anas);
            ab.addBuddy(ayoubi);
            ab.addBuddy(alex);

            // Persist the aggregate (cascade saves buddies)
            bookRepo.save(ab);

            System.out.println("Saved AddressBook with " + ab.getBuddies().size() + " buddies!");
        };
    }
}
