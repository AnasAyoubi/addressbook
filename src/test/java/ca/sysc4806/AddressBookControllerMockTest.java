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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressBookController.class)
class AddressBookControllerMockTest {

    @Autowired MockMvc mockMvc;

    @MockBean AddressBookRepository bookRepo;
    @MockBean BuddyInfoRepository buddyRepo;

    @Test
    void getAllAddressBooks_returnsSeedLikeJson() throws Exception {
        // Arrange a fake book with your real names
        AddressBook book = new AddressBook("My Contacts");

        book.addBuddy(new BuddyInfo("Anas",  "613-2222"));
        book.addBuddy(new BuddyInfo("Ayoubi","613-1111"));
        book.addBuddy(new BuddyInfo("Alex",  "613-3333"));
        Mockito.when(bookRepo.findAll()).thenReturn(List.of(book));

        // Act + Assert
        mockMvc.perform(get("/addressbooks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].label").value("My Contacts"))
                .andExpect(jsonPath("$[0].buddies", hasSize(3)))
                .andExpect(jsonPath("$[0].buddies[*].name",
                        containsInAnyOrder("Anas","Ayoubi","Alex")));
    }

    @Test
    void getById_returnsOneBook() throws Exception {
        AddressBook book = new AddressBook("My Contacts");
        Mockito.when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/addressbooks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("My Contacts"));
    }

    @Test
    void createBook_persistsAndReturnsBook() throws Exception {
        AddressBook saved = new AddressBook("Friends");
        Mockito.when(bookRepo.save(Mockito.any(AddressBook.class))).thenReturn(saved);

        mockMvc.perform(post("/addressbooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"label\":\"Friends\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.label").value("Friends"));
    }
}
