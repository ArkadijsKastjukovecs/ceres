package lv.adaptivemedia.ceres.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculationService {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final int SCALE = 4;

    public BigDecimal calculateConversionRate(Integer numberOfVisits, Integer numberOfSales) {
        if (numberOfVisits == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(numberOfSales).divide(BigDecimal.valueOf(numberOfVisits), SCALE, ROUNDING_MODE);
    }
}
