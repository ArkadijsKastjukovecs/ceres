package lv.adaptivemedia.ceres.integration.juno.consumer;

import lv.adaptivemedia.ceres.integration.juno.model.SalesDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class JunoConsumer {

    private static final Logger log = LoggerFactory.getLogger(JunoConsumer.class);
    private static final String SALES_DATA_ENDPOINT = "/sales-data";

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
                .bodyToFlux(SalesDataDto.class);
    }
}
