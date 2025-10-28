package ca.sysc4806;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addressbooks")
public class AddressBookController {

    private final AddressBookRepository bookRepo;
    private final BuddyInfoRepository buddyRepo;

    public AddressBookController(AddressBookRepository bookRepo, BuddyInfoRepository buddyRepo) {
        this.bookRepo = bookRepo;
        this.buddyRepo = buddyRepo;
    }

    // ---- CRUD for AddressBook ----

    @GetMapping
    public Iterable<AddressBook> allBooks() {
        return bookRepo.findAll();
    }

    @GetMapping("/{id}")
    public AddressBook oneBook(@PathVariable Long id) {
        return bookRepo.findById(id).orElseThrow();
    }

    @PostMapping
    public AddressBook createBook(@RequestBody AddressBook ab) {
        return bookRepo.save(ab);
    }

    // ---- Manage buddies within a book ----

    // POST /addressbooks/{id}/buddies  -> add buddy to this book
    @PostMapping("/{id}/buddies")
    public AddressBook addBuddy(@PathVariable Long id, @RequestBody BuddyInfo buddy) {
        AddressBook book = bookRepo.findById(id).orElseThrow();

        // keep both sides of relation in sync
        book.addBuddy(buddy);           // sets buddy.addressBook = book
        // save via the aggregate; cascade will persist BuddyInfo
        bookRepo.save(book);

        // return the updated book (now includes the new buddy with generated id)
        return book;
    }

    // DELETE /addressbooks/{id}/buddies/{buddyId} -> remove a buddy from this book
    @DeleteMapping("/{id}/buddies/{buddyId}")
    public AddressBook removeBuddy(@PathVariable Long id, @PathVariable Long buddyId) {
        AddressBook book = bookRepo.findById(id).orElseThrow();

        // fetch the entity (BuddyInfo), not just the id
        BuddyInfo buddy = buddyRepo.findById(buddyId).orElseThrow();

        // detach both sides and delete the buddy row
        book.removeBuddy(buddy);        // removes from list + sets buddy.addressBook = null
        buddyRepo.deleteById(buddyId);  // ensure it's gone

        // save the book after mutation
        return bookRepo.save(book);
    }
}
