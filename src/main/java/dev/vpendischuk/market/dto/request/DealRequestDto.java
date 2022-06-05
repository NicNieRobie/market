package dev.vpendischuk.market.dto.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A data transfer object (DTO) that represents data for a deal request.
 */
@Getter
public class DealRequestDto {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * ID of the product that is to be purchased.
     */
    @NotNull(message = "Product ID can't be null")
    @Min(value = 1, message = "Product ID must be greater than 0")
    private Long id;

    /**
     * Product amount.
     */
    @NotNull(message = "Amount can't be null")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Integer amount;
}
