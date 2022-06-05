package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.domain.AccountBook;
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
 * Class that contains integration tests for the {@link AccountBookRepository} repository.
 * <p>
 * Tests in this class check if an {@link AccountBookRepository} functions properly
 *   and manages data in a dedicated database.
 * <p>
 * All tests in this class are transactional.
 */
@DisplayName("AccountBookRepository integration tests")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountBookRepositoryIntegrationTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link AccountBookRepository} instance - the test subject.
     */
    @Autowired
    AccountBookRepository accountBookRepository;

    /**
     * {@link AccountRepository} instance required to set up data for tests.
     */
    @Autowired
    AccountRepository accountRepository;

    /**
     * {@link BookRepository} instance required to set up data for tests.
     */
    @Autowired
    BookRepository bookRepository;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link AccountBookRepository} successfully
     *   creates new account-book entries.
     */
    @Test
    @DisplayName("Creates a new account-book entry")
    public void testCreatesAccountBookEntry() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        Book newBook = new Book();
        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        AccountBook newAccountBook = new AccountBook(newAccount, newBook, 1);

        // Pre-populating database with data.
        accountRepository.save(newAccount);
        bookRepository.save(newBook);

        // Creating an entry.
        AccountBook savedEntry = accountBookRepository.save(newAccountBook);

        // Check for persistence.
        Assertions.assertAll(
                () -> Assertions.assertEquals(newAccount, savedEntry.getAccount()),
                () -> Assertions.assertEquals(newBook, savedEntry.getBook()),
                () -> Assertions.assertEquals(newAccountBook.getAmount(), savedEntry.getAmount())
        );
    }

    /**
     * Tests if {@link AccountBookRepository} successfully
     *   saves changes in an account-book entry.
     */
    @Test
    @DisplayName("Saves changes in an entry")
    public void savesChangesInBookEntry() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        Book newBook = new Book();
        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        AccountBook newAccountBook = new AccountBook(newAccount, newBook, 1);

        // Pre-populating database with data.
        long accountId = accountRepository.save(newAccount).getId();
        long bookId = bookRepository.save(newBook).getId();

        // Saving new entry.
        accountBookRepository.save(newAccountBook);

        // Changing the entry.
        newAccountBook.setAmount(10);

        // Saving changes.
        accountBookRepository.save(newAccountBook);

        Optional<AccountBook> savedEntryOptional = accountBookRepository
                .findAccountBookByAccountIdAndBookId(accountId, bookId);

        // Check for persistence.
        Assertions.assertTrue(savedEntryOptional.isPresent());
        Assertions.assertEquals(10, savedEntryOptional.get().getAmount());
    }

    /**
     * Tests if {@link AccountBookRepository} successfully
     *   finds an account-book entry by its account ID and book ID.
     */
    @Test
    @DisplayName("Finds an existing entry by account ID and book ID")
    public void testFindsExistingEntry() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        Book newBook = new Book();
        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        AccountBook newAccountBook = new AccountBook(newAccount, newBook, 1);

        // Pre-populating database with data.
        long accountId = accountRepository.save(newAccount).getId();
        long bookId = bookRepository.save(newBook).getId();

        accountBookRepository.save(newAccountBook);

        Optional<AccountBook> foundEntryOptional = accountBookRepository
                .findAccountBookByAccountIdAndBookId(accountId, bookId);

        // Check if the entry is the same.
        Assertions.assertTrue(foundEntryOptional.isPresent());
        Assertions.assertEquals(newAccountBook, foundEntryOptional.get());
    }

    /**
     * Tests if {@link AccountBookRepository} successfully
     *   finds all existing entries.
     */
    @Test
    @DisplayName("Finds all entries")
    public void findsAllExistingEntries() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        Book firstNewBook = new Book();
        firstNewBook.setName("Algorithms");
        firstNewBook.setAuthor("Robert Sedgewick");

        Book secondNewBook = new Book();
        secondNewBook.setName("The C++ Programming Language");
        secondNewBook.setAuthor("Bjarne Stroustrup");

        AccountBook firstNewAccountBook = new AccountBook(newAccount, firstNewBook, 1);
        AccountBook secondNewAccountBook = new AccountBook(newAccount, secondNewBook, 3);

        // Pre-populating database with data.
        accountRepository.save(newAccount);
        bookRepository.save(firstNewBook);
        bookRepository.save(secondNewBook);

        // Saving entries.
        AccountBook firstSavedEntry = accountBookRepository.save(firstNewAccountBook);
        AccountBook secondSavedEntry = accountBookRepository.save(secondNewAccountBook);

        List<AccountBook> savedEntries = accountBookRepository.findAll();

        // Check if found entries contain all the previously created ones.
        assertThat(savedEntries, hasItems(firstSavedEntry, secondSavedEntry));
    }

    /**
     * Tests if {@link AccountBookRepository} successfully
     *   deletes all existing entries.
     */
    @Test
    @DisplayName("Deletes all entries")
    public void testDeletesAllEntries() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        Book firstNewBook = new Book();
        firstNewBook.setName("Algorithms");
        firstNewBook.setAuthor("Robert Sedgewick");

        Book secondNewBook = new Book();
        secondNewBook.setName("The C++ Programming Language");
        secondNewBook.setAuthor("Bjarne Stroustrup");

        AccountBook firstNewAccountBook = new AccountBook(newAccount, firstNewBook, 1);
        AccountBook secondNewAccountBook = new AccountBook(newAccount, secondNewBook, 3);

        // Pre-populating database with data.
        accountRepository.save(newAccount);
        bookRepository.save(firstNewBook);
        bookRepository.save(secondNewBook);

        // Saving entries.
        accountBookRepository.save(firstNewAccountBook);
        accountBookRepository.save(secondNewAccountBook);

        // Deleting all entries.
        accountBookRepository.deleteAll();

        List<AccountBook> savedEntries = accountBookRepository.findAll();

        // Check if no entries were found.
        Assertions.assertEquals(0, savedEntries.size());
    }
}
