package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository that presents an interface for
 *   the <b>books</b> table in the database.
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Truncates the <b>books</b> table and restarts its ID generator.
     */
    @Modifying
    @Query(
            value = "TRUNCATE TABLE books RESTART IDENTITY CASCADE",
            nativeQuery = true
    )
    void truncate();
}
