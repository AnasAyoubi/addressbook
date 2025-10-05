package ca.sysc4806;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address_book")
public class AddressBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label; // e.g., "My Contacts"

    // Unidirectional OneToMany to BuddyInfo.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_book_id")
    private List<BuddyInfo> buddies = new ArrayList<>();

    public AddressBook() {}

    public AddressBook(String label) {
        this.label = label;
    }

    // Add buddy
    public void addBuddy(BuddyInfo buddy) {
        buddies.add(buddy);
    }

    // Remove buddy by ID (fix for your controller)
    public void removeBuddy(Long buddyId) {
        buddies.removeIf(b -> b.getId().equals(buddyId));
    }

    // Getters/setters
    public Long getId() { return id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public List<BuddyInfo> getBuddies() { return buddies; }
}
