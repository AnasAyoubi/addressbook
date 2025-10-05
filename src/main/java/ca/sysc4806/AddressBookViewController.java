package ca.sysc4806;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AddressBookViewController {

    private final AddressBookRepository repo;

    public AddressBookViewController(AddressBookRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        AddressBook book = repo.findById(id).orElseThrow();
        model.addAttribute("addressBook", book);
        return "addressbook"; // matches addressbook.html in templates
    }
}
