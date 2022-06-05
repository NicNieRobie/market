package dev.vpendischuk.market.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

/**
 * A data transfer object (DTO) that represents market data.
 */
@Getter
@Setter
public class MarketDto extends RepresentationModel<MarketDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * List of DTOs that represent products currently on the market.
     */
    private List<ProductDto> products;

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two market DTOs are equal iff the product lists they represent are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketDto marketDto = (MarketDto) o;
        return Objects.equals(products, marketDto.products);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(products);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "MarketDto{" +
                "products=" + products +
                '}';
    }
}
