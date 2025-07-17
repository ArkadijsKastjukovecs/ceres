package lv.adaptivemedia.ceres.scheduler;

import lv.adaptivemedia.ceres.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UpdateVisitScheduler {

    private static final Logger log = LoggerFactory.getLogger(UpdateVisitScheduler.class);

    private final HistoryService historyService;

    public UpdateVisitScheduler(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES, initialDelay = 0)
    public void handleUnsavedVisits() {
        log.debug("Starting process of updating history visits");

        final var startTimeMillis = System.currentTimeMillis();
        historyService.saveAllHistoryVisits();
        final var endTimeMillis = System.currentTimeMillis();
        final var timeIntervalInSeconds = (endTimeMillis - startTimeMillis) / 1000;
        log.debug("Visit history updated in {} seconds", timeIntervalInSeconds);
    }
}
