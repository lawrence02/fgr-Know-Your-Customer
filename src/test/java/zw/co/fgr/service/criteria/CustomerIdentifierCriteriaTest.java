package zw.co.fgr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerIdentifierCriteriaTest {

    @Test
    void newCustomerIdentifierCriteriaHasAllFiltersNullTest() {
        var customerIdentifierCriteria = new CustomerIdentifierCriteria();
        assertThat(customerIdentifierCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void customerIdentifierCriteriaFluentMethodsCreatesFiltersTest() {
        var customerIdentifierCriteria = new CustomerIdentifierCriteria();

        setAllFilters(customerIdentifierCriteria);

        assertThat(customerIdentifierCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void customerIdentifierCriteriaCopyCreatesNullFilterTest() {
        var customerIdentifierCriteria = new CustomerIdentifierCriteria();
        var copy = customerIdentifierCriteria.copy();

        assertThat(customerIdentifierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(customerIdentifierCriteria)
        );
    }

    @Test
    void customerIdentifierCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerIdentifierCriteria = new CustomerIdentifierCriteria();
        setAllFilters(customerIdentifierCriteria);

        var copy = customerIdentifierCriteria.copy();

        assertThat(customerIdentifierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(customerIdentifierCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerIdentifierCriteria = new CustomerIdentifierCriteria();

        assertThat(customerIdentifierCriteria).hasToString("CustomerIdentifierCriteria{}");
    }

    private static void setAllFilters(CustomerIdentifierCriteria customerIdentifierCriteria) {
        customerIdentifierCriteria.id();
        customerIdentifierCriteria.identifierType();
        customerIdentifierCriteria.identifierValue();
        customerIdentifierCriteria.channel();
        customerIdentifierCriteria.verified();
        customerIdentifierCriteria.isPrimary();
        customerIdentifierCriteria.createdAt();
        customerIdentifierCriteria.verifiedAt();
        customerIdentifierCriteria.customerId();
        customerIdentifierCriteria.distinct();
    }

    private static Condition<CustomerIdentifierCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIdentifierType()) &&
                condition.apply(criteria.getIdentifierValue()) &&
                condition.apply(criteria.getChannel()) &&
                condition.apply(criteria.getVerified()) &&
                condition.apply(criteria.getIsPrimary()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getVerifiedAt()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustomerIdentifierCriteria> copyFiltersAre(
        CustomerIdentifierCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIdentifierType(), copy.getIdentifierType()) &&
                condition.apply(criteria.getIdentifierValue(), copy.getIdentifierValue()) &&
                condition.apply(criteria.getChannel(), copy.getChannel()) &&
                condition.apply(criteria.getVerified(), copy.getVerified()) &&
                condition.apply(criteria.getIsPrimary(), copy.getIsPrimary()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getVerifiedAt(), copy.getVerifiedAt()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
