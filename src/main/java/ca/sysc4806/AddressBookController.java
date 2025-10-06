// this is the spring Rest controller, it listens for
// HTTP requests at specific URLs and executes Java code in response.
// this will be the backend logic

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

    @GetMapping("/")
    public String home(){
        return "App is running on Azure";
    }

    // GET /addressbooks → returns all address books
    @GetMapping
    public Iterable<AddressBook> getAllAddressBooks() {
        return bookRepo.findAll();
    }

    // POST /addressbooks → create a new address book
    @PostMapping
    public AddressBook createBook(@RequestBody AddressBook book) {
        return bookRepo.save(book);
    }

    // GET /addressbooks/{id} → return one address book
    @GetMapping("/{id}")
    public AddressBook getBook(@PathVariable Long id) {
        return bookRepo.findById(id).orElseThrow();
    }

    // POST /addressbooks/{id}/buddies → add buddy to address book
    @PostMapping("/{id}/buddies")
    public AddressBook addBuddy(@PathVariable Long id, @RequestBody BuddyInfo buddy) {
        AddressBook book = bookRepo.findById(id).orElseThrow();
        book.addBuddy(buddyRepo.save(buddy));
        return bookRepo.save(book);
    }

    // DELETE /addressbooks/{id}/buddies/{buddyId} → remove buddy
    @DeleteMapping("/{id}/buddies/{buddyId}")
    public AddressBook removeBuddy(@PathVariable Long id, @PathVariable Long buddyId) {
        AddressBook book = bookRepo.findById(id).orElseThrow();
        book.removeBuddy(buddyId);
        return bookRepo.save(book);
    }
}
