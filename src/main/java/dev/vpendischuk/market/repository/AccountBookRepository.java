package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Spring Data JPA repository that presents an interface for
 *   the <b>account_book</b> table in the database.
 */
public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {
    /**
     * Finds an account book entry for the specified account and book IDs in the database.
     *
     * @param accountId account ID.
     * @param bookId purchased book ID.
     * @return Optional {@link AccountBook} entity - not empty if the entry does exist,
     *   empty otherwise.
     */
    Optional<AccountBook> findAccountBookByAccountIdAndBookId(Long accountId, Long bookId);

    /**
     * Truncates the <b>account_book</b> table and restarts its ID generator.
     */
    @Modifying
    @Query(
            value = "TRUNCATE TABLE account_book RESTART IDENTITY CASCADE",
            nativeQuery = true
    )
    void truncate();
}
