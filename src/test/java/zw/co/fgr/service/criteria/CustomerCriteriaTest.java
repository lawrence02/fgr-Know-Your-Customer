package zw.co.fgr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerCriteriaTest {

    @Test
    void newCustomerCriteriaHasAllFiltersNullTest() {
        var customerCriteria = new CustomerCriteria();
        assertThat(customerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void customerCriteriaFluentMethodsCreatesFiltersTest() {
        var customerCriteria = new CustomerCriteria();

        setAllFilters(customerCriteria);

        assertThat(customerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void customerCriteriaCopyCreatesNullFilterTest() {
        var customerCriteria = new CustomerCriteria();
        var copy = customerCriteria.copy();

        assertThat(customerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(customerCriteria)
        );
    }

    @Test
    void customerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerCriteria = new CustomerCriteria();
        setAllFilters(customerCriteria);

        var copy = customerCriteria.copy();

        assertThat(customerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(customerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerCriteria = new CustomerCriteria();

        assertThat(customerCriteria).hasToString("CustomerCriteria{}");
    }

    private static void setAllFilters(CustomerCriteria customerCriteria) {
        customerCriteria.id();
        customerCriteria.customerRef();
        customerCriteria.customerType();
        customerCriteria.fullName();
        customerCriteria.dateOfBirth();
        customerCriteria.idNumber();
        customerCriteria.registrationNumber();
        customerCriteria.address();
        customerCriteria.phoneNumber();
        customerCriteria.createdAt();
        customerCriteria.updatedAt();
        customerCriteria.customerIdentifierId();
        customerCriteria.kycCaseId();
        customerCriteria.distinct();
    }

    private static Condition<CustomerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCustomerRef()) &&
                condition.apply(criteria.getCustomerType()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getDateOfBirth()) &&
                condition.apply(criteria.getIdNumber()) &&
                condition.apply(criteria.getRegistrationNumber()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getCustomerIdentifierId()) &&
                condition.apply(criteria.getKycCaseId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustomerCriteria> copyFiltersAre(CustomerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCustomerRef(), copy.getCustomerRef()) &&
                condition.apply(criteria.getCustomerType(), copy.getCustomerType()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getDateOfBirth(), copy.getDateOfBirth()) &&
                condition.apply(criteria.getIdNumber(), copy.getIdNumber()) &&
                condition.apply(criteria.getRegistrationNumber(), copy.getRegistrationNumber()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getCustomerIdentifierId(), copy.getCustomerIdentifierId()) &&
                condition.apply(criteria.getKycCaseId(), copy.getKycCaseId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
