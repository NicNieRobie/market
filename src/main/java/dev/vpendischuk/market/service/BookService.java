package dev.vpendischuk.market.service;

import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.repository.BookRepository;

/**
 * Interface that describes an account book service -
 *   service that provides access to the {@link BookRepository}.
 */
public interface BookService {
    /**
     * Finds the book with the specified ID in the database.
     *
     * @param id book ID.
     * @return the found book entry or null, if no book
     *   with the specified ID exists.
     */
    Book findById(long id);

    /**
     * Saves a non-null book entry in the database.
     *
     * @param book the book entry to be persisted.
     * @return the persisted book entry.
     */
    Book save(Book book);

    /**
     * Deletes all book entries and resets their ID generation.
     */
    void truncate();
}
