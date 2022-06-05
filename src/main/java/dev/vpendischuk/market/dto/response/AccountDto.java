package dev.vpendischuk.market.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

/**
 * A data transfer object (DTO) that represents account data.
 */
@Getter
@Setter
public class AccountDto extends RepresentationModel<AccountDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * Account money balance.
     */
    private Integer balance;

    /**
     * Collection of DTOs that represent purchased books.
     */
    private List<AccountBookDto> books;

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two account DTOs are equal iff their balances and purchased books lists are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AccountDto that = (AccountDto) o;
        return balance.equals(that.balance) && books.equals(that.books);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), balance, books);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "AccountDto{" +
                "balance=" + balance +
                ", books=" + books +
                '}';
    }
}
