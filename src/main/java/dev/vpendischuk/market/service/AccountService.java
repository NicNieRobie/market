package dev.vpendischuk.market.service;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.repository.AccountRepository;

/**
 * Interface that describes an account book service -
 *   service that provides access to the {@link AccountRepository}.
 */
public interface AccountService {
    /**
     * Finds the account with the specified ID in the database.
     *
     * @param id account ID.
     * @return the found account entry or null, if no account
     *   with the specified ID exists.
     */
    Account findById(long id);

    /**
     * Retrieves the current user's account data from the database.
     * <p>
     * Note: as a hack for the current implementation
     *   that has no way of authenticating or authorizing the user
     *   due to the API request format requirements,
     *   this method de-facto returns the account with ID 1.
     *
     * @return the current user's account or null, if it doesn't exist.
     */
    Account getCurrentAccount();

    /**
     * Saves a non-null account in the database.
     *
     * @param account the account to be persisted.
     * @return the persisted account.
     */
    Account save(Account account);

    /**
     * Deducts the specified amount of money from the account.
     *
     * @param id account ID.
     * @param decrement balance decrement.
     */
    void decreaseBalance(long id, int decrement);

    /**
     * Deletes all account entries and resets their ID generation.
     */
    void truncate();
}
