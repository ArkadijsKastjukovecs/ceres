package lv.adaptivemedia.ceres.web;

import lv.adaptivemedia.ceres.integration.juno.model.ConversionDto;
import lv.adaptivemedia.ceres.integration.juno.model.NamedConversionDto;
import lv.adaptivemedia.ceres.service.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/conversion")
public class ConversionController {

    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping("/specific")
    public ResponseEntity<ConversionDto> specificConversion(@RequestParam String pageCode,
                                                            @RequestParam LocalDate fromDate,
                                                            @RequestParam LocalDate toDate) {
        final var conversionDto = conversionService.calculateConversionRate(pageCode, fromDate, toDate);
        return new ResponseEntity<>(conversionDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NamedConversionDto>> specificConversion(@RequestParam LocalDate fromDate,
                                                                       @RequestParam LocalDate toDate) {
        final var conversionDto = conversionService.calculateConversionRate(fromDate, toDate);
        return new ResponseEntity<>(conversionDto, HttpStatus.OK);
    }
}
