package dev.vpendischuk.market.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

/**
 * A data transfer object (DTO) that represents a purchased book.
 */
@Getter
@Setter
public class AccountBookDto extends RepresentationModel<AccountBookDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * DTO representation of a book.
     */
    private BookDto book;

    /**
     * Amount of books purchased.
     */
    private Integer amount;

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two account book DTOs are equal iff the books they represent and
    //   the amounts of books purchased are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AccountBookDto that = (AccountBookDto) o;
        return book.equals(that.book) && amount.equals(that.amount);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), book, amount);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "AccountBookDto{" +
                "book=" + book +
                ", amount=" + amount +
                '}';
    }
}
