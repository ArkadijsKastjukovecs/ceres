package lv.adaptivemedia.ceres.web;

import lv.adaptivemedia.ceres.integration.juno.model.CommissionDto;
import lv.adaptivemedia.ceres.service.CommissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/commission")
public class CommissionController {


    private final CommissionService commissionService;

    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    @GetMapping("/specific")
    public ResponseEntity<CommissionDto> specificConversion(@RequestParam String pageCode,
                                                            @RequestParam LocalDate fromDate,
                                                            @RequestParam LocalDate toDate) {
        final var commissionDto = commissionService.calculateCommission(pageCode, fromDate, toDate);
        return ResponseEntity.ok(commissionDto);
    }
}
