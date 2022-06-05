package dev.vpendischuk.market.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

/**
 * A data transfer object (DTO) that represents product data.
 */
@Getter
@Setter
public class ProductDto extends RepresentationModel<ProductDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * Product ID.
     */
    private Long id;

    /**
     * DTO that represents the book represented by this product.
     */
    private BookDto book;

    /**
     * Product price.
     */
    private Integer price;

    /**
     * Amount of product left.
     */
    private Integer amount;

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two product DTOs are equal iff the books they represent, their prices and their amounts are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductDto that = (ProductDto) o;
        return book.equals(that.book) && price.equals(that.price) && amount.equals(that.amount);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, book, price, amount);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", productBook=" + book +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
