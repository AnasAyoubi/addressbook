package ca.sysc4806;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AddressBookViewController {
    private final AddressBookRepository bookRepo;

    public AddressBookViewController(AddressBookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("addressBook", bookRepo.findById(id).orElse(null));
        return "addressbook"; // renders src/main/resources/templates/addressbook.html
    }
}
