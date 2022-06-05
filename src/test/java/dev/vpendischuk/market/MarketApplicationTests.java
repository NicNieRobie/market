package dev.vpendischuk.market;

import dev.vpendischuk.market.controller.AccountController;
import dev.vpendischuk.market.controller.MarketController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Class that contains application context loading test.
 */
@DisplayName("Application context test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MarketApplicationTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * A {@link MarketController} instance, that has to be injected by
     *   the Spring's IoC container.
     */
    @Autowired
    MarketController marketController;

    /**
     * A {@link AccountController} instance, that has to be injected by
     *   the Spring's IoC container.
     */
    @Autowired
    AccountController accountController;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if the application's controllers were initialized
     *   in the application context.
     */
    @Test
    @DisplayName("Application context loads")
    void contextLoads() {
        // Check if the controllers are initialized.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(marketController),
                () -> Assertions.assertNotNull(accountController)
        );
    }
}
