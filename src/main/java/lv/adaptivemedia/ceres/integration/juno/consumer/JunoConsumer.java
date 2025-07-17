package lv.adaptivemedia.ceres.integration.juno.consumer;

import lv.adaptivemedia.ceres.error.BackendTechnicalException;
import lv.adaptivemedia.ceres.integration.juno.model.SalesDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class JunoConsumer {

    private static final Logger log = LoggerFactory.getLogger(JunoConsumer.class);
    private static final String SALES_DATA_ENDPOINT = "/sales-data";
    private static final String FETCH_SALES_ERROR_MESSAGE_TEMPLATE = "Exception while fetching sales information for time period %s - %s";

    private final WebClient webClient;
    private final String uri;

    public JunoConsumer(WebClient webClient,
                        @Value("${integration.juno.rest-uri}") String uri) {
        this.webClient = webClient;
        this.uri = uri;
    }

    public Flux<SalesDataDto> fetchSalesInformation(LocalDate fromDate, LocalDate toDate) {
        log.debug("Getting sales information for time period {} - {}", fromDate, toDate);

        final var salesDataUri = UriComponentsBuilder
                .fromUriString(uri)
                .path(SALES_DATA_ENDPOINT)
                .queryParam("fromDate", fromDate)
                .queryParam("toDate", toDate)
                .build()
                .toUri();

        return webClient.get()
                .uri(salesDataUri)
                .retrieve()
                .bodyToFlux(SalesDataDto.class)
                .doOnError(throwable -> handleErrorOnFetchSales(throwable, fromDate, toDate));
    }

    private void handleErrorOnFetchSales(Throwable throwable, LocalDate fromDate, LocalDate toDate) {
        throw new BackendTechnicalException(FETCH_SALES_ERROR_MESSAGE_TEMPLATE.formatted(fromDate, toDate), HttpStatus.INTERNAL_SERVER_ERROR, throwable);
    }
}
