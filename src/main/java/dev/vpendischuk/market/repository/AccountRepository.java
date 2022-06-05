package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository that presents an interface for
 *   the <b>accounts</b> table in the database.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * Truncates the <b>accounts</b> table and restarts its ID generator.
     */
    @Modifying
    @Query(
            value = "TRUNCATE TABLE accounts RESTART IDENTITY CASCADE",
            nativeQuery = true
    )
    void truncate();
}
