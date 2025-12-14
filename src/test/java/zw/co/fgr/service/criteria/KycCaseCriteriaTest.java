package zw.co.fgr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class KycCaseCriteriaTest {

    @Test
    void newKycCaseCriteriaHasAllFiltersNullTest() {
        var kycCaseCriteria = new KycCaseCriteria();
        assertThat(kycCaseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void kycCaseCriteriaFluentMethodsCreatesFiltersTest() {
        var kycCaseCriteria = new KycCaseCriteria();

        setAllFilters(kycCaseCriteria);

        assertThat(kycCaseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void kycCaseCriteriaCopyCreatesNullFilterTest() {
        var kycCaseCriteria = new KycCaseCriteria();
        var copy = kycCaseCriteria.copy();

        assertThat(kycCaseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(kycCaseCriteria)
        );
    }

    @Test
    void kycCaseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var kycCaseCriteria = new KycCaseCriteria();
        setAllFilters(kycCaseCriteria);

        var copy = kycCaseCriteria.copy();

        assertThat(kycCaseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(kycCaseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var kycCaseCriteria = new KycCaseCriteria();

        assertThat(kycCaseCriteria).hasToString("KycCaseCriteria{}");
    }

    private static void setAllFilters(KycCaseCriteria kycCaseCriteria) {
        kycCaseCriteria.id();
        kycCaseCriteria.kycRef();
        kycCaseCriteria.status();
        kycCaseCriteria.channel();
        kycCaseCriteria.startedAt();
        kycCaseCriteria.lastActivityAt();
        kycCaseCriteria.lastUpdatedAt();
        kycCaseCriteria.completedAt();
        kycCaseCriteria.expiresAt();
        kycCaseCriteria.consentId();
        kycCaseCriteria.submissionId();
        kycCaseCriteria.kycDocumentId();
        kycCaseCriteria.kycNotificationId();
        kycCaseCriteria.customerId();
        kycCaseCriteria.distinct();
    }

    private static Condition<KycCaseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKycRef()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getChannel()) &&
                condition.apply(criteria.getStartedAt()) &&
                condition.apply(criteria.getLastActivityAt()) &&
                condition.apply(criteria.getLastUpdatedAt()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getExpiresAt()) &&
                condition.apply(criteria.getConsentId()) &&
                condition.apply(criteria.getSubmissionId()) &&
                condition.apply(criteria.getKycDocumentId()) &&
                condition.apply(criteria.getKycNotificationId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<KycCaseCriteria> copyFiltersAre(KycCaseCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKycRef(), copy.getKycRef()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getChannel(), copy.getChannel()) &&
                condition.apply(criteria.getStartedAt(), copy.getStartedAt()) &&
                condition.apply(criteria.getLastActivityAt(), copy.getLastActivityAt()) &&
                condition.apply(criteria.getLastUpdatedAt(), copy.getLastUpdatedAt()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getExpiresAt(), copy.getExpiresAt()) &&
                condition.apply(criteria.getConsentId(), copy.getConsentId()) &&
                condition.apply(criteria.getSubmissionId(), copy.getSubmissionId()) &&
                condition.apply(criteria.getKycDocumentId(), copy.getKycDocumentId()) &&
                condition.apply(criteria.getKycNotificationId(), copy.getKycNotificationId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
