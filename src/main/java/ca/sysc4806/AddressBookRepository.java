// src/main/java/ca/sysc4806/AddressBookRepository.java
package ca.sysc4806;

import org.springframework.data.repository.CrudRepository;

public interface AddressBookRepository extends CrudRepository<AddressBook, Long> {}
