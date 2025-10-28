package ca.sysc4806;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressBookApi {

    private final AddressBookRepository books;
    private final BuddyInfoRepository buddies;

    public AddressBookApi(AddressBookRepository books, BuddyInfoRepository buddies) {
        this.books = books; this.buddies = buddies;
    }

    @GetMapping("/books")
    public Iterable<AddressBook> allBooks() {
        return books.findAll();
    }

    @GetMapping("/books/{id}")
    public AddressBook oneBook(@PathVariable Long id) {
        return books.findById(id).orElseThrow();
    }

    @PostMapping("/books")
    public AddressBook createBook(@RequestBody AddressBook ab) {
        // id auto-generated; no need to touch it
        return books.save(ab);
    }

    @PostMapping("/books/{id}/buddies")
    public BuddyInfo addBuddy(@PathVariable Long id, @RequestBody BuddyInfo in) {
        AddressBook ab = books.findById(id).orElseThrow();
        ab.addBuddy(in);
        books.save(ab);           // persist both sides
        return in;                // now has an id
    }

    @DeleteMapping("/buddies/{id}")
    public void deleteBuddy(@PathVariable Long id) {
        buddies.deleteById(id);
    }
}
