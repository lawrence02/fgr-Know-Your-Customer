package zw.co.fgr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zw.co.fgr.domain.CustomerIdentifierTestSamples.*;
import static zw.co.fgr.domain.CustomerTestSamples.*;
import static zw.co.fgr.domain.KycCaseTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import zw.co.fgr.web.rest.TestUtil;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void customerIdentifierTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        CustomerIdentifier customerIdentifierBack = getCustomerIdentifierRandomSampleGenerator();

        customer.addCustomerIdentifier(customerIdentifierBack);
        assertThat(customer.getCustomerIdentifiers()).containsOnly(customerIdentifierBack);
        assertThat(customerIdentifierBack.getCustomer()).isEqualTo(customer);

        customer.removeCustomerIdentifier(customerIdentifierBack);
        assertThat(customer.getCustomerIdentifiers()).doesNotContain(customerIdentifierBack);
        assertThat(customerIdentifierBack.getCustomer()).isNull();

        customer.customerIdentifiers(new HashSet<>(Set.of(customerIdentifierBack)));
        assertThat(customer.getCustomerIdentifiers()).containsOnly(customerIdentifierBack);
        assertThat(customerIdentifierBack.getCustomer()).isEqualTo(customer);

        customer.setCustomerIdentifiers(new HashSet<>());
        assertThat(customer.getCustomerIdentifiers()).doesNotContain(customerIdentifierBack);
        assertThat(customerIdentifierBack.getCustomer()).isNull();
    }

    @Test
    void kycCaseTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        KycCase kycCaseBack = getKycCaseRandomSampleGenerator();

        customer.addKycCase(kycCaseBack);
        assertThat(customer.getKycCases()).containsOnly(kycCaseBack);
        assertThat(kycCaseBack.getCustomer()).isEqualTo(customer);

        customer.removeKycCase(kycCaseBack);
        assertThat(customer.getKycCases()).doesNotContain(kycCaseBack);
        assertThat(kycCaseBack.getCustomer()).isNull();

        customer.kycCases(new HashSet<>(Set.of(kycCaseBack)));
        assertThat(customer.getKycCases()).containsOnly(kycCaseBack);
        assertThat(kycCaseBack.getCustomer()).isEqualTo(customer);

        customer.setKycCases(new HashSet<>());
        assertThat(customer.getKycCases()).doesNotContain(kycCaseBack);
        assertThat(kycCaseBack.getCustomer()).isNull();
    }
}
