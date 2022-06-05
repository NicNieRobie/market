package dev.vpendischuk.market.service;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.repository.AccountBookRepository;
import dev.vpendischuk.market.repository.AccountRepository;
import dev.vpendischuk.market.service.impl.AccountBookServiceImpl;
import dev.vpendischuk.market.service.impl.AccountServiceImpl;
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
 * Class that contains unit tests for the {@link AccountServiceImpl} service class.
 * <p>
 * Tests in this class check if an {@link AccountServiceImpl} functions properly in isolation
 *   from its dependencies' functionality.
 */
@DisplayName("AccountServiceImpl unit tests")
@ExtendWith(MockitoExtension.class)
public class AccountServiceImplUnitTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link AccountServiceImpl} instance - the test subject.
     */
    @InjectMocks
    AccountServiceImpl accountService;

    /**
     * Mock {@link AccountServiceImpl} instance.
     */
    @Mock
    AccountRepository accountRepository;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link AccountServiceImpl} successfully
     *   returns the current account.
     */
    @Test
    @DisplayName("Returns current account")
    public void testGetCurrentAccount() {
        // Creating mock data.
        Account account = new Account();

        account.setBalance(10000);
        account.setId(1L);

        // Mocking service functionality with mock data.
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account currentAccount = accountService.getCurrentAccount();

        // Check if account was returned with correct parameters.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(currentAccount),
                () -> Assertions.assertEquals(1L, currentAccount.getId()),
                () -> Assertions.assertEquals(10000, currentAccount.getBalance())
        );
    }

    /**
     * Tests if {@link AccountServiceImpl} successfully
     *   finds account by its ID.
     */
    @Test
    @DisplayName("Finds account by its ID")
    public void testAccountFoundByExistingId() {
        // Creating mock data.
        Account account = new Account();

        account.setBalance(10000);
        account.setId(1L);

        // Mocking service functionality with mock data.
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account foundAccount = accountService.findById(1L);

        // Check if account was returned with correct parameters.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(foundAccount),
                () -> Assertions.assertEquals(1L, foundAccount.getId()),
                () -> Assertions.assertEquals(10000, foundAccount.getBalance())
        );
    }

    /**
     * Tests if {@link AccountServiceImpl} returns null
     *   when trying to find nonexistent account.
     */
    @Test
    @DisplayName("Returns null for nonexistent ID")
    public void testAccountNotFoundByNonExistingId() {
        // Mocking service functionality.
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        Account foundAccount = accountService.findById(2L);

        // Check that the found account is null.
        Assertions.assertNull(foundAccount);
    }

    /**
     * Tests if {@link AccountServiceImpl} successfully deducts
     *   a set amount of money from account's balance.
     */
    @Test
    @DisplayName("Decreases account balance")
    public void testDecreasesAccountBalance() {
        // Creating mock data.
        Account account = new Account();

        account.setBalance(10000);
        account.setId(1L);

        // Mocking service functionality with mock data.
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Decreasing balance.
        accountService.decreaseBalance(1L, 2000);

        Account foundAccount = accountService.findById(1L);

        // Check if the account balance has decreased.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(foundAccount),
                () -> Assertions.assertEquals(8000, foundAccount.getBalance())
        );
    }

    /**
     * Tests if {@link AccountServiceImpl}
     *   throws an {@link IllegalArgumentException}
     *   if an attempt to save a null entry was made.
     */
    @Test
    @DisplayName("Throws exception if tried to save a null entry")
    public void testSavingNullAccountThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> accountService.save(null));
    }
}
