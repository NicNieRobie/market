package dev.vpendischuk.market.repository;

import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

/**
 * Class that contains integration tests for the {@link ProductRepository} repository.
 * <p>
 * Tests in this class check if an {@link ProductRepository} functions properly
 *   and manages data in a dedicated database.
 * <p>
 * All tests in this class are transactional.
 */
@DisplayName("ProductRepository integration tests")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryIntegrationTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * {@link ProductRepository} instance - the test subject.
     */
    @Autowired
    ProductRepository productRepository;

    /**
     * {@link BookRepository} instance required to set up data for tests.
     */
    @Autowired
    BookRepository bookRepository;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link ProductRepository} successfully
     *   creates new product entries.
     */
    @Test
    @DisplayName("Creates a new product entry")
    public void testCreatesProductEntry() {
        // Creating data.
        Product newProduct = new Product();
        Book newBook = new Book();

        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        newProduct.setPrice(1000);
        newProduct.setAmount(100);
        newProduct.setProductBook(newBook);

        // Saving entry.
        bookRepository.save(newBook);
        Product savedProduct = productRepository.save(newProduct);

        // Checking for persistence.
        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedProduct.getId()),
                () -> Assertions.assertEquals(newBook, savedProduct.getProductBook()),
                () -> Assertions.assertEquals(newProduct.getAmount(), savedProduct.getAmount()),
                () -> Assertions.assertEquals(newProduct.getPrice(), savedProduct.getPrice())
        );
    }

    /**
     * Tests if {@link ProductRepository} successfully
     *   saves changes in a product entry.
     */
    @Test
    @DisplayName("Saves changes in an entry")
    public void savesChangesInProductEntry() {
        // Creating data.
        Product newProduct = new Product();
        Book newBook = new Book();

        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        newProduct.setPrice(1000);
        newProduct.setAmount(100);
        newProduct.setProductBook(newBook);

        // Saving entry.
        bookRepository.save(newBook);
        long entryId = productRepository.save(newProduct).getId();

        // Changing entry.
        newProduct.setPrice(2000);

        // Saving changes.
        productRepository.save(newProduct);

        Optional<Product> savedEntryOptional = productRepository.findById(entryId);

        // Checking for persistence.
        Assertions.assertTrue(savedEntryOptional.isPresent());
        Assertions.assertEquals(2000, savedEntryOptional.get().getPrice());
    }

    /**
     * Tests if {@link ProductRepository} successfully
     *   finds a product entry by its ID.
     */
    @Test
    @DisplayName("Finds an existing entry by product ID")
    public void testFindsExistentEntry() {
        // Creating data.
        Product newProduct = new Product();
        Book newBook = new Book();

        newBook.setName("Algorithms");
        newBook.setAuthor("Robert Sedgewick");

        newProduct.setPrice(1000);
        newProduct.setAmount(100);
        newProduct.setProductBook(newBook);

        // Saving entry.
        bookRepository.save(newBook);
        Product savedProduct = productRepository.save(newProduct);

        Optional<Product> foundProductOptional = productRepository.findById(savedProduct.getId());

        // Checking for persistence.

        Assertions.assertTrue(foundProductOptional.isPresent());

        Product foundProduct = foundProductOptional.get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(newBook, foundProduct.getProductBook()),
                () -> Assertions.assertEquals(newProduct.getAmount(), foundProduct.getAmount()),
                () -> Assertions.assertEquals(newProduct.getPrice(), foundProduct.getPrice())
        );
    }

    /**
     * Tests if {@link ProductRepository} successfully
     *   finds all existing entries.
     */
    @Test
    @DisplayName("Finds all entries")
    public void findsAllExistentEntries() {
        // Creating data.
        Product firstNewProduct = new Product();
        Product secondNewProduct = new Product();
        Book firstNewBook = new Book();
        Book secondNewBook = new Book();

        firstNewBook.setName("Algorithms");
        firstNewBook.setAuthor("Robert Sedgewick");

        secondNewBook.setName("The C++ Programming Language");
        secondNewBook.setAuthor("Bjarne Stroustrup");

        firstNewProduct.setPrice(1000);
        firstNewProduct.setAmount(100);
        firstNewProduct.setProductBook(firstNewBook);

        secondNewProduct.setPrice(2000);
        secondNewProduct.setAmount(20);
        secondNewProduct.setProductBook(secondNewBook);

        // Saving entries.
        bookRepository.save(firstNewBook);
        bookRepository.save(secondNewBook);
        Product firstSavedProduct = productRepository.save(firstNewProduct);
        Product secondSavedProduct = productRepository.save(secondNewProduct);

        List<Product> savedProducts = productRepository.findAll();

        // Checking if the found entries contain all the previously created entries.
        assertThat(savedProducts, hasItems(firstSavedProduct, secondSavedProduct));
    }

    /**
     * Tests if {@link ProductRepository} successfully
     *   deletes all existing entries.
     */
    @Test
    @DisplayName("Deletes all entries")
    public void testDeletesAllEntries() {
        // Creating data.
        Product firstNewProduct = new Product();
        Product secondNewProduct = new Product();
        Book firstNewBook = new Book();
        Book secondNewBook = new Book();

        firstNewBook.setName("Algorithms");
        firstNewBook.setAuthor("Robert Sedgewick");

        secondNewBook.setName("The C++ Programming Language");
        secondNewBook.setAuthor("Bjarne Stroustrup");

        firstNewProduct.setPrice(1000);
        firstNewProduct.setAmount(100);
        firstNewProduct.setProductBook(firstNewBook);

        secondNewProduct.setPrice(2000);
        secondNewProduct.setAmount(20);
        secondNewProduct.setProductBook(secondNewBook);

        // Saving entries.
        bookRepository.save(firstNewBook);
        bookRepository.save(secondNewBook);
        productRepository.save(firstNewProduct);
        productRepository.save(secondNewProduct);

        // Deleting all entries.
        productRepository.deleteAll();

        List<Product> savedProducts = productRepository.findAll();

        // Checking if no entries were found.
        Assertions.assertEquals(0, savedProducts.size());
    }
}
