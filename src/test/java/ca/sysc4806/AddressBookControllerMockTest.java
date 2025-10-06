
package ca.sysc4806;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressBookController.class)
class AddressBookControllerMockTest {

    @Autowired
    private MockMvc mvc;

    @MockBean private AddressBookRepository bookRepo;
    @MockBean private BuddyInfoRepository buddyRepo;

    @Test
    void getAllAddressBooks_returnsMyContactsWithThreeBuddies() throws Exception {
        AddressBook ab = new AddressBook("My Contacts");
        ab.addBuddy(new BuddyInfo("Anas", "613-2222"));
        ab.addBuddy(new BuddyInfo("Ayoubi", "613-1111"));
        ab.addBuddy(new BuddyInfo("Alex", "613-3333"));

        Mockito.when(bookRepo.findAll()).thenReturn(List.of(ab));

        mvc.perform(get("/addressbooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("My Contacts"))
                .andExpect(jsonPath("$[0].buddies[*].name",
                        containsInAnyOrder("Anas", "Ayoubi", "Alex")))
                .andExpect(jsonPath("$[0].buddies[*].phoneNumber",
                        containsInAnyOrder("613-2222", "613-1111", "613-3333")));
    }

    @Test
    void getBookById_returnsMyContacts() throws Exception {
        AddressBook ab = new AddressBook("My Contacts");
        ab.addBuddy(new BuddyInfo("Anas", "613-2222"));
        ab.addBuddy(new BuddyInfo("Ayoubi", "613-1111"));
        ab.addBuddy(new BuddyInfo("Alex", "613-3333"));

        Mockito.when(bookRepo.findById(1L)).thenReturn(Optional.of(ab));

        mvc.perform(get("/addressbooks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("My Contacts"))
                .andExpect(jsonPath("$.buddies[*].name",
                        containsInAnyOrder("Anas", "Ayoubi", "Alex")))
                .andExpect(jsonPath("$.buddies[*].phoneNumber",
                        containsInAnyOrder("613-2222", "613-1111", "613-3333")));
    }
}
