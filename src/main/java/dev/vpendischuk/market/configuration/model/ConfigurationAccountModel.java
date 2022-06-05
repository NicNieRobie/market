package dev.vpendischuk.market.configuration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * Represents user account data in database seeding configs,
 *   i.e. JSON files with initial database seeding data.
 * <p>
 * The model used as a target class when mapping account data in the JSON
 *   to a Java object.
 */
@Getter
@NoArgsConstructor
public class ConfigurationAccountModel {
    /**
     * Account balance.
     */
    @Min(0)
    private Integer money;
}
