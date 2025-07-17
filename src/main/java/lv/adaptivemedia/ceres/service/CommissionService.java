package lv.adaptivemedia.ceres.service;

import lv.adaptivemedia.ceres.dao.VisitDao;
import lv.adaptivemedia.ceres.integration.juno.model.CommissionDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CommissionService {

    private final VisitDao visitDao;

    public CommissionService(VisitDao visitDao) {
        this.visitDao = visitDao;
    }

    public CommissionDto calculateCommission(String pageCode, LocalDate fromDate, LocalDate toDate) {
        final var totalCommissionAmount = Optional.ofNullable(visitDao.findTotalCommissionAmount(pageCode, fromDate, toDate))
                .orElse(BigDecimal.ZERO);
        return new CommissionDto(totalCommissionAmount);
    }
}
