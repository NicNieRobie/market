package dev.vpendischuk.market.service;

import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.repository.BookRepository;
import dev.vpendischuk.market.repository.ProductRepository;
import dev.vpendischuk.market.service.impl.AccountServiceImpl;
import dev.vpendischuk.market.service.impl.BookServiceImpl;
import dev.vpendischuk.market.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

/**
 * Class that contains unit tests for the {@link ProductServiceImpl} service class.
 * <p>
 * Tests in this class check if an {@link ProductServiceImpl} functions properly in isolation
 *   from its dependencies' functionality.
 */
@DisplayName("ProductServiceImpl unit tests")
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplUnitTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link ProductServiceImpl} instance - the test subject.
     */
    @InjectMocks
    ProductServiceImpl productService;

    /**
     * Mock {@link ProductRepository} instance.
     */
    @Mock
    ProductRepository productRepository;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link ProductServiceImpl} successfully
     *   finds all products.
     */
    @Test
    @DisplayName("Finds all products")
    public void testFindAll() {
        // Creating mock data.
        Product firstProduct = new Product();
        Product secondProduct = new Product();
        Book firstBook = new Book();
        Book secondBook = new Book();

        firstBook.setId(1L);
        firstBook.setName("Algorithms");
        firstBook.setAuthor("Robert Sedgewick");

        firstBook.setId(1L);
        firstBook.setName("Algorithms");
        firstBook.setAuthor("Robert Sedgewick");

        firstProduct.setProductBook(firstBook);
        firstProduct.setAmount(2);
        firstProduct.setPrice(100);

        secondProduct.setProductBook(secondBook);
        firstProduct.setAmount(4);
        firstProduct.setPrice(500);

        List<Product> mockProducts = List.of(firstProduct, secondProduct);

        // Mocking service functionality with mock data.
        Mockito.when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> products = productService.findAll();

        // Checking if result contains all entries.
        Assertions.assertIterableEquals(mockProducts, products);
    }

    /**
     * Tests if {@link ProductServiceImpl} successfully
     *   finds product by its ID.
     */
    @Test
    @DisplayName("Finds product by its ID")
    public void testProductFoundByExistingId() {
        // Creating mock data.
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setProductBook(mockBook);
        mockProduct.setAmount(2);
        mockProduct.setPrice(100);

        // Mocking service functionality with mock data.
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Product foundProduct = productService.findById(1L);

        // Check if product was returned with correct parameters.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(foundProduct),
                () -> Assertions.assertEquals(mockProduct, foundProduct)
        );
    }

    /**
     * Tests if {@link ProductServiceImpl} returns null
     *   when trying to find nonexistent product.
     */
    @Test
    @DisplayName("Returns null for nonexistent ID")
    public void testProductNotFoundByNonExistingId() {
        // Mocking service functionality with mock data.
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Product foundProduct = productService.findById(2L);

        // Check that the found product is null.
        Assertions.assertNull(foundProduct);
    }

    /**
     * Tests if {@link ProductServiceImpl} successfully deducts
     *   a set amount of products from the total count.
     */
    @Test
    @DisplayName("Decreases product amount")
    public void testDecreasesProductAmount() {
        // Creating mock data.
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setProductBook(mockBook);
        mockProduct.setAmount(2);
        mockProduct.setPrice(100);

        // Mocking service functionality with mock data.
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        productService.decreaseAmount(1L, 1);
        Product foundProduct = productService.findById(1L);

        // Check if the product amount has decreased.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(foundProduct),
                () -> Assertions.assertEquals(1, foundProduct.getAmount())
        );
    }

    /**
     * Tests if {@link ProductServiceImpl}
     *   throws an {@link IllegalArgumentException}
     *   if an attempt to save a null entry was made.
     */
    @Test
    @DisplayName("Throws exception if tried to save a null entry")
    public void testSavingNullProductThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.save(null));
    }
}
