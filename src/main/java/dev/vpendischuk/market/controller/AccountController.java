package dev.vpendischuk.market.controller;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.dto.request.AccountUpdateRequestDto;
import dev.vpendischuk.market.dto.request.NewAccountRequestDto;
import dev.vpendischuk.market.dto.response.AccountDto;
import dev.vpendischuk.market.dto.assembler.AccountDtoAssembler;
import dev.vpendischuk.market.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST API controller used to provide client access to user account data.
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link AccountController} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    /* -------------------------------- Fields ----------------------------- */

    /**
     * An {@link AccountService} instance used to get current account data.
     */
    private final AccountService accountService;

    /**
     * An {@link AccountDtoAssembler} instance used to assemble {@link AccountDto}
     *   response DTOs.
     */
    private final AccountDtoAssembler accountDtoAssembler;

    /* ----------------------------- Constructors -------------------------- */

    /**
     * Initializes a new {@link AccountController} instance.
     *
     * @param accountService account service used to access the account repository.
     * @param accountDtoAssembler {@link AccountDto} object assembler.
     */
    public AccountController(AccountService accountService, AccountDtoAssembler accountDtoAssembler) {
        this.accountService = accountService;
        this.accountDtoAssembler = accountDtoAssembler;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Handles HTTP GET current account data retrieval request.
     *
     * @return current account data (HTTP code 200) or null if current account data
     *   could not be found in the database (HTTP code 500).
     */
    @Operation(summary = "Get current account data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned account information",
                    content = {
                    @Content(mediaType = "application/hal+json", schema = @Schema(implementation = AccountDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Server could not get current account information",
                    content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<AccountDto> getAccountInfo() {
        Account account = accountService.getCurrentAccount();

        // Account 1 not found in the database -> error code 500.
        if (account == null) {
            logger.debug("Could not get current account information");
            return ResponseEntity.internalServerError().body(null);
        }

        return ResponseEntity.ok().body(accountDtoAssembler.toModel(account));
    }
}
