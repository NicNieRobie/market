package dev.vpendischuk.market.service.impl;

import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.repository.ProductRepository;
import dev.vpendischuk.market.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The default implementation of a product service -
 *   service that provides access to the {@link ProductRepository}.
 */
@Service
public class ProductServiceImpl implements ProductService {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link ProductServiceImpl} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /* ------------------------------ Fields ------------------------------- */

    /**
     * A {@link ProductRepository} instance used to access the products table in the database.
     */
    private final ProductRepository productRepository;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link ProductServiceImpl} instance.
     *
     * @param productRepository a JPA interface for the products table access.
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Retrieves all persisted product entries in the database.
     *
     * @return list of all persisted product entries.
     */
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Finds the product with the specified ID in the database.
     *
     * @param id product ID.
     * @return the found product entry or null, if no product
     *   with the specified ID exists.
     */
    @Override
    public Product findById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Deducts the specified number from the product count.
     *
     * @param id product ID.
     * @param decrement balance decrement.
     */
    @Override
    public void decreaseAmount(long id, int decrement) {
        Product target = findById(id);

        if (target != null) {
            // Decrement is greater than amount -> IllegalArgumentException.
            if (target.getAmount() - decrement < 0) {
                logger.error("Failed to decrease amount of Product ID {} - " +
                        "decrement is greater than product amount", id);
                throw new IllegalArgumentException("Decrement is greater than product amount");
            }

            if (target.getAmount() - decrement == 0) {
                // Delete product if it's depleted.
                productRepository.delete(target);
            } else {
                target.setAmount(target.getAmount() - decrement);
                productRepository.save(target);
            }
        }
    }

    /**
     * Saves a non-null product entry in the database.
     *
     * @param product the product entry to be persisted.
     * @return the persisted product entry.
     */
    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Argument product cannot be null");
        }

        logger.debug("Saving product " + product);
        return productRepository.save(product);
    }

    /**
     * Deletes all product entries and resets their ID generation.
     */
    @Override
    @Transactional
    public void truncate() {
        logger.info("Clearing product data");
        productRepository.truncate();
    }
}
