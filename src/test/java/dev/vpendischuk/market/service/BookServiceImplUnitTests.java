package dev.vpendischuk.market.service;

import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.repository.BookRepository;
import dev.vpendischuk.market.service.impl.AccountServiceImpl;
import dev.vpendischuk.market.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

/**
 * Class that contains unit tests for the {@link BookServiceImpl} service class.
 * <p>
 * Tests in this class check if an {@link BookServiceImpl} functions properly in isolation
 *   from its dependencies' functionality.
 */
@DisplayName("BookServiceImpl unit tests")
@ExtendWith(MockitoExtension.class)
public class BookServiceImplUnitTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link BookServiceImpl} instance - the test subject.
     */
    @InjectMocks
    BookServiceImpl bookService;

    /**
     * Mock {@link BookRepository} instance.
     */
    @Mock
    BookRepository bookRepository;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link BookServiceImpl} successfully
     *   finds book by its ID.
     */
    @Test
    @DisplayName("Finds book by its ID")
    public void testBookFoundByExistingId() {
        // Creating mock data.
        Book mockBook = new Book();

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        // Mocking service functionality with mock data.
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        Book foundBook = bookService.findById(1L);

        // Check if book was returned with correct parameters.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(foundBook),
                () -> Assertions.assertEquals(mockBook, foundBook)
        );
    }

    /**
     * Tests if {@link BookServiceImpl} returns null
     *   when trying to find nonexistent book.
     */
    @Test
    @DisplayName("Returns null for nonexistent ID")
    public void testBookNotFoundByNonexistentId() {
        // Mocking service functionality.
        Mockito.when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        Book foundBook = bookService.findById(2L);

        // Check that the found book is null.
        Assertions.assertNull(foundBook);
    }

    /**
     * Tests if {@link BookServiceImpl}
     *   throws an {@link IllegalArgumentException}
     *   if an attempt to save a null entry was made.
     */
    @Test
    @DisplayName("Throws exception if tried to save a null entry")
    public void testSavingNullBookThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.save(null));
    }
}
