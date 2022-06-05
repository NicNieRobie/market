package dev.vpendischuk.market.dto.assembler;

import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.dto.response.MarketDto;
import dev.vpendischuk.market.dto.response.ProductDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A {@link RepresentationModelAssembler} implementation
 *   that assembles {@link MarketDto} instances
 *   representing an {@link List<Product>} entry.
 */
@Component
public class MarketDtoAssembler implements RepresentationModelAssembler<List<Product>, MarketDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * A {@link ProductDtoAssembler} instance used to assemble {@link ProductDto} DTOs.
     */
    private final ProductDtoAssembler productDtoAssembler;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link MarketDtoAssembler} instance.
     *
     * @param productDtoAssembler {@link ProductDtoAssembler} object assembler.
     */
    public MarketDtoAssembler(ProductDtoAssembler productDtoAssembler) {
        this.productDtoAssembler = productDtoAssembler;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Creates an {@link MarketDto} object model that represents the
     *   specified {@link List<Product>} entity.
     *
     * @param entity the entity to generate representation for.
     * @return {@link MarketDto} representation.
     */
    @Override
    public MarketDto toModel(List<Product> entity) {
        MarketDto marketDto = new MarketDto();

        List<ProductDto> productDtos = entity.stream().map(productDtoAssembler::toModel).toList();
        marketDto.setProducts(productDtos);

        return marketDto;
    }
}
