package dev.vpendischuk.market.configuration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * Represents product book data in database seeding configs,
 *   i.e. JSON files with initial database seeding data.
 * <p>
 * The model used as a target class when mapping book data in the JSON
 *   to a Java object.
 */
@Getter
@NoArgsConstructor
public class ConfigurationBookModel {
    /**
     * Book's author.
     */
    private String author;

    /**
     * Book's name.
     */
    private String name;

    /**
     * Book's price.
     */
    @Min(0)
    private Integer price;

    /**
     * The amount of this product on the market.
     */
    @Min(1)
    private Integer amount;
}
