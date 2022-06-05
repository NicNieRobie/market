package dev.vpendischuk.market.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * JPA Entity model of an account - model that represents user data in the system.
 */
@Entity
@Table(name="accounts")
@Getter
@NoArgsConstructor
public class Account implements Serializable {
    /* ---------------------------- Static fields -------------------------- */

    @Serial
    private static final long serialVersionUID = 6244737392040437757L;

    /* -------------------------------- Fields ----------------------------- */

    /**
     * Account ID.
     */
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    /**
     * Account money balance.
     */
    @NotNull
    @Setter
    @Column(name = "price", nullable = false)
    private Integer balance;

    /**
     * Books purchased by account owner.
     */
    @OneToMany(mappedBy="account", cascade=CascadeType.ALL, orphanRemoval=true)
    private final Set<AccountBook> accountBooks = new HashSet<>();

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two accounts are equal iff their balances and purchased book lists are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return balance.equals(account.balance) && accountBooks.equals(account.accountBooks);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(balance, accountBooks);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", accountBooks=" + accountBooks +
                '}';
    }
}
