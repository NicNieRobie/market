package dev.vpendischuk.market.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vpendischuk.market.configuration.model.ConfigurationBookModel;
import dev.vpendischuk.market.configuration.model.ConfigurationModel;
import dev.vpendischuk.market.domain.Account;
import dev.vpendischuk.market.domain.Book;
import dev.vpendischuk.market.domain.Product;
import dev.vpendischuk.market.service.AccountBookService;
import dev.vpendischuk.market.service.AccountService;
import dev.vpendischuk.market.service.BookService;
import dev.vpendischuk.market.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;

/**
 * A command line runner used on application startup to load seeding data into the database.
 * <p>
 * The runner reads command line arguments set on application startup and loads seeding data from JSON files.
 * <p>
 * Database seeding may fail if data in the JSON file is not compliant with the format specified
 *   by the {@link ConfigurationModel} data type and its properties' data types.
 */
@Component
public class ConfigurationRunner implements CommandLineRunner {
    /* ---------------------------- Static fields -------------------------- */

    /**
     * {@link ConfigurationRunner} class-level logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationRunner.class);

    /* -------------------------------- Fields ----------------------------- */

    /**
     * An {@link AccountService} instance used by the runner to truncate
     *   the account repository and fill it with the seeding data.
     */
    private final AccountService accountService;

    /**
     * A {@link ProductService} instance used by the runner to truncate
     *   the product repository and fill it with the seeding data.
     */
    private final ProductService productService;

    /**
     * A {@link BookService} instance used by the runner to truncate
     *   the book repository and fill it with the seeding data.
     */
    private final BookService bookService;

    /**
     * An {@link AccountBookService} instance used by the runner to truncate
     *   the account-book entry repository and fill it with the seeding data.
     */
    private final AccountBookService accountBookService;

    /* ----------------------------- Constructors -------------------------- */

    /**
     * Initializes a new {@link ConfigurationRunner} instance.
     *
     * @param accountService account service used to access the account repository.
     * @param productService product service used to access the product repository.
     * @param bookService book service used to access the book repository.
     * @param accountBookService account-book entry service used to access the account-book entry repository.
     */
    public ConfigurationRunner(AccountService accountService,
                               ProductService productService,
                               BookService bookService,
                               AccountBookService accountBookService) {
        this.accountService = accountService;
        this.productService = productService;
        this.bookService = bookService;
        this.accountBookService = accountBookService;
    }

    /* -------------------------- Public methods -------------------------- */

    /**
     * Loads the seeding data from the JSON file with the specified path
     *   and fills the database with data.
     * <p>
     * If the data format in the specified file is invalid, the seeding process
     *   won't be initiated, and the application will use the data previously
     *   persisted in the database.
     *
     * @param inputStream the JSON file input stream.
     */
    public void loadSeedingData(InputStream inputStream) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ConfigurationModel> typeReference = new TypeReference<>() { };

        logger.info("Loading JSON seeding data");

        try {
            // Mapping data to a ConfigurationModel instance via Jackson.
            ConfigurationModel configurationModel = mapper.readValue(inputStream, typeReference);

            // Clearing the database.
            accountService.truncate();
            accountBookService.truncate();
            bookService.truncate();
            productService.truncate();

            // Persisting account data.
            Account account = new Account();
            account.setBalance(configurationModel.getAccount().getMoney());
            accountService.save(account);

            // Persisting product data.
            for (ConfigurationBookModel bookModel : configurationModel.getBooks()) {
                Book book = new Book(bookModel.getName(), bookModel.getAuthor());
                Product product = new Product(book, bookModel.getPrice(), bookModel.getAmount());

                bookService.save(book);
                productService.save(product);
            }

            logger.info("Loaded JSON seeding data");
        } catch (IOException ex) {
            logger.warn("Could not read data from the seeding data file");
        }
    }

    /**
     * Method executed on application startup.
     * <p>
     * Checks command line arguments for JSON file paths and tries
     *   to load database seeding data from them if the files exist.
     * <p>
     * Note: if multiple JSON files are specified, only data from the last (existing) file
     *   specified in the arguments array will be persisted in the database.
     *
     * @param args command line arguments.
     */
    @Override
    public void run(String... args) {
        for (String arg : args) {
            logger.debug("Arg: {}", arg);
        }

        logger.info("Startup configuration initiated...");

        if (args.length == 0) {
            logger.info("No seeding data JSON path specified - using persisted data from the database");
        }

        // Checking arguments.
        for (String arg : args) {
            if (arg.equals("staticSeed")) {
                logger.info("Static seeding flag detected, seeding database...");

                try {
                    ClassPathResource classPathResource = new ClassPathResource("static/data.json");
                    InputStream inputStream = classPathResource.getInputStream();
                    loadSeedingData(inputStream);
                } catch (IOException ex) {
                    logger.error("Could not read static seeding data");
                }
                break;
            }

            if (arg.endsWith(".json")) {
                try {
                    InputStream fileInputStream = new FileInputStream(arg);
                    loadSeedingData(fileInputStream);
                } catch (FileNotFoundException ex) {
                    logger.error("File {} not found", arg);
                }
            }
        }

        logger.info("Startup configuration done");
    }
}
