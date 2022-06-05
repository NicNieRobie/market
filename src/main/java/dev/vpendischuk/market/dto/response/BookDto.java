package dev.vpendischuk.market.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

/**
 * A data transfer object (DTO) that represents book data.
 */
@Getter
@Setter
public class BookDto extends RepresentationModel<BookDto> {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * Book name.
     */
    private String name;

    /**
     * Book author.
     */
    private String author;

    /* -------------------------- Public methods -------------------------- */

    // equals() method override.
    // Two book DTOs are equal iff their names and authors are equal.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookDto bookDto = (BookDto) o;
        return name.equals(bookDto.name) && author.equals(bookDto.author);
    }

    // hashCode() method override.
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, author);
    }

    // toString() method override.
    @Override
    public String toString() {
        return "BookDto{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
