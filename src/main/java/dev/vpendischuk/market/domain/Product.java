package dev.vpendischuk.market.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * JPA Entity model of a product - model that represents product data in the system.
 */
@Entity
@Table(name="products")
@Getter
@Setter
@NoArgsConstructor
public class Product implements Serializable {
    /* ---------------------------- Static fields -------------------------- */

    @Serial
    private static final long serialVersionUID = -8928256441624637749L;

    /* -------------------------------- Fields ----------------------------- */

    /**
     * Product ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    /**
     * The book that this product represents.
     */
    @NotNull
    @OneToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book productBook;

    /**
     * Product price.
     */
    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    /**
     * Amount of product left.
     */
    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    /* ----------------------------- Constructors -------------------------- */

    /**
     * Initializes a new {@link Product} instance.
     *
     * @param productBook the book that this product represents.
     * @param price product price.
     * @param amount amount of product left.
     */
    public Product(Book productBook, Integer price, Integer amount) {
        this.productBook = productBook;
        this.price = price;
        this.amount = amount;
    }

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two products are equal iff the books they represent are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productBook.equals(product.productBook);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(productBook);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productBook=" + productBook +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
