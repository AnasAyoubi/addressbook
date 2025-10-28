package ca.sysc4806;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("addressbookPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        AddressBook book = new AddressBook();
        book.setLabel("My Contacts");

        book.addBuddy(new BuddyInfo("Anas",   "613-1111"));
        book.addBuddy(new BuddyInfo("Ayoubi", "613-2222"));

        em.persist(book);
        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Done. SQLite DB file created at project root (addressbook.db).");
    }
}
