package dev.vpendischuk.market.dto.assembler;

import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.dto.response.BookDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * A {@link RepresentationModelAssembler} implementation
 *   that assembles {@link BookDto} instances
 *   representing an {@link Book} entry.
 */
@Component
public class BookDtoAssembler implements RepresentationModelAssembler<Book, BookDto> {
    /* -------------------------- Public methods -------------------------- */

    /**
     * Creates an {@link BookDto} object model that represents the
     *   specified {@link Book} entity.
     *
     * @param entity the entity to generate representation for.
     * @return {@link BookDto} representation.
     */
    @Override
    public BookDto toModel(Book entity) {
        BookDto bookDto = new BookDto();
        bookDto.setAuthor(entity.getAuthor());
        bookDto.setName(entity.getName());

        return bookDto;
    }
}
