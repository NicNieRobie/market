package dev.vpendischuk.market.service;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.domain.AccountBook;
import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.repository.AccountBookRepository;

/**
 * Interface that describes an account book service -
 *   service that provides access to the {@link AccountBookRepository}.
 */
public interface AccountBookService {
    /**
     * Retrieves an account-book entry for the given account and book.
     *
     * @param account the account.
     * @param book the purchased book.
     * @return the retrieved entry or null, if no entry exists for the given parameters.
     */
    AccountBook getByAccountAndBookOrCreate(Account account, Book book);

    /**
     * Saves a non-null account-book entry in the database.
     *
     * @param accountBook the entry to be persisted.
     * @return the persisted entry.
     */
    AccountBook save(AccountBook accountBook);

    /**
     * Adds an entry (or updates an existing entry) for the specified
     *   account ID and book ID to register a book purchase deal.
     *
     * @param accountId account of the purchaser.
     * @param bookId the purchased book ID.
     * @param quantity purchased book quantity.
     */
    void addOne(long accountId, long bookId, int quantity);

    /**
     * Deletes all account-book entries and resets their ID generation.
     */
    void truncate();
}
