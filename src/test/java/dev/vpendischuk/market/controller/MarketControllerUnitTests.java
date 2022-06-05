package dev.vpendischuk.market.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.dto.response.BookDto;
import dev.vpendischuk.market.dto.response.MarketDto;
import dev.vpendischuk.market.dto.response.ProductDto;
import dev.vpendischuk.market.dto.assembler.MarketDtoAssembler;
import dev.vpendischuk.market.dto.assembler.ProductDtoAssembler;
import dev.vpendischuk.market.service.AccountBookService;
import dev.vpendischuk.market.service.AccountService;
import dev.vpendischuk.market.service.BookService;
import dev.vpendischuk.market.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

/**
 * Class that contains unit tests for the {@link MarketController} controller class.
 * <p>
 * Tests in this class check if a {@link MarketController} functions properly in isolation
 *   from its dependencies' functionality (i.e. calls the correct services in correct order).
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(MarketController.class)
@AutoConfigureMockMvc
@DisplayName("MarketController unit tests")
public class MarketControllerUnitTests {
    /* ---------------------------- Configuration -------------------------- */

    /**
     * Mock {@link AccountBookService} instance.
     */
    @MockBean
    private AccountBookService accountBookService;

    /**
     * Mock {@link ProductService} instance.
     */
    @MockBean
    private ProductService productService;

    /**
     * Mock {@link AccountService} instance.
     */
    @MockBean
    private AccountService accountService;

    /**
     * Mock {@link BookService} instance.
     */
    @MockBean
    private BookService bookService;

    /**
     * Mock {@link MarketDtoAssembler} instance.
     */
    @MockBean
    private MarketDtoAssembler marketDtoAssembler;

    /**
     * Mock {@link ProductDtoAssembler} instance.
     */
    @MockBean
    private ProductDtoAssembler productDtoAssembler;

    /**
     * {@link MockMvc} instance used for testing.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    /* -------------------------------- Tests ------------------------------ */

    /**
     * Tests if {@link MarketController}'s market info retrieval
     *   functions properly in isolation.
     */
    @Test
    @DisplayName("Returns valid market info")
    public void testAll() throws Exception {
        // Creating mock data.
        MarketDto mockMarketDto = new MarketDto();
        ProductDto mockProductDto = new ProductDto();
        BookDto mockBookDto = new BookDto();

        mockBookDto.setName("Algorithms");
        mockBookDto.setAuthor("Robert Sedgewick");
        mockProductDto.setId(1L);
        mockProductDto.setBook(mockBookDto);
        mockProductDto.setPrice(100);
        mockProductDto.setAmount(1);
        mockMarketDto.setProducts(List.of(mockProductDto));

        // Mocking service functionality with mock data.
        Mockito.when(marketDtoAssembler.toModel(anyList())).thenReturn(mockMarketDto);

        // Checking response status and JSON content.
        mockMvc.perform(get("/market"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.products[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.products[0].book.name", Matchers.is("Algorithms")))
                .andExpect(jsonPath("$.products[0].book.author", Matchers.is("Robert Sedgewick")))
                .andExpect(jsonPath("$.products[0].price", Matchers.is(100)))
                .andExpect(jsonPath("$.products[0].amount", Matchers.is(1)));
    }

    /**
     * Tests if {@link MarketController}'s deal is successful
     *   if all the necessary conditions are met.
     */
    @Test
    @DisplayName("The deal is successful if all requirements are met")
    public void testDealSuccessful() throws Exception {
        // Creating mock data.
        Account mockAccount = new Account();
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockAccount.setId(1L);
        mockAccount.setBalance(10000);

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setId(1L);
        mockProduct.setPrice(1000);
        mockProduct.setAmount(2);
        mockProduct.setProductBook(mockBook);

        // Mocking service functionality with mock data.
        Mockito.when(productService.findById(anyLong())).thenReturn(mockProduct);
        Mockito.when(accountService.getCurrentAccount()).thenReturn(mockAccount);

        // Checking response status.
        mockMvc.perform(post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"amount\": 2}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests if {@link MarketController}'s deal is rejected
     *   if user has less money than required.
     */
    @Test
    @DisplayName("The deal is rejected if user has less money than required")
    public void testDealRejectedIfNotEnoughMoney() throws Exception {
        // Creating mock data.
        Account mockAccount = new Account();
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockAccount.setId(1L);
        mockAccount.setBalance(1000);

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setId(1L);
        mockProduct.setPrice(1000);
        mockProduct.setAmount(2);
        mockProduct.setProductBook(mockBook);

        // Mocking service functionality with mock data.
        Mockito.when(productService.findById(anyLong())).thenReturn(mockProduct);
        Mockito.when(accountService.getCurrentAccount()).thenReturn(mockAccount);

        // Checking response status and the error message.
        mockMvc.perform(post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"amount\": 2}"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string("ERROR: Not enough money for Product ID 1"));
    }

    /**
     * Tests if {@link MarketController}'s deal is rejected
     *   if there isn't enough product on the market.
     */
    @Test
    @DisplayName("The deal is rejected if there isn't enough product on the market")
    public void testDealRejectedIfNotEnoughProduct() throws Exception {
        // Creating mock data.
        Account mockAccount = new Account();
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockAccount.setId(1L);
        mockAccount.setBalance(10000);

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setId(1L);
        mockProduct.setPrice(1000);
        mockProduct.setAmount(1);
        mockProduct.setProductBook(mockBook);

        // Mocking service functionality with mock data.
        Mockito.when(productService.findById(anyLong())).thenReturn(mockProduct);
        Mockito.when(accountService.getCurrentAccount()).thenReturn(mockAccount);

        // Checking response status and the error message.
        mockMvc.perform(post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"amount\": 2}"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string("ERROR: Not enough product for Product ID 1"));
    }

    /**
     * Tests if {@link MarketController}'s deal is rejected
     *   if the product was not found.
     */
    @Test
    @DisplayName("The deal is rejected if the product was not found")
    public void testDealRejectedIfProductNotFound() throws Exception {
        // Creating mock data.
        Account mockAccount = new Account();

        mockAccount.setId(1L);
        mockAccount.setBalance(10000);

        // Mocking service functionality with mock data.
        Mockito.when(productService.findById(anyLong())).thenReturn(null);
        Mockito.when(accountService.getCurrentAccount()).thenReturn(mockAccount);

        // Checking response status and the error message.
        mockMvc.perform(post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"amount\": 2}"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string("ERROR: Product ID 1 invalid - product not found"));
    }

    /**
     * Tests if {@link MarketController}'s product creation
     *   functions properly in isolation.
     */
    @Test
    @DisplayName("Creates new product")
    public void testNewProductCreated() throws Exception {
        // Creating mock data.
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setId(1L);
        mockProduct.setPrice(100);
        mockProduct.setAmount(1);
        mockProduct.setProductBook(mockBook);

        ProductDto mockProductDto = new ProductDto();
        BookDto mockBookDto = new BookDto();

        mockBookDto.setName("Algorithms");
        mockBookDto.setAuthor("Robert Sedgewick");
        mockProductDto.setId(1L);
        mockProductDto.setBook(mockBookDto);
        mockProductDto.setPrice(100);
        mockProductDto.setAmount(1);

        // Mocking service functionality with mock data.
        Mockito.when(productService.save(any(Product.class))).thenReturn(mockProduct);
        Mockito.when(productDtoAssembler.toModel(any(Product.class))).thenReturn(mockProductDto);

        // Checking response status and JSON content.
        mockMvc.perform(post("/market")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"NAME\",\"author\":\"AUTHOR\",\"price\":100,\"amount\":12}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.name", Matchers.is("Algorithms")))
                .andExpect(jsonPath("$.book.author", Matchers.is("Robert Sedgewick")))
                .andExpect(jsonPath("$.price", Matchers.is(100)))
                .andExpect(jsonPath("$.amount", Matchers.is(1)));
    }

    /**
     * Tests if {@link MarketController}'s product info by ID
     *   retrieval functions properly in isolation.
     */
    @Test
    @DisplayName("Retrieves product by ID if product exists")
    public void testGetsExistingProductById() throws Exception {
        // Creating mock data.
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setId(1L);
        mockProduct.setPrice(100);
        mockProduct.setAmount(1);
        mockProduct.setProductBook(mockBook);

        ProductDto mockProductDto = new ProductDto();
        BookDto mockBookDto = new BookDto();

        mockBookDto.setName("Algorithms");
        mockBookDto.setAuthor("Robert Sedgewick");
        mockProductDto.setId(1L);
        mockProductDto.setBook(mockBookDto);
        mockProductDto.setPrice(100);
        mockProductDto.setAmount(1);

        // Mocking service functionality with mock data.
        Mockito.when(productService.findById(anyLong())).thenReturn(mockProduct);
        Mockito.when(productDtoAssembler.toModel(any(Product.class))).thenReturn(mockProductDto);

        // Checking response status and JSON content.
        mockMvc.perform(get("/market/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.name", Matchers.is("Algorithms")))
                .andExpect(jsonPath("$.book.author", Matchers.is("Robert Sedgewick")))
                .andExpect(jsonPath("$.price", Matchers.is(100)))
                .andExpect(jsonPath("$.amount", Matchers.is(1)));
    }

    /**
     * Tests if {@link MarketController}'s product info by ID
     *   retrieval fails if product with such ID does not exist.
     */
    @Test
    @DisplayName("Does not retrieve product by ID if product with such ID does not exist")
    public void testDoesNotGetProductByNonExistentId() throws Exception {
        // Mocking service functionality.
        Mockito.when(productService.findById(anyLong())).thenReturn(null);

        // Checking response status.
        mockMvc.perform(get("/market/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Tests if {@link MarketController}'s product info by ID
     *   update functions properly in isolation.
     */
    @Test
    @DisplayName("Updates product")
    public void testPatchesExistingProduct() throws Exception {
        // Creating mock data.
        Product mockProduct = new Product();
        Book mockBook = new Book();

        mockBook.setId(1L);
        mockBook.setName("Algorithms");
        mockBook.setAuthor("Robert Sedgewick");

        mockProduct.setId(1L);
        mockProduct.setPrice(100);
        mockProduct.setAmount(1);
        mockProduct.setProductBook(mockBook);

        ProductDto mockProductDto = new ProductDto();
        BookDto mockBookDto = new BookDto();

        mockBookDto.setName("Algorithms");
        mockBookDto.setAuthor("Robert Sedgewick");
        mockProductDto.setId(1L);
        mockProductDto.setBook(mockBookDto);
        mockProductDto.setPrice(100);
        mockProductDto.setAmount(1);

        // Mocking service functionality with mock data.
        Mockito.when(productService.findById(anyLong())).thenReturn(mockProduct);
        Mockito.when(productService.save(any(Product.class))).thenReturn(mockProduct);
        Mockito.when(productDtoAssembler.toModel(any(Product.class))).thenReturn(mockProductDto);

        // Checking response status and JSON content.
        mockMvc.perform(patch("/market/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":100,\"amount\":2}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.name", Matchers.is("Algorithms")))
                .andExpect(jsonPath("$.book.author", Matchers.is("Robert Sedgewick")))
                .andExpect(jsonPath("$.price", Matchers.is(100)))
                .andExpect(jsonPath("$.amount", Matchers.is(1)));
    }

    /**
     * Tests if {@link MarketController}'s product info by ID
     *   update fails if product with given ID does not exist.
     */
    @Test
    @DisplayName("Does not update product if it does not exist")
    public void testFailsToPatchNonExistingProduct() throws Exception {
        // Mocking service functionality.
        Mockito.when(productService.findById(anyLong())).thenReturn(null);

        // Checking response status.
        mockMvc.perform(patch("/market/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":100,\"amount\":2}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
