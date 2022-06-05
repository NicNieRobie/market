package dev.vpendischuk.market.configuration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents data (account data, products data) in database seeding configs,
 *   i.e. JSON files with initial database seeding data.
 * <p>
 * The model used as a target class when mapping data in the JSON
 *   to a Java object.
 */
@Getter
@NoArgsConstructor
public class ConfigurationModel {
    /**
     * User account data.
     */
    private ConfigurationAccountModel account;

    /**
     * Market data.
     */
    private List<ConfigurationBookModel> books;
}
