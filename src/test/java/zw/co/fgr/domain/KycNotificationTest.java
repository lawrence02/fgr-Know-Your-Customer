package zw.co.fgr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zw.co.fgr.domain.KycCaseTestSamples.*;
import static zw.co.fgr.domain.KycNotificationTestSamples.*;

import org.junit.jupiter.api.Test;
import zw.co.fgr.web.rest.TestUtil;

class KycNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycNotification.class);
        KycNotification kycNotification1 = getKycNotificationSample1();
        KycNotification kycNotification2 = new KycNotification();
        assertThat(kycNotification1).isNotEqualTo(kycNotification2);

        kycNotification2.setId(kycNotification1.getId());
        assertThat(kycNotification1).isEqualTo(kycNotification2);

        kycNotification2 = getKycNotificationSample2();
        assertThat(kycNotification1).isNotEqualTo(kycNotification2);
    }

    @Test
    void kycCaseTest() {
        KycNotification kycNotification = getKycNotificationRandomSampleGenerator();
        KycCase kycCaseBack = getKycCaseRandomSampleGenerator();

        kycNotification.setKycCase(kycCaseBack);
        assertThat(kycNotification.getKycCase()).isEqualTo(kycCaseBack);

        kycNotification.kycCase(null);
        assertThat(kycNotification.getKycCase()).isNull();
    }
}
