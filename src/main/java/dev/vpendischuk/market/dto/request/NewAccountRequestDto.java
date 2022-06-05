package dev.vpendischuk.market.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A data transfer object (DTO) that represents data for an account creation request.
 */
@Getter
@NoArgsConstructor
public class NewAccountRequestDto {
    /* ------------------------------ Fields ------------------------------- */

    /**
     * New account balance.
     */
    @NotNull
    @Min(value = 0, message = "Account balance value must be greater than 0")
    private Integer balance;
}
