package zw.co.fgr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zw.co.fgr.domain.CustomerIdentifierTestSamples.*;
import static zw.co.fgr.domain.CustomerTestSamples.*;

import org.junit.jupiter.api.Test;
import zw.co.fgr.web.rest.TestUtil;

class CustomerIdentifierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerIdentifier.class);
        CustomerIdentifier customerIdentifier1 = getCustomerIdentifierSample1();
        CustomerIdentifier customerIdentifier2 = new CustomerIdentifier();
        assertThat(customerIdentifier1).isNotEqualTo(customerIdentifier2);

        customerIdentifier2.setId(customerIdentifier1.getId());
        assertThat(customerIdentifier1).isEqualTo(customerIdentifier2);

        customerIdentifier2 = getCustomerIdentifierSample2();
        assertThat(customerIdentifier1).isNotEqualTo(customerIdentifier2);
    }

    @Test
    void customerTest() {
        CustomerIdentifier customerIdentifier = getCustomerIdentifierRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        customerIdentifier.setCustomer(customerBack);
        assertThat(customerIdentifier.getCustomer()).isEqualTo(customerBack);

        customerIdentifier.customer(null);
        assertThat(customerIdentifier.getCustomer()).isNull();
    }
}
