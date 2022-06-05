package dev.vpendischuk.market.dto.assembler;

import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.dto.response.BookDto;
import dev.vpendischuk.market.dto.response.ProductDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * A {@link RepresentationModelAssembler} implementation
 *   that assembles {@link ProductDto} instances
 *   representing an {@link Product} entry.
 */
@Component
public class ProductDtoAssembler implements RepresentationModelAssembler<Product, ProductDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * A {@link BookDtoAssembler} instance used to assemble {@link BookDto} DTOs.
     */
    private final BookDtoAssembler bookDtoAssembler;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link ProductDtoAssembler} instance.
     *
     * @param bookDtoAssembler {@link BookDtoAssembler} object assembler.
     */
    public ProductDtoAssembler(BookDtoAssembler bookDtoAssembler) {
        this.bookDtoAssembler = bookDtoAssembler;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Creates an {@link ProductDto} object model that represents the
     *   specified {@link Product} entity.
     *
     * @param entity the entity to generate representation for.
     * @return {@link ProductDto} representation.
     */
    @Override
    public ProductDto toModel(Product entity) {
        ProductDto productDto = new ProductDto();
        productDto.setId(entity.getId());
        productDto.setBook(bookDtoAssembler.toModel(entity.getProductBook()));
        productDto.setPrice(entity.getPrice());
        productDto.setAmount(entity.getAmount());
        return productDto;
    }
}
