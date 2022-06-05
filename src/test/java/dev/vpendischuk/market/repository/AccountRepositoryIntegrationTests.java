package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.Account;
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
 * Class that contains integration tests for the {@link AccountRepository} repository.
 * <p>
 * Tests in this class check if an {@link AccountRepository} functions properly
 *   and manages data in a dedicated database.
 * <p>
 * All tests in this class are transactional.
 */
@DisplayName("AccountRepository integration tests")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryIntegrationTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link AccountRepository} instance - the test subject.
     */
    @Autowired
    AccountRepository accountRepository;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link AccountRepository} successfully
     *   creates new account entries.
     */
    @Test
    @DisplayName("Creates a new account entry")
    public void testCreatesAccountEntry() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        // Saving entry.
        Account savedAccount = accountRepository.save(newAccount);

        // Checking for persistence.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedAccount.getId()),
                () -> Assertions.assertEquals(newAccount.getBalance(), savedAccount.getBalance()),
                () -> Assertions.assertEquals(0, savedAccount.getAccountBooks().size())
        );
    }

    /**
     * Tests if {@link AccountRepository} successfully
     *   saves changes in an account entry.
     */
    @Test
    @DisplayName("Saves changes in an entry")
    public void savesChangesInAccountEntry() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        // Saving entry.
        long entryId = accountRepository.save(newAccount).getId();

        // Changing entry.
        newAccount.setBalance(9000);

        // Saving changes.
        accountRepository.save(newAccount);

        Optional<Account> savedEntryOptional = accountRepository.findById(entryId);

        // Checking for persistence.
        Assertions.assertTrue(savedEntryOptional.isPresent());
        Assertions.assertEquals(9000, savedEntryOptional.get().getBalance());
    }

    /**
     * Tests if {@link AccountRepository} successfully
     *   finds an account entry by its ID.
     */
    @Test
    @DisplayName("Finds an existing entry by account ID")
    public void testFindsExistentEntry() {
        // Creating data.
        Account newAccount = new Account();
        newAccount.setBalance(10000);

        // Saving entry.
        Account savedAccount = accountRepository.save(newAccount);

        Optional<Account> foundAccountOptional = accountRepository.findById(savedAccount.getId());

        // Checking for persistence.

        Assertions.assertTrue(foundAccountOptional.isPresent());

        Account foundAccount = foundAccountOptional.get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(newAccount.getBalance(), foundAccount.getBalance()),
                () -> Assertions.assertEquals(0, foundAccount.getAccountBooks().size())
        );
    }

    /**
     * Tests if {@link AccountRepository} successfully
     *   finds all existing entries.
     */
    @Test
    @DisplayName("Finds all entries")
    public void findsAllExistentEntries() {
        // Creating data.
        Account firstNewAccount = new Account();
        firstNewAccount.setBalance(10000);

        Account secondNewAccount = new Account();
        secondNewAccount.setBalance(500);

        // Saving entries.
        Account firstSavedAccount = accountRepository.save(firstNewAccount);
        Account secondSavedAccount = accountRepository.save(secondNewAccount);

        List<Account> savedAccounts = accountRepository.findAll();

        // Checking if the found entries contain all the previously created entries.
        assertThat(savedAccounts, hasItems(firstSavedAccount, secondSavedAccount));
    }

    /**
     * Tests if {@link AccountRepository} successfully
     *   deletes all existing entries.
     */
    @Test
    @DisplayName("Deletes all entries")
    public void testDeletesAllEntries() {
        // Creating data.
        Account firstNewAccount = new Account();
        firstNewAccount.setBalance(10000);

        Account secondNewAccount = new Account();
        secondNewAccount.setBalance(500);

        // Saving entries.
        accountRepository.save(firstNewAccount);
        accountRepository.save(secondNewAccount);

        // Deleting all entries.
        accountRepository.deleteAll();

        List<Account> savedAccounts = accountRepository.findAll();

        // Checking if no entries were found.
        Assertions.assertEquals(0, savedAccounts.size());
    }
}
