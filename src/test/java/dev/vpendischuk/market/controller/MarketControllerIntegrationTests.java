package dev.vpendischuk.market.controller;

import dev.vpendischuk.market.configuration.ConfigurationRunner;
import dev.vpendischuk.market.dto.request.NewProductRequestDto;
import dev.vpendischuk.market.dto.request.ProductUpdateRequestDto;
import dev.vpendischuk.market.dto.response.BookDto;
import dev.vpendischuk.market.dto.response.MarketDto;
import dev.vpendischuk.market.dto.response.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

/**
 * Class that contains integration tests for the {@link MarketController} controller class.
 * <p>
 * Tests in this class check if a {@link MarketController} functions properly in a
 *   real application environment (i.e. when not isolated from its dependencies' functionality).
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@DisplayName("MarketController integration tests")
public class MarketControllerIntegrationTests {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link MarketControllerIntegrationTests} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MarketControllerIntegrationTests.class);

    /* ---------------------------- Configuration -------------------------- */

    /**
     * Injected {@link MarketController} instance used for testing.
     */
    @Autowired
    MarketController marketController;

    /**
     * Injected {@link ConfigurationRunner} instance used for database seeding.
     */
    @Autowired
    ConfigurationRunner configurationRunner;

    /**
     * Database seeding method that is run before each test.
     */
    @BeforeEach
    public void loadSeedData() {
        try {
            String dataFilePath = Objects.requireNonNull(this.getClass().getResource("data.json")).getPath();
            configurationRunner.loadSeedingData(new FileInputStream(dataFilePath));
        } catch (NullPointerException | FileNotFoundException ex) {
            logger.error("Could not load seeding data for an integration test");
        }
    }

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link MarketController}'s market info retrieval flow
     *   with dependencies functions properly.
     */
    @Test
    @DisplayName("Returns valid market info")
    public void testGetMarketProducts() {
        // Retrieving market data.

        MarketDto marketDto = marketController.all().getBody();

        Assertions.assertNotNull(marketDto);

        // Retrieving product list.

        List<ProductDto> products = marketDto.getProducts();

        Assertions.assertEquals(2, products.size());

        // Expected results.

        ProductDto firstExpectedProduct = new ProductDto();
        BookDto firstExpectedBook = new BookDto();

        firstExpectedProduct.setAmount(15);
        firstExpectedProduct.setPrice(1500);
        firstExpectedProduct.setBook(firstExpectedBook);

        firstExpectedBook.setName("Философия Java");
        firstExpectedBook.setAuthor("Брюс Эккель");

        ProductDto secondExpectedProduct = new ProductDto();
        BookDto secondExpectedBook = new BookDto();

        secondExpectedProduct.setAmount(10);
        secondExpectedProduct.setPrice(2500);
        secondExpectedProduct.setBook(secondExpectedBook);

        secondExpectedBook.setName("Effective Java");
        secondExpectedBook.setAuthor("Joshua Bloch");

        // Check for expected results.
        assertThat(products, hasItems(firstExpectedProduct, secondExpectedProduct));
    }

    /**
     * Tests if {@link MarketController}'s product info by ID retrieval flow
     *   with dependencies functions properly if product exists.
     */
    @Test
    @DisplayName("Retrieves product by ID")
    public void testGetProductById() {
        ProductDto productDto = marketController.getProduct(2L).getBody();

        Assertions.assertNotNull(productDto);

        // Comparing retrieved data with data from JSON.
        Assertions.assertAll(
                () -> Assertions.assertEquals(2500, productDto.getPrice()),
                () -> Assertions.assertEquals(10, productDto.getAmount()),
                () -> Assertions.assertEquals("Effective Java", productDto.getBook().getName()),
                () -> Assertions.assertEquals("Joshua Bloch", productDto.getBook().getAuthor())
        );
    }

    /**
     * Tests if {@link MarketController}'s product info by ID retrieval flow
     *   with dependencies returns null if product does not exist.
     */
    @Test
    @DisplayName("Does not retrieve product by ID if product with such ID does not exist")
    public void testDoesNotGetProductByNonExistentId() {
        ProductDto productDto = marketController.getProduct(3L).getBody();

        Assertions.assertNull(productDto);
    }

    /**
     * Tests if {@link MarketController}'s product creation flow
     *   with dependencies functions properly.
     */
    @Test
    @DisplayName("Creates new product")
    public void testCreatesProduct() {
        // New product data.
        NewProductRequestDto newProductRequestDto = new NewProductRequestDto(
                "Bjarne Stroustrup",
                "The C++ Programming Language",
                1000,
                100
        );

        // Creating product.
        ProductDto newProduct = marketController.newProduct(newProductRequestDto).getBody();

        // Comparing persisted data with initial data.
        Assertions.assertNotNull(newProduct);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1000, newProduct.getPrice()),
                () -> Assertions.assertEquals(100, newProduct.getAmount()),
                () -> Assertions.assertEquals("The C++ Programming Language", newProduct.getBook().getName()),
                () -> Assertions.assertEquals("Bjarne Stroustrup", newProduct.getBook().getAuthor()),
                () -> Assertions.assertEquals(3, newProduct.getId())
        );
    }

    /**
     * Tests if {@link MarketController}'s product update flow
     *   with dependencies functions properly.
     */
    @Test
    @DisplayName("Updates product")
    public void testPatchesExistingProduct() {
        // New product parameters.
        ProductUpdateRequestDto productUpdateRequestDto = new ProductUpdateRequestDto(null,3000, 20);

        // Updating product.
        ProductDto productDto = marketController.updateProduct(2L, productUpdateRequestDto).getBody();

        // Comparing persisted data with new parameters.
        Assertions.assertNotNull(productDto);
        Assertions.assertAll(
                () -> Assertions.assertEquals(3000, productDto.getPrice()),
                () -> Assertions.assertEquals(20, productDto.getAmount()),
                () -> Assertions.assertEquals("Effective Java", productDto.getBook().getName()),
                () -> Assertions.assertEquals("Joshua Bloch", productDto.getBook().getAuthor())
        );
    }

    /**
     * Tests if {@link MarketController}'s product update flow
     *   with dependencies fails if product with given ID does not exist.
     */
    @Test
    @DisplayName("Does not update product if it does not exist")
    public void failsToPatchNonExistingProduct() {
        ProductUpdateRequestDto productUpdateRequestDto = new ProductUpdateRequestDto(null,3000, 20);

        ProductDto productDto = marketController.updateProduct(3L, productUpdateRequestDto).getBody();

        // Controller output must be null.
        Assertions.assertNull(productDto);
    }
}
