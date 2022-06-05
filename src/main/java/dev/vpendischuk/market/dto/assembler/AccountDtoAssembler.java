package dev.vpendischuk.market.dto.assembler;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.dto.response.AccountBookDto;
import dev.vpendischuk.market.dto.response.AccountDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A {@link RepresentationModelAssembler} implementation
 *   that assembles {@link AccountDto} instances
 *   representing an {@link Account} entry.
 */
@Component
public class AccountDtoAssembler implements RepresentationModelAssembler<Account, AccountDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * A {@link AccountBookDtoAssembler} instance used to assemble {@link AccountBookDto} DTOs.
     */
    private final AccountBookDtoAssembler accountBookDtoAssembler;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link AccountDtoAssembler} instance.
     *
     * @param accountBookDtoAssembler {@link AccountBookDto} object assembler.
     */
    public AccountDtoAssembler(AccountBookDtoAssembler accountBookDtoAssembler) {
        this.accountBookDtoAssembler = accountBookDtoAssembler;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Creates an {@link AccountDto} object model that represents the
     *   specified {@link Account} entity.
     *
     * @param entity the entity to generate representation for.
     * @return {@link AccountDto} representation.
     */
    @Override
    public AccountDto toModel(Account entity) {
        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(entity.getBalance());

        List<AccountBookDto> books = entity.getAccountBooks().stream().map(accountBookDtoAssembler::toModel).toList();
        accountDto.setBooks(books);

        return accountDto;
    }
}
