package dev.vpendischuk.market.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import dev.vpendischuk.market.controller.AccountController;
import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.domain.AccountBook;
import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.repository.AccountBookRepository;
import dev.vpendischuk.market.service.impl.AccountBookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

/**
 * Class that contains unit tests for the {@link AccountBookServiceImpl} service class.
 * <p>
 * Tests in this class check if an {@link AccountBookServiceImpl} functions properly in isolation
 *   from its dependencies' functionality.
 */
@DisplayName("AccountBookServiceImpl unit tests")
@ExtendWith(MockitoExtension.class)
public class AccountBookServiceImplUnitTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link AccountBookServiceImpl} instance - the test subject.
     */
    @InjectMocks
    AccountBookServiceImpl accountBookService;

    /**
     * Mock {@link AccountBookRepository} instance.
     */
    @Mock
    AccountBookRepository accountBookRepository;

    /**
     * Mock {@link AccountService} instance.
     */
    @Mock
    AccountService accountService;

    /**
     * Mock {@link BookService} instance.
     */
    @Mock
    BookService bookService;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link AccountBookServiceImpl} successfully creates
     *   a new account-book entry for given account and book
     *   if it was previously nonexistent.
     */
    @Test
    @DisplayName("Creates a new account-book entry")
    public void testAddsNewEntryIfNonexistentAndEntitiesExist() {
        // Create mock data.
        Account mockAccount = new Account();
        Book mockBook = new Book();

        mockAccount.setId(1L);
        mockAccount.setBalance(10000);

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        AccountBook mockEntry = new AccountBook(mockAccount, mockBook, 0);

        // Mocking service functionality with mock data.
        Mockito.when(accountService.findById(1L)).thenReturn(mockAccount);
        Mockito.when(bookService.findById(1L)).thenReturn(mockBook);
        Mockito.when(accountBookRepository.save(any(AccountBook.class))).thenReturn(mockEntry);

        // Requesting an entry.
        accountBookService.addOne(1L, 1L, 3);

        AccountBook entry = accountBookService.getByAccountAndBookOrCreate(mockAccount, mockBook);

        // Check if entry was created with correct parameters.
        Assertions.assertAll(
                () -> Assertions.assertEquals(mockAccount, entry.getAccount()),
                () -> Assertions.assertEquals(mockBook, entry.getBook()),
                () -> Assertions.assertEquals(3, entry.getAmount())
        );
    }

    /**
     * Tests if {@link AccountBookServiceImpl} successfully updates
     *   an account-book entry for given account and book
     *   if it already exists.
     */
    @Test
    @DisplayName("Updates existing entry")
    public void testUpdatesEntryIfExistentAndEntitiesExist() {
        // Create mock data.
        Account mockAccount = new Account();
        Book mockBook = new Book();

        mockAccount.setId(1L);
        mockAccount.setBalance(10000);

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        AccountBook mockEntry = new AccountBook(mockAccount, mockBook, 3);

        // Mocking service functionality with mock data.
        Mockito.when(accountService.findById(1L)).thenReturn(mockAccount);
        Mockito.when(bookService.findById(1L)).thenReturn(mockBook);
        Mockito.when(accountBookRepository.save(any(AccountBook.class))).thenReturn(mockEntry);

        // Requesting an entry.
        accountBookService.addOne(1L, 1L, 3);

        AccountBook entry = accountBookService.getByAccountAndBookOrCreate(mockAccount, mockBook);

        // Check if entry was updated with correct parameters.
        Assertions.assertAll(
                () -> Assertions.assertEquals(mockAccount, entry.getAccount()),
                () -> Assertions.assertEquals(mockBook, entry.getBook()),
                () -> Assertions.assertEquals(6, entry.getAmount())
        );
    }

    /**
     * Tests if {@link AccountBookServiceImpl} throws {@link IllegalArgumentException}
     *   on entry request if given account does not exist.
     */
    @Test
    @DisplayName("Throws exception if account in the request parameters does not exist")
    public void testAddOneThrowsExceptionIfAccountDoesNotExist() {
        // Create mock data.
        Book mockBook = new Book();

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        // Mocking service functionality with mock data.
        Mockito.when(accountService.findById(1L)).thenReturn(null);
        Mockito.when(bookService.findById(1L)).thenReturn(mockBook);

        // Check if an IllegalArgumentException is thrown.
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> accountBookService.addOne(1L, 1L, 3)
        );
    }

    /**
     * Tests if {@link AccountBookServiceImpl} throws {@link IllegalArgumentException}
     *   on entry request if given book does not exist.
     */
    @Test
    @DisplayName("Throws exception if product in the request parameters does not exist")
    public void testAddOneThrowsExceptionIfBookDoesNotExist() {
        // Create mock data.
        Account mockAccount = new Account();

        mockAccount.setId(1L);
        mockAccount.setBalance(10000);

        // Mocking service functionality with mock data.
        Mockito.when(accountService.findById(1L)).thenReturn(mockAccount);
        Mockito.when(bookService.findById(1L)).thenReturn(null);

        // Check if an IllegalArgumentException is thrown.
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> accountBookService.addOne(1L, 1L, 3)
        );
    }

    /**
     * Tests if {@link AccountBookServiceImpl} successfully finds
     *   the existing account-book entry for given account and book.
     */
    @Test
    @DisplayName("Finds entries by account and book")
    public void testFindsExistentEntryByAccountAndBook() {
        // Create mock data.
        Account account = new Account();
        Book book = new Book();

        account.setId(1L);
        account.setBalance(10000);

        book.setId(1L);
        book.setName("Algorithms");
        book.setAuthor("Robert Sedgewick");

        AccountBook mockEntry = new AccountBook(account, book, 3);

        // Mocking service functionality with mock data.
        Mockito.when(accountBookRepository.findAccountBookByAccountIdAndBookId(1L, 1L))
                .thenReturn(Optional.of(mockEntry));

        // Requesting an entry.
        AccountBook foundEntry = accountBookService.getByAccountAndBookOrCreate(account, book);

        // Check that entries are equal.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(foundEntry),
                () -> Assertions.assertEquals(mockEntry, foundEntry)
        );
    }

    /**
     * Tests if {@link AccountBookServiceImpl} successfully creates
     *   a new account-book entry for given account and book
     *   if it was previously nonexistent (inner level).
     */
    @Test
    @DisplayName("Creates a new account-book entry (inner level)")
    public void testCreatesNewEntryForAccountAndBookIfNonExistent() {
        // Create mock data.
        Account account = new Account();
        Book book = new Book();

        account.setId(1L);
        account.setBalance(10000);

        book.setId(1L);
        book.setName("Algorithms");
        book.setAuthor("Robert Sedgewick");

        AccountBook mockEntry = new AccountBook(account, book, 0);

        // Mocking service functionality with mock data.
        Mockito.when(accountBookRepository.findAccountBookByAccountIdAndBookId(1L, 1L))
                .thenReturn(Optional.empty());
        Mockito.when(accountBookRepository.save(any(AccountBook.class))).thenReturn(mockEntry);

        AccountBook foundEntry = accountBookService.getByAccountAndBookOrCreate(account, book);

        // Check if entry is created.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(foundEntry),
                () -> Assertions.assertEquals(mockEntry, foundEntry)
        );
    }

    /**
     * Tests if {@link AccountBookServiceImpl}
     *   throws an {@link IllegalArgumentException}
     *   if an attempt to save a null entry was made.
     */
    @Test
    @DisplayName("Throws exception if tried to save a null entry")
    public void testSavingNullEntryThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> accountBookService.save(null));
    }
}
