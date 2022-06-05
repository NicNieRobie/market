package dev.vpendischuk.market.service.impl;

import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.repository.BookRepository;
import dev.vpendischuk.market.service.BookService;
import dev.vpendischuk.market.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The default implementation of a book service -
 *   service that provides access to the {@link BookRepository}.
 */
@Service
public class BookServiceImpl implements BookService {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link BookServiceImpl} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    /* ------------------------------ Fields ------------------------------- */

    /**
     * A {@link BookRepository} instance used to access the books table in the database.
     */
    private final BookRepository bookRepository;

    /**
     * A {@link ProductService} instance used to clear product data.
     */
    private final ProductService productService;

    /* --------------------------- Constructors ---------------------------- */

    /**
     * Initializes a new {@link BookServiceImpl} instance.
     *
     * @param bookRepository a JPA interface for the books table access.
     * @param productService product service used to clear product data.
     */
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ProductService productService) {
        this.bookRepository = bookRepository;
        this.productService = productService;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Finds the book with the specified ID in the database.
     *
     * @param id book ID.
     * @return the found book entry or null, if no book
     *   with the specified ID exists.
     */
    @Override
    public Book findById(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Saves a non-null book entry in the database.
     *
     * @param book the book entry to be persisted.
     * @return the persisted book entry.
     */
    @Override
    public Book save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Argument book cannot be null");
        }

        logger.debug("Saving book " + book);
        return bookRepository.save(book);
    }

    /**
     * Deletes all book entries and resets their ID generation.
     */
    @Override
    @Transactional
    public void truncate() {
        // Deleting books together with products as
        //   they are coupled through foreign keys.
        logger.info("Clearing book (& product) data");
        productService.truncate();
        bookRepository.truncate();
    }
}
