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
 * JPA Entity model of an account book entry - model that represents
 *   many-to-many associations between accounts and books.
 */
@Entity
@Table(name="account_book")
@Getter
@NoArgsConstructor
public class AccountBook implements Serializable {
    @Serial
    private static final long serialVersionUID = -2081352481302148019L;

    /* -------------------------------- Fields ----------------------------- */

    /**
     * Account book entry ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    /**
     * The account that purchased the book.
     */
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="account_id")
    private Account account;

    /**
     * The purchased book model.
     */
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="book_id")
    private Book book;

    /**
     * The amount of books purchased.
     */
    @Setter
    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    /* ----------------------------- Constructors -------------------------- */

    /**
     * Initializes a new {@link AccountBook} instance.
     *
     * @param account the account that purchased the book
     * @param book the purchased book model.
     * @param amount the amount of books purchased.
     */
    public AccountBook(Account account, Book book, Integer amount) {
        this.account = account;
        this.book = book;
        this.amount = amount;
    }

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two account-book entries are equal iff the respective account IDs and books IDs are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBook that = (AccountBook) o;
        return account.getId().equals(that.account.getId()) && book.getId().equals(that.book.getId());
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(account.getId(), book.getId());
    }

    // toString() method override.
    @Override
    public String toString() {
        return "AccountBook{" +
                "accountId=" + account.getId() +
                ", bookId=" + book.getId() +
                ", amount=" + amount +
                '}';
    }
}
