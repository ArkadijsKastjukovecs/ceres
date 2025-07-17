package lv.adaptivemedia.ceres.service;

import lv.adaptivemedia.ceres.dao.VisitDao;
import lv.adaptivemedia.ceres.integration.juno.consumer.JunoConsumer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class HistoryService {

    private static final LocalDate MIN_LOCALDATE = LocalDate.of(1970, 1, 1);

    private final JunoConsumer junoConsumer;
    private final VisitDao visitDao;

    public HistoryService(JunoConsumer junoConsumer, VisitDao visitDao) {
        this.junoConsumer = junoConsumer;
        this.visitDao = visitDao;
    }

    public void saveAllHistoryVisits() {
        final var lastSavedVisit = visitDao.findLastSavedVisit();
        final var fromDate = Optional.ofNullable(lastSavedVisit)
                .map(LocalDateTime::toLocalDate)
                .orElse(MIN_LOCALDATE);

        final var salesDataDto = junoConsumer.fetchSalesInformation(fromDate, LocalDate.now());
        final var thresholdDate = Optional.ofNullable(lastSavedVisit)
                .orElse(MIN_LOCALDATE.atStartOfDay());

        salesDataDto.filter(data -> data.visitDate().isAfter(thresholdDate))
                .subscribe(visitDao::saveVisitFromJuno);
    }
}
