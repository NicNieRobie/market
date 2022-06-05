package dev.vpendischuk.market.dto.request;

import dev.vpendischuk.market.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;

/**
 * A data transfer object (DTO) that represents data for a product update request.
 */
@Getter
@AllArgsConstructor
public class ProductUpdateRequestDto {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * New product book data.
     */
    private Book book;

    /**
     * New product price.
     */
    @Min(value = 0, message = "Price can't be negative")
    private Integer price;

    /**
     * New product amount.
     */
    @Min(value = 1, message = "Amount must be greater than 0")
    private Integer amount;
}
