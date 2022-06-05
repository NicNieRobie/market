package dev.vpendischuk.market.service.impl;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.domain.AccountBook;
import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.repository.AccountBookRepository;
import dev.vpendischuk.market.service.AccountBookService;
import dev.vpendischuk.market.service.AccountService;
import dev.vpendischuk.market.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The default implementation of an account book service -
 *   service that provides access to the {@link AccountBookRepository}.
 */
@Service
public class AccountBookServiceImpl implements AccountBookService {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link AccountBookServiceImpl} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AccountBookServiceImpl.class);

    /* ------------------------------ Fields ------------------------------- */

    /**
     * An {@link AccountBookRepository} instance used to access the account_book
     *   table in the database.
     */
    private final AccountBookRepository accountBookRepository;

    /**
     * An {@link AccountService} instance used to retrieve account data.
     */
    private final AccountService accountService;

    /**
     * A {@link BookService} instance used to retrieve book data.
     */
    private final BookService bookService;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link AccountBookServiceImpl} instance.
     *
     * @param accountBookRepository a JPA interface for the account_book table access.
     * @param accountService account service used to retrieve account data.
     * @param bookService book service used to retrieve book data.
     */
    @Autowired
    public AccountBookServiceImpl(AccountBookRepository accountBookRepository,
                                  AccountService accountService,
                                  BookService bookService) {
        this.accountBookRepository = accountBookRepository;
        this.accountService = accountService;
        this.bookService = bookService;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Retrieves an account-book entry for the given account and book.
     *
     * @param account the account.
     * @param book the purchased book.
     * @return the retrieved entry or null, if no entry exists for the given parameters.
     */
    @Override
    public AccountBook getByAccountAndBookOrCreate(Account account, Book book) {
        return accountBookRepository.findAccountBookByAccountIdAndBookId(account.getId(), book.getId()).orElseGet(
                () -> createAccountBookEntry(account, book)
        );
    }

    /**
     * Saves a non-null account-book entry in the database.
     *
     * @param accountBook the entry to be persisted.
     * @return the persisted entry.
     */
    @Override
    public AccountBook save(AccountBook accountBook) {
        if (accountBook == null) {
            throw new IllegalArgumentException("Argument accountBook cannot be null");
        }

        logger.debug("Saving account-book entry " + accountBook);

        return accountBookRepository.save(accountBook);
    }

    /**
     * Adds an entry (or updates an existing entry) for the specified
     *   account ID and book ID to register a book purchase deal.
     *
     * @param accountId account of the purchaser.
     * @param bookId the purchased book ID.
     * @param quantity purchased book quantity.
     */
    @Override
    public void addOne(long accountId, long bookId, int quantity) {
        Account account = accountService.findById(accountId);
        Book book = bookService.findById(bookId);

        // Checking IDs for validity.

        if (account == null) {
            logger.error("Can't add Book ID " + bookId + " to Account ID " + accountId + " - invalid Account ID");
            throw new IllegalArgumentException("Invalid Account ID " + accountId);
        }

        if (book == null) {
            logger.error("Can't add Book ID " + bookId + " to Account ID " + accountId + " - invalid Book ID");
            throw new IllegalArgumentException("Invalid Book ID " + bookId);
        }

        // Updating an existing account-book entry or creating a new one.
        AccountBook accountBookEntry = getByAccountAndBookOrCreate(account, book);
        accountBookEntry.setAmount(accountBookEntry.getAmount() + quantity);

        logger.debug("Updating account-book entry for Account ID " + accountId + ", Book ID " + bookId +
                " by quantity " + quantity);
        save(accountBookEntry);
    }

    /**
     * Deletes all account-book entries and resets their ID generation.
     */
    @Override
    @Transactional
    public void truncate() {
        logger.info("Clearing account book data");
        accountBookRepository.truncate();
    }

    /* -------------------------- Private methods -------------------------- */

    /**
     * Creates a new entry for the given account and book.
     *
     * @param account the account.
     * @param book the purchased book.
     * @return the persisted entry.
     */
    private AccountBook createAccountBookEntry(Account account, Book book) {
        logger.debug("Creating account-book entry for Account ID " + account.getId() + ", Book ID " + book.getId());

        AccountBook entry = new AccountBook(account, book, 0);
        return accountBookRepository.save(entry);
    }
}
