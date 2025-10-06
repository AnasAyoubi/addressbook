// src/test/java/ca/sysc4806/AddressBookControllerMockTest.java
package ca.sysc4806;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressBookController.class)
class AddressBookControllerMockTest {

    @Autowired MockMvc mockMvc;

    @MockBean AddressBookRepository bookRepo;
    @MockBean BuddyInfoRepository buddyRepo;

    @Test
    void getBook_returnsBookJson() throws Exception {
        AddressBook ab = new AddressBook("My Contacts");
        // No ab.setId(...) here
        when(bookRepo.findById(1L)).thenReturn(Optional.of(ab));

        mockMvc.perform(get("/addressbooks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("My Contacts"));
    }
}
