package dev.vpendischuk.market.controller;

import dev.vpendischuk.market.configuration.ConfigurationRunner;
import dev.vpendischuk.market.dto.response.AccountDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

/**
 * Class that contains integration tests for the {@link AccountController} controller class.
 * <p>
 * Tests in this class check if an {@link AccountController} functions properly in a
 *   real application environment (i.e. when not isolated from its dependencies' functionality).
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@DisplayName("AccountController integration tests")
public class AccountControllerIntegrationTests {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link AccountControllerIntegrationTests} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AccountControllerIntegrationTests.class);

    /* ---------------------------- Configuration -------------------------- */

    /**
     * Injected {@link AccountController} instance used for testing.
     */
    @Autowired
    AccountController accountController;

    /**
     * Injected {@link ConfigurationRunner} instance used for database seeding.
     */
    @Autowired
    ConfigurationRunner configurationRunner;

    /**
     * Database seeding method that is run before each test.
     */
    @BeforeEach
    private void loadSeedData() {
        try {
            String dataFilePath = Objects.requireNonNull(this.getClass().getResource("data.json")).getPath();
            configurationRunner.loadSeedingData(new FileInputStream(dataFilePath));
        } catch (NullPointerException | FileNotFoundException ex) {
            logger.error("Could not load seeding data for an integration test");
        }
    }

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link AccountController}'s account info retrieval flow
     *   with dependencies functions properly.
     */
    @Test
    @DisplayName("Returns account info")
    public void testGetAccountInfo() {
        AccountDto accountInfo = accountController.getAccountInfo().getBody();

        Assertions.assertNotNull(accountInfo);

        AccountDto expectedAccountInfo = new AccountDto();
        expectedAccountInfo.setBalance(20000);
        expectedAccountInfo.setBooks(List.of());

        Assertions.assertEquals(expectedAccountInfo, accountInfo);
    }
}
