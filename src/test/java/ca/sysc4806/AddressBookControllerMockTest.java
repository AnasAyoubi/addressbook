package ca.sysc4806;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AddressBookController.class)
class AddressBookControllerMockTest {

    @Autowired MockMvc mvc;

    @MockBean AddressBookRepository bookRepo;
    @MockBean BuddyInfoRepository buddyRepo;

    @Test
    void getAll_returnsJsonWithThreeBuddies() throws Exception {
        AddressBook ab = new AddressBook("My Contacts");
        ab.setId(1L);
        ab.addBuddy(new BuddyInfo("Anas", "613-2222"));
        ab.addBuddy(new BuddyInfo("Ayoubi", "613-1111"));
        ab.addBuddy(new BuddyInfo("Alex", "613-3333"));

        Mockito.when(bookRepo.findAll()).thenReturn(List.of(ab));

        mvc.perform(get("/addressbooks").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("My Contacts"))
                .andExpect(jsonPath("$[0].buddies[*].name",
                        containsInAnyOrder("Anas", "Ayoubi", "Alex")));
    }

    @Test
    void getOne_returnsExpectedBook() throws Exception {
        AddressBook ab = new AddressBook("My Contacts");
        ab.setId(99L);
        ab.addBuddy(new BuddyInfo("Anas", "613-2222"));
        ab.addBuddy(new BuddyInfo("Ayoubi", "613-1111"));
        ab.addBuddy(new BuddyInfo("Alex", "613-3333"));

        Mockito.when(bookRepo.findById(99L)).thenReturn(Optional.of(ab));

        mvc.perform(get("/addressbooks/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("My Contacts"))
                .andExpect(jsonPath("$.buddies", hasSize(3)));
    }
}
