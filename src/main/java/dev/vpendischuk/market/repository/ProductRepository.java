package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository that presents an interface for
 *   the <b>products</b> table in the database.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Truncates the <b>products</b> table and restarts its ID generator.
     */
    @Modifying
    @Query(
            value = "TRUNCATE TABLE products RESTART IDENTITY CASCADE",
            nativeQuery = true
    )
    void truncate();
}
