package lv.adaptivemedia.ceres.integration.juno.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SalesDataDto(
        Long id,
        String trackingId,
        LocalDateTime visitDate,
        LocalDateTime saleDate,
        BigDecimal salePrice,
        String product,
        BigDecimal commissionAmount
) {
}
