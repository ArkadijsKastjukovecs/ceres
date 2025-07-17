package lv.adaptivemedia.ceres.dao;

import lv.adaptivemedia.ceres.integration.juno.model.SalesDataDto;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lv.adaptivemedia.ceres.jooq.generated.tables.Sales.SALES;
import static lv.adaptivemedia.ceres.jooq.generated.tables.Visits.VISITS;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.sum;

@Service
public class VisitDao {

    private final DSLContext dslContext;

    public VisitDao(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void saveVisitFromJuno(SalesDataDto salesData) {
        final var tracking = splitTrackingId(salesData.trackingId());
        final var visitsRecord = dslContext.insertInto(VISITS)
                .set(VISITS.TRACKING_PREFIX, tracking.trackingPrefix)
                .set(VISITS.TRACKING_NUMBER, tracking.trackingNumber)
                .set(VISITS.VISIT_DATE, salesData.visitDate())
                .set(VISITS.PRODUCT, salesData.product())
                .returning(VISITS.ID)
                .fetchOne();

        if (salesData.saleDate() != null && visitsRecord != null) {
            dslContext.insertInto(SALES)
                    .set(SALES.VISIT_ID, visitsRecord.getId())
                    .set(SALES.SALE_DATE, salesData.saleDate())
                    .set(SALES.SALE_PRICE, salesData.salePrice())
                    .set(SALES.COMMISSION_AMOUNT, salesData.commissionAmount())
                    .execute();
        }
    }

    public LocalDateTime findLastSavedVisit() {
        return dslContext
                .select(max(VISITS.VISIT_DATE))
                .from(VISITS)
                .fetchOne(0, LocalDateTime.class);
    }

    public Integer findNumberOfVisits(String trackingPrefix, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
        return dslContext.selectCount()
                .from(VISITS)
                .where(VISITS.VISIT_DATE.ge(fromDateTime))
                .and(VISITS.VISIT_DATE.le(toDateTime))
                .and(VISITS.TRACKING_PREFIX.eq(trackingPrefix))
                .fetchOne(0, Integer.class);
    }

    public Integer findNumberOfPurchases(String trackingPrefix, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
        return dslContext.selectCount()
                .from(VISITS)
                .join(SALES).on(VISITS.ID.eq(SALES.VISIT_ID).and(VISITS.TRACKING_PREFIX.eq(trackingPrefix)))
                .where(SALES.SALE_DATE.ge(fromDateTime))
                .and(SALES.SALE_DATE.le(toDateTime))
                .fetchOne(0, Integer.class);
    }

    public BigDecimal findTotalCommissionAmount(String trackingPrefix, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
        return dslContext
                .select(sum(SALES.COMMISSION_AMOUNT))
                .from(VISITS)
                .join(SALES).on(VISITS.ID.eq(SALES.VISIT_ID).and(VISITS.TRACKING_PREFIX.eq(trackingPrefix)))
                .where(SALES.SALE_DATE.ge(fromDateTime))
                .and(SALES.SALE_DATE.le(toDateTime))
                .fetchOne(0, BigDecimal.class);
    }

    public List<Record3<String, Integer, Integer>> findPurchasesAndVisitsByProduct(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
        return dslContext
                .select(VISITS.PRODUCT, count(SALES.ID), count(VISITS.ID))
                .from(VISITS)
                .leftJoin(SALES).on(VISITS.ID.eq(SALES.VISIT_ID))
                .where(VISITS.VISIT_DATE.ge(fromDateTime))
                .and(VISITS.VISIT_DATE.le(toDateTime))
                .groupBy(VISITS.PRODUCT)
                .fetch();
    }

    private static Tracking splitTrackingId(String trackingId) {
        final var prefixMatcher = Pattern.compile("^[A-Z]{3}")
                .matcher(trackingId);
        String trackingPrefix = null;
        if (prefixMatcher.find()) {
            trackingPrefix = prefixMatcher
                    .group();
        }

        Integer trackingNumberInt = null;
        final var numberMatcher = Pattern.compile("\\d{3}$")
                .matcher(trackingId);
        if (numberMatcher.find()) {
            trackingNumberInt = Optional.of(numberMatcher)
                    .map(Matcher::group)
                    .map(Integer::valueOf)
                    .orElse(null);
        }
        return new Tracking(trackingPrefix, trackingNumberInt);
    }

    private record Tracking(String trackingPrefix, Integer trackingNumber) {
    }
}
