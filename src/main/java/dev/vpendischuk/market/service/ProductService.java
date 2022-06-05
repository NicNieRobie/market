package dev.vpendischuk.market.service;

import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.repository.ProductRepository;

import java.util.List;

/**
 * Interface that describes an account book service -
 *   service that provides access to the {@link ProductRepository}.
 */
public interface ProductService {
    /**
     * Retrieves all persisted product entries in the database.
     *
     * @return list of all persisted product entries.
     */
    List<Product> findAll();

    /**
     * Finds the product with the specified ID in the database.
     *
     * @param id product ID.
     * @return the found product entry or null, if no product
     *   with the specified ID exists.
     */
    Product findById(long id);

    /**
     * Deducts the specified number from the product count.
     *
     * @param id product ID.
     * @param decrement balance decrement.
     */
    void decreaseAmount(long id, int decrement);

    /**
     * Saves a non-null product entry in the database.
     *
     * @param product the product entry to be persisted.
     * @return the persisted product entry.
     */
    Product save(Product product);

    /**
     * Deletes all product entries and resets their ID generation.
     */
    void truncate();
}
