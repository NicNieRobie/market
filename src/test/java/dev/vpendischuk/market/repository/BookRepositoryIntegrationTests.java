package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

/**
 * Class that contains integration tests for the {@link BookRepository} repository.
 * <p>
 * Tests in this class check if an {@link BookRepository} functions properly
 *   and manages data in a dedicated database.
 * <p>
 * All tests in this class are transactional.
 */
@DisplayName("BookRepository integration tests")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryIntegrationTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link BookRepository} instance - the test subject.
     */
    @Autowired
    BookRepository bookRepository;

    /**
     * {@link ProductRepository} instance required to set up data for tests.
     */
    @Autowired
    ProductRepository productRepository;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link BookRepository} successfully
     *   creates new book entries.
     */
    @Test
    @DisplayName("Creates a new book entry")
    public void testCreatesBookEntry() {
        // Creating data.
        Book newBook = new Book();
        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        // Saving entry.
        Book savedBook = bookRepository.save(newBook);

        // Checking for persistence.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedBook.getId()),
                () -> Assertions.assertEquals(newBook, savedBook)
        );
    }

    /**
     * Tests if {@link BookRepository} successfully
     *   saves changes in a book entry.
     */
    @Test
    @DisplayName("Saves changes in an entry")
    public void savesChangesInBookEntry() {
        // Creating data.
        Book newBook = new Book();
        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        // Saving entry.
        long entryId = bookRepository.save(newBook).getId();

        // Changing entry.
        newBook.setAuthor("Donald Knuth");

        // Saving changes.
        bookRepository.save(newBook);

        Optional<Book> savedEntryOptional = bookRepository.findById(entryId);

        // Checking for persistence.
        Assertions.assertTrue(savedEntryOptional.isPresent());
        Assertions.assertEquals("Donald Knuth", savedEntryOptional.get().getAuthor());
    }

    /**
     * Tests if {@link BookRepository} successfully
     *   finds a book entry by its ID.
     */
    @Test
    @DisplayName("Finds an existing entry by book ID")
    public void testFindsExistentEntry() {
        // Creating data.
        Book newBook = new Book();
        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        // Saving entry.
        Book savedBook = bookRepository.save(newBook);

        Optional<Book> foundBookOptional = bookRepository.findById(savedBook.getId());

        // Checking for persistence.

        Assertions.assertTrue(foundBookOptional.isPresent());

        Book foundBook = foundBookOptional.get();

        Assertions.assertEquals(newBook, foundBook);
    }

    /**
     * Tests if {@link BookRepository} successfully
     *   finds all existing entries.
     */
    @Test
    @DisplayName("Finds all entries")
    public void findsAllExistentEntries() {
        // Creating data.
        Book firstNewBook = new Book();
        firstNewBook.setName("Algorithms");
        firstNewBook.setAuthor("Robert Sedgewick");

        Book secondNewBook = new Book();
        secondNewBook.setName("The C++ Programming Language");
        secondNewBook.setAuthor("Bjarne Stroustrup");

        // Saving entries.
        Book firstSavedBook = bookRepository.save(firstNewBook);
        Book secondSavedBook = bookRepository.save(secondNewBook);

        List<Book> savedBooks = bookRepository.findAll();

        // Checking if the found entries contain all the previously created entries.
        assertThat(savedBooks, hasItems(firstSavedBook, secondSavedBook));
    }

    /**
     * Tests if {@link BookRepository} successfully
     *   deletes all existing entries.
     */
    @Test
    @DisplayName("Deletes all entries")
    public void testDeletesAllEntries() {
        // Creating data.
        Book firstNewBook = new Book();
        firstNewBook.setName("Algorithms");
        firstNewBook.setAuthor("Robert Sedgewick");

        Book secondNewBook = new Book();
        secondNewBook.setName("The C++ Programming Language");
        secondNewBook.setAuthor("Bjarne Stroustrup");

        // Saving entries.
        bookRepository.save(firstNewBook);
        bookRepository.save(secondNewBook);

        // Deleting entries.
        productRepository.deleteAll();
        bookRepository.deleteAll();

        List<Book> savedBooks = bookRepository.findAll();

        // Checking if no entries were found.
        Assertions.assertEquals(0, savedBooks.size());
    }
}
