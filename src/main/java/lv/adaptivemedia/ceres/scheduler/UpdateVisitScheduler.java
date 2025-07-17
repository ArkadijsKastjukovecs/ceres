package lv.adaptivemedia.ceres.scheduler;

import lv.adaptivemedia.ceres.service.HistoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UpdateVisitScheduler {

    private final HistoryService historyService;

    public UpdateVisitScheduler(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES, initialDelay = 0)
    public void handleUnsavedVisits() {
        historyService.saveAllHistoryVisits();
    }
}
