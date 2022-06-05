package dev.vpendischuk.market.controller;

import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.dto.assembler.MarketDtoAssembler;
import dev.vpendischuk.market.dto.assembler.ProductDtoAssembler;
import dev.vpendischuk.market.dto.request.NewProductRequestDto;
import dev.vpendischuk.market.dto.request.ProductUpdateRequestDto;
import dev.vpendischuk.market.dto.request.DealRequestDto;
import dev.vpendischuk.market.dto.response.MarketDto;
import dev.vpendischuk.market.dto.response.ProductDto;
import dev.vpendischuk.market.service.AccountBookService;
import dev.vpendischuk.market.service.AccountService;
import dev.vpendischuk.market.service.BookService;
import dev.vpendischuk.market.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST API controller used to provide client access to market data (products data).
 */
@RestController
@RequestMapping("/market")
public class MarketController {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link MarketController} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MarketController.class);

    /* -------------------------------- Fields ----------------------------- */

    /**
     * An {@link AccountBookService} instance used to register book purchases
     *   made by the user.
     */
    private final AccountBookService accountBookService;

    /**
     * A {@link ProductService} instance used to access the product repository.
     */
    private final ProductService productService;

    /**
     * A {@link BookService} instance used to access the book repository.
     */
    private final BookService bookService;

    /**
     * A {@link BookService} instance used to access user balance on book purchases.
     */
    private final AccountService accountService;

    /**
     * A {@link MarketDtoAssembler} instance used to assemble {@link MarketDto}
     *   response DTOs.
     */
    private final MarketDtoAssembler marketDtoAssembler;

    /**
     * A {@link ProductDtoAssembler} instance used to assemble {@link ProductDto}
     *   response DTOs.
     */
    private final ProductDtoAssembler productDtoAssembler;

    /* ----------------------------- Constructors -------------------------- */

    /**
     * Initializes a new {@link MarketController} instance.
     *
     * @param accountBookService account book service used to register purchases.
     * @param productService product service used to access the product repository.
     * @param accountService account service used to access user balance.
     * @param bookService book service used to access the book repository.
     * @param marketDtoAssembler {@link MarketDto} object assembler.
     * @param productDtoAssembler {@link ProductDto} object assembler.
     */
    public MarketController(AccountBookService accountBookService,
                            ProductService productService,
                            AccountService accountService,
                            BookService bookService,
                            MarketDtoAssembler marketDtoAssembler,
                            ProductDtoAssembler productDtoAssembler) {
        this.accountBookService = accountBookService;
        this.productService = productService;
        this.accountService = accountService;
        this.bookService = bookService;
        this.marketDtoAssembler = marketDtoAssembler;
        this.productDtoAssembler = productDtoAssembler;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Handles HTTP GET market data retrieval request.
     *
     * @return market data (HTTP code 200).
     */
    @Operation(summary = "Get market data (list of products)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched market data",
                    content = {
                    @Content(mediaType = "application/hal+json", schema = @Schema(implementation = MarketDto.class))
            })
    })
    @GetMapping("")
    public ResponseEntity<MarketDto> all() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok().body(marketDtoAssembler.toModel(products));
    }

    /**
     * Handles HTTP POST product creation and persistence request.
     *
     * @param newProductRequestDto DTO containing new product data.
     * @return created product data (HTTP code 201).
     */
    @Operation(summary = "Create new product on the market")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new product",
                    content = {
                    @Content(mediaType = "application/hal+json", schema = @Schema(implementation = ProductDto.class))
            })
    })
    @PostMapping("")
    public ResponseEntity<ProductDto> newProduct(@Valid @RequestBody NewProductRequestDto newProductRequestDto) {
        // Initializing book data.
        Book newBook = new Book();
        newBook.setName(newProductRequestDto.getName());
        newBook.setAuthor(newProductRequestDto.getAuthor());

        // Initializing product data.
        Product newProduct = new Product();
        newProduct.setProductBook(newBook);
        newProduct.setAmount(newProductRequestDto.getAmount());
        newProduct.setPrice(newProductRequestDto.getPrice());

        // Saving to repositories.
        bookService.save(newBook);
        Product savedProduct = productService.save(newProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDtoAssembler.toModel(savedProduct));
    }

    /**
     * Handles HTTP GET product data by ID retrieval request.
     *
     * @param id product ID.
     * @return found product data (HTTP code 200) or null if product
     *   with specified ID could not be found in the database (HTTP code 404).
     */
    @Operation(summary = "Get product info by product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned account information",
                    content = {
                    @Content(mediaType = "application/hal+json", schema = @Schema(implementation = ProductDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Server could not find product with specified ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok().body(productDtoAssembler.toModel(product));
    }

    /**
     * Handles HTTP PATCH update product data by ID request.
     *
     * @param id product ID.
     * @param productUpdateRequestDto DTO containing new product data parameters.
     * @return updated product data (HTTP code 200) or null if product
     *   with specified ID could not be found in the database (HTTP code 404).
     */
    @Operation(summary = "Update product with specified product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated product information",
                    content = {
                    @Content(mediaType = "application/hal+json", schema = @Schema(implementation = ProductDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Server could not find product with specified ID",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @Valid @RequestBody ProductUpdateRequestDto productUpdateRequestDto) {
        Product targetProduct = productService.findById(id);

        // Product not found -> error 404.
        if (targetProduct == null) {
            logger.info("PATCH request for Product ID {} invalid - product not found", id);
            return ResponseEntity.notFound().build();
        }

        // Updating product fields.

        if (productUpdateRequestDto.getAmount() != null) {
            targetProduct.setAmount(productUpdateRequestDto.getAmount());
        }

        if (productUpdateRequestDto.getPrice() != null) {
            targetProduct.setPrice(productUpdateRequestDto.getPrice());
        }

        // Updating the book if required.
        if (productUpdateRequestDto.getBook() != null) {
            Book targetBook = bookService.findById(targetProduct.getProductBook().getId());
            Book newData = productUpdateRequestDto.getBook();

            if (newData.getName() != null) {
                targetBook.setName(newData.getName());
            }

            if (newData.getAuthor() != null) {
                targetBook.setAuthor(newData.getAuthor());
            }

            targetProduct.setProductBook(bookService.save(targetBook));
        }

        targetProduct = productService.save(targetProduct);

        return ResponseEntity.ok().body(productDtoAssembler.toModel(targetProduct));
    }

    /**
     * Handles HTTP POST perform book purchase deal request.
     *
     * @param dealRequestDto DTO containing deal parameters (product ID, quantity of books).
     * @return empty response (HTTP code 200) if the deal is successful or error message -
     *   if deal could not be performed due to invalid request (code 400) or
     *   if account data could not be found in the database (code 500).
     */
    @Operation(summary = "Update product with specified product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully performed the deal",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Server could not find product with specified ID OR " +
                    "Not enough product with specified ID OR " +
                    "Account balance is too low for the deal",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server couldn't get current account data",
                    content = @Content)
    })
    @PostMapping("/deal")
    public ResponseEntity<?> performDeal(@Valid @RequestBody DealRequestDto dealRequestDto) {
        long productId = dealRequestDto.getId();
        int productAmount = dealRequestDto.getAmount();

        // Fetching account and product data.
        Product targetProduct = productService.findById(dealRequestDto.getId());
        Account currentAccount = accountService.getCurrentAccount();

        // Product with specified ID not found -> error 400.
        if (targetProduct == null) {
            logger.info("Deal for Product ID {} rejected - product not found", productId);
            return ResponseEntity.badRequest().body("ERROR: Product ID " + productId +
                    " invalid - product not found");
        }

        // Account data could not be found -> error 500.
        if (currentAccount == null) {
            logger.info("Deal for Product ID {} rejected - current account unknown", productId);
            return ResponseEntity.internalServerError().body("ERROR: Couldn't get account information");
        }

        // Not enough product with specified ID -> error 400.
        if (targetProduct.getAmount() < productAmount) {
            logger.info("Deal for Product ID {} rejected - not enough product", productId);
            return ResponseEntity.badRequest().body("ERROR: Not enough product for Product ID " + productId);
        }

        int price = targetProduct.getPrice();

        // Not enough money for the deal -> error 400.
        if (price * productAmount > currentAccount.getBalance()) {
            logger.info("Deal for Product ID {} rejected - not enough money", productId);
            return ResponseEntity.badRequest().body("ERROR: Not enough money for Product ID " + productId);
        }

        long accountId = currentAccount.getId();
        long bookId = targetProduct.getProductBook().getId();

        // Registering purchase.
        accountBookService.addOne(accountId, bookId, productAmount);
        accountService.decreaseBalance(accountId, price * productAmount);
        productService.decreaseAmount(productId, productAmount);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
