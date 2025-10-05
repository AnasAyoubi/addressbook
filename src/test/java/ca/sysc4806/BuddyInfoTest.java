package ca.sysc4806;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class BuddyInfoTest {

    @Autowired
    private BuddyInfoRepository buddyRepo;

    @Test
    void testPersistAndQueryBuddyInfo() {
        BuddyInfo buddy = new BuddyInfo("Anas", "613-1111");
        buddyRepo.save(buddy);

        BuddyInfo found = buddyRepo.findById(buddy.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Anas");
        assertThat(found.getPhoneNumber()).isEqualTo("613-1111");
    }
}
