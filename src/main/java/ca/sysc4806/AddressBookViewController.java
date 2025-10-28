package ca.sysc4806;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui")
public class AddressBookViewController {

    private final AddressBookRepository books;
    private final BuddyInfoRepository buddies;

    public AddressBookViewController(AddressBookRepository books, BuddyInfoRepository buddies) {
        this.books = books;
        this.buddies = buddies;
    }

    // List all address books + create form
    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", books.findAll());
        model.addAttribute("newBook", new AddressBook());
        return "books"; // templates/books.html
    }

    // Create a new address book
    @PostMapping("/books")
    public String createBook(@ModelAttribute("newBook") AddressBook ab) {
        books.save(ab);
        return "redirect:/ui/books";
    }

    // View a single book and its buddies + add-buddy form
    @GetMapping("/books/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        AddressBook ab = books.findById(id).orElseThrow();
        model.addAttribute("book", ab);
        model.addAttribute("newBuddy", new BuddyInfo());
        return "book"; // templates/book.html
    }

    // Add a buddy to a book (uses helper to keep both sides in sync)
    @PostMapping("/books/{id}/buddies")
    public String addBuddy(@PathVariable Long id, @ModelAttribute("newBuddy") BuddyInfo in) {
        AddressBook ab = books.findById(id).orElseThrow();
        ab.addBuddy(in);          // sets back-reference inside helper
        books.save(ab);           // cascades to BuddyInfo if CascadeType.ALL
        return "redirect:/ui/books/" + id;
    }

    // Delete a buddy by ID (simple, avoids Long -> BuddyInfo mismatch)
    @PostMapping("/buddies/{buddyId}/delete")
    public String deleteBuddy(@PathVariable Long buddyId, @RequestParam("bookId") Long bookId) {
        buddies.deleteById(buddyId);
        return "redirect:/ui/books/" + bookId;
    }
}
