package dev.vpendischuk.market.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * JPA Entity model of a book - model that represents book data in the system.
 */
@Entity
@Table(name="books")
@NoArgsConstructor
public class Book implements Serializable {
    /* ---------------------------- Static fields -------------------------- */

    @Serial
    private static final long serialVersionUID = -7909258702246406882L;

    /* -------------------------------- Fields ----------------------------- */

    /**
     * Book ID.
     */
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    /**
     * Book name.
     */
    @NotEmpty
    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Book author name.
     */
    @NotEmpty
    @Getter
    @Setter
    @Column(name = "author", nullable = false)
    private String author;

    /* ----------------------------- Constructors -------------------------- */

    /**
     * Initializes a new {@link Book} instance.
     *
     * @param name book name.
     * @param author book author name.
     */
    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two books are equal iff their names and authors are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return name.equals(book.name) && author.equals(book.author);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(name, author);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
