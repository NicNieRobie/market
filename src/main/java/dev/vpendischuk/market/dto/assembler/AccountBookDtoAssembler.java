package dev.vpendischuk.market.dto.assembler;

import dev.vpendischuk.market.domain.AccountBook;
import dev.vpendischuk.market.dto.response.AccountBookDto;
import dev.vpendischuk.market.dto.response.BookDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * A {@link RepresentationModelAssembler} implementation
 *   that assembles {@link AccountBookDto} instances
 *   representing an {@link AccountBook} entry.
 */
@Component
public class AccountBookDtoAssembler implements RepresentationModelAssembler<AccountBook, AccountBookDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * A {@link BookDtoAssembler} instance used to assemble {@link BookDto} DTOs.
     */
    private final BookDtoAssembler bookDtoAssembler;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link AccountBookDtoAssembler} instance.
     *
     * @param bookDtoAssembler {@link BookDto} object assembler.
     */
    public AccountBookDtoAssembler(BookDtoAssembler bookDtoAssembler) {
        this.bookDtoAssembler = bookDtoAssembler;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Creates an {@link AccountBookDto} object model that represents the
     *   specified {@link AccountBook} entity.
     *
     * @param entity the entity to generate representation for.
     * @return {@link AccountBookDto} representation.
     */
    @Override
    public AccountBookDto toModel(AccountBook entity) {
        AccountBookDto accountBookDto = new AccountBookDto();
        accountBookDto.setBook(bookDtoAssembler.toModel(entity.getBook()));
        accountBookDto.setAmount(entity.getAmount());
        return accountBookDto;
    }
}
