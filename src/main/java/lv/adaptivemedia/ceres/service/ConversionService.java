package lv.adaptivemedia.ceres.service;

import lv.adaptivemedia.ceres.dao.VisitDao;
import lv.adaptivemedia.ceres.integration.juno.model.ConversionDto;
import lv.adaptivemedia.ceres.integration.juno.model.NamedConversionDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConversionService {

    private final VisitDao visitDao;
    private final CalculationService calculationService;

    public ConversionService(VisitDao visitDao, CalculationService calculationService) {
        this.visitDao = visitDao;
        this.calculationService = calculationService;
    }

    public ConversionDto calculateConversionRate(String pageCode, LocalDate fromDate, LocalDate toDate) {
        final var numberOfVisits = visitDao.findNumberOfVisits(pageCode, fromDate, toDate);
        final var numberOfPurchases = visitDao.findNumberOfPurchases(pageCode, fromDate, toDate);
        final var conversionRate = calculationService.calculateConversionRate(numberOfVisits, numberOfPurchases);
        return new ConversionDto(conversionRate);
    }

    public List<NamedConversionDto> calculateConversionRate(LocalDate fromDate, LocalDate toDate) {
        final var numberOfPurchases = visitDao.findPurchasesAndVisitsByProduct(fromDate, toDate);
        return numberOfPurchases.stream()
                .map(record3 -> {
                    final var conversionRate = calculationService.calculateConversionRate(record3.component3(), record3.component2());
                    return new NamedConversionDto(record3.component1(), conversionRate);
                })
                .toList();
    }
}
