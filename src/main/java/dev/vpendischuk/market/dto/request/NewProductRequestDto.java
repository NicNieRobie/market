package dev.vpendischuk.market.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A data transfer object (DTO) that represents data for a product creation request.
 */
@Getter
@AllArgsConstructor
public class NewProductRequestDto {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * Book author.
     */
    @NotNull(message = "Author can't be null")
    private String author;

    /**
     * Book name.
     */
    @NotNull(message = "Name can't be null")
    private String name;

    /**
     * Book price.
     */
    @NotNull(message = "Price can't be null")
    @Min(value = 0, message = "Price can't be negative")
    private Integer price;

    /**
     * Amount of books.
     */
    @NotNull(message = "Amount can't be null")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Integer amount;
}
