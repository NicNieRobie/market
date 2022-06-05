package dev.vpendischuk.market.service.impl;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.repository.AccountRepository;
import dev.vpendischuk.market.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The default implementation of an account service -
 *   service that provides access to the {@link AccountRepository}.
 */
@Service
public class AccountServiceImpl implements AccountService {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link AccountServiceImpl} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    /**
     * Current account ID (hack, must be removed if authentication gets added).
     */
    private static final Long ACCOUNT_ID = 1L;

    /* ------------------------------ Fields ------------------------------- */

    /**
     * An {@link AccountRepository} instance used to access the accounts
     *   table in the database.
     */
    private final AccountRepository accountRepository;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link AccountServiceImpl} instance.
     *
     * @param accountRepository a JPA interface for the accounts table access.
     */
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Finds the account with the specified ID in the database.
     *
     * @param id account ID.
     * @return the found account entry or null, if no account
     *   with the specified ID exists.
     */
    @Override
    public Account findById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

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
    @Override
    public Account getCurrentAccount() {
        return accountRepository.findById(ACCOUNT_ID).orElse(null);
    }

    /**
     * Deducts the specified amount of money from the account.
     *
     * @param id account ID.
     * @param decrement balance decrement.
     */
    @Override
    public void decreaseBalance(long id, int decrement) {
        logger.debug("Reducing balance for Account ID {} by {}", id, decrement);
        Account target = findById(id);

        if (target != null) {
            target.setBalance(target.getBalance() - decrement);
            accountRepository.save(target);
        }
    }

    /**
     * Saves a non-null account in the database.
     *
     * @param account the account to be persisted.
     * @return the persisted account.
     */
    @Override
    public Account save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Argument account cannot be null");
        }

        Account savedAccount = accountRepository.save(account);
        logger.debug("Saved account ID " + savedAccount.getId());

        return savedAccount;
    }

    /**
     * Deletes all account entries and resets their ID generation.
     */
    @Override
    @Transactional
    public void truncate() {
        logger.info("Clearing account data");
        accountRepository.truncate();
    }
}
