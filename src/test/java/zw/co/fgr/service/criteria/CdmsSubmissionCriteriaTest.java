package zw.co.fgr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CdmsSubmissionCriteriaTest {

    @Test
    void newCdmsSubmissionCriteriaHasAllFiltersNullTest() {
        var cdmsSubmissionCriteria = new CdmsSubmissionCriteria();
        assertThat(cdmsSubmissionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cdmsSubmissionCriteriaFluentMethodsCreatesFiltersTest() {
        var cdmsSubmissionCriteria = new CdmsSubmissionCriteria();

        setAllFilters(cdmsSubmissionCriteria);

        assertThat(cdmsSubmissionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cdmsSubmissionCriteriaCopyCreatesNullFilterTest() {
        var cdmsSubmissionCriteria = new CdmsSubmissionCriteria();
        var copy = cdmsSubmissionCriteria.copy();

        assertThat(cdmsSubmissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cdmsSubmissionCriteria)
        );
    }

    @Test
    void cdmsSubmissionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cdmsSubmissionCriteria = new CdmsSubmissionCriteria();
        setAllFilters(cdmsSubmissionCriteria);

        var copy = cdmsSubmissionCriteria.copy();

        assertThat(cdmsSubmissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cdmsSubmissionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cdmsSubmissionCriteria = new CdmsSubmissionCriteria();

        assertThat(cdmsSubmissionCriteria).hasToString("CdmsSubmissionCriteria{}");
    }

    private static void setAllFilters(CdmsSubmissionCriteria cdmsSubmissionCriteria) {
        cdmsSubmissionCriteria.id();
        cdmsSubmissionCriteria.submissionRef();
        cdmsSubmissionCriteria.status();
        cdmsSubmissionCriteria.responseCode();
        cdmsSubmissionCriteria.responseMessage();
        cdmsSubmissionCriteria.attempts();
        cdmsSubmissionCriteria.submittedAt();
        cdmsSubmissionCriteria.lastAttemptAt();
        cdmsSubmissionCriteria.nextRetryAt();
        cdmsSubmissionCriteria.cdmsCustomerId();
        cdmsSubmissionCriteria.kycCaseId();
        cdmsSubmissionCriteria.distinct();
    }

    private static Condition<CdmsSubmissionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSubmissionRef()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getResponseCode()) &&
                condition.apply(criteria.getResponseMessage()) &&
                condition.apply(criteria.getAttempts()) &&
                condition.apply(criteria.getSubmittedAt()) &&
                condition.apply(criteria.getLastAttemptAt()) &&
                condition.apply(criteria.getNextRetryAt()) &&
                condition.apply(criteria.getCdmsCustomerId()) &&
                condition.apply(criteria.getKycCaseId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CdmsSubmissionCriteria> copyFiltersAre(
        CdmsSubmissionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSubmissionRef(), copy.getSubmissionRef()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getResponseCode(), copy.getResponseCode()) &&
                condition.apply(criteria.getResponseMessage(), copy.getResponseMessage()) &&
                condition.apply(criteria.getAttempts(), copy.getAttempts()) &&
                condition.apply(criteria.getSubmittedAt(), copy.getSubmittedAt()) &&
                condition.apply(criteria.getLastAttemptAt(), copy.getLastAttemptAt()) &&
                condition.apply(criteria.getNextRetryAt(), copy.getNextRetryAt()) &&
                condition.apply(criteria.getCdmsCustomerId(), copy.getCdmsCustomerId()) &&
                condition.apply(criteria.getKycCaseId(), copy.getKycCaseId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
