package ca.sysc4806;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class AddressBook {

    @Id @GeneratedValue
    private Long id;

    private String label;

    @JsonManagedReference
    @OneToMany(mappedBy = "addressBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuddyInfo> buddies = new ArrayList<>();

    public AddressBook() {}
    public AddressBook(String label) { this.label = label; }

    // ---- helpers to maintain both sides of the relation ----
    public void addBuddy(BuddyInfo b) {
        if (b == null) return;
        buddies.add(b);
        b.setAddressBook(this);
    }
    public void removeBuddy(BuddyInfo b) {
        if (b == null) return;
        buddies.remove(b);
        b.setAddressBook(null);
    }

    // ---- getters/setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public List<BuddyInfo> getBuddies() { return buddies; }
    public void setBuddies(List<BuddyInfo> buddies) { this.buddies = buddies; }

    // equals/hashCode on id
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressBook)) return false;
        AddressBook that = (AddressBook) o;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
