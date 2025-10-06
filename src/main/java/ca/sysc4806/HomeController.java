package ca.sysc4806;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // Root of the site
    @GetMapping("/")
    public String home() {
        return "App is running on Azure";
        // or: return "redirect:/addressbooks";  // if you want to redirect to your JSON
        // or: return "redirect:/view/1";        // if you want to show the Thymeleaf page
    }
}
