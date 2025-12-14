package zw.co.fgr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zw.co.fgr.domain.KycCaseTestSamples.*;
import static zw.co.fgr.domain.KycConsentTestSamples.*;

import org.junit.jupiter.api.Test;
import zw.co.fgr.web.rest.TestUtil;

class KycConsentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycConsent.class);
        KycConsent kycConsent1 = getKycConsentSample1();
        KycConsent kycConsent2 = new KycConsent();
        assertThat(kycConsent1).isNotEqualTo(kycConsent2);

        kycConsent2.setId(kycConsent1.getId());
        assertThat(kycConsent1).isEqualTo(kycConsent2);

        kycConsent2 = getKycConsentSample2();
        assertThat(kycConsent1).isNotEqualTo(kycConsent2);
    }

    @Test
    void kycCaseTest() {
        KycConsent kycConsent = getKycConsentRandomSampleGenerator();
        KycCase kycCaseBack = getKycCaseRandomSampleGenerator();

        kycConsent.setKycCase(kycCaseBack);
        assertThat(kycConsent.getKycCase()).isEqualTo(kycCaseBack);
        assertThat(kycCaseBack.getConsent()).isEqualTo(kycConsent);

        kycConsent.kycCase(null);
        assertThat(kycConsent.getKycCase()).isNull();
        assertThat(kycCaseBack.getConsent()).isNull();
    }
}
