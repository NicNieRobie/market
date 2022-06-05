package dev.vpendischuk.market.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.dto.response.AccountBookDto;
import dev.vpendischuk.market.dto.response.AccountDto;
import dev.vpendischuk.market.dto.response.BookDto;
import dev.vpendischuk.market.dto.assembler.AccountDtoAssembler;
import dev.vpendischuk.market.service.AccountService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

/**
 * Class that contains unit tests for the {@link AccountController} controller class.
 * <p>
 * Tests in this class check if an {@link AccountController} functions properly in isolation
 *   from its dependencies' functionality (i.e. calls the correct services in correct order).
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
@DisplayName("AccountController unit tests")
public class AccountControllerUnitTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * Mock {@link AccountService} instance.
     */
    @MockBean
    private AccountService accountService;

    /**
     * Mock {@link AccountDtoAssembler} instance.
     */
    @MockBean
    private AccountDtoAssembler accountDtoAssembler;

    /**
     * {@link MockMvc} instance used for testing.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link AccountController}'s account info retrieval
     *   functions properly in isolation.
     */
    @Test
    @DisplayName("Returns account info")
    public void testGetAccountInfo() throws Exception {
        // Creating mock data.

        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setBalance(10000);

        AccountDto mockAccountDto = new AccountDto();
        mockAccountDto.setBalance(10000);

        BookDto mockBookDto = new BookDto();
        mockBookDto.setName("Algorithms");
        mockBookDto.setAuthor("Robert Sedgewick");

        AccountBookDto mockAccountBookDto = new AccountBookDto();
        mockAccountBookDto.setBook(mockBookDto);
        mockAccountBookDto.setAmount(3);

        mockAccountDto.setBooks(List.of(mockAccountBookDto));

        // Mocking service functionality with mock data.
        Mockito.when(accountService.getCurrentAccount()).thenReturn(mockAccount);
        Mockito.when(accountDtoAssembler.toModel(any(Account.class))).thenReturn(mockAccountDto);

        // Checking response status and JSON content.
        mockMvc.perform(get("/account"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", Matchers.is(10000)))
                .andExpect(jsonPath("$.books", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.books[0].book.name", Matchers.is("Algorithms")))
                .andExpect(jsonPath("$.books[0].book.author", Matchers.is("Robert Sedgewick")))
                .andExpect(jsonPath("$.books[0].amount", Matchers.is(3)));
    }
}
