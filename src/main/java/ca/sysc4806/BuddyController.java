package ca.sysc4806;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buddies")
public class BuddyController {

    private final BuddyInfoRepository buddyRepo;

    public BuddyController(BuddyInfoRepository buddyRepo) {
        this.buddyRepo = buddyRepo;
    }

    // GET /buddies → returns all buddies as JSON
    @GetMapping
    public Iterable<BuddyInfo> getAllBuddies() {
        return buddyRepo.findAll();
    }
}
