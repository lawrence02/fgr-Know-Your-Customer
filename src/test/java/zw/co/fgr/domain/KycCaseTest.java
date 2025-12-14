package zw.co.fgr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zw.co.fgr.domain.CdmsSubmissionTestSamples.*;
import static zw.co.fgr.domain.CustomerTestSamples.*;
import static zw.co.fgr.domain.KycCaseTestSamples.*;
import static zw.co.fgr.domain.KycConsentTestSamples.*;
import static zw.co.fgr.domain.KycDocumentTestSamples.*;
import static zw.co.fgr.domain.KycNotificationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import zw.co.fgr.web.rest.TestUtil;

class KycCaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycCase.class);
        KycCase kycCase1 = getKycCaseSample1();
        KycCase kycCase2 = new KycCase();
        assertThat(kycCase1).isNotEqualTo(kycCase2);

        kycCase2.setId(kycCase1.getId());
        assertThat(kycCase1).isEqualTo(kycCase2);

        kycCase2 = getKycCaseSample2();
        assertThat(kycCase1).isNotEqualTo(kycCase2);
    }

    @Test
    void consentTest() {
        KycCase kycCase = getKycCaseRandomSampleGenerator();
        KycConsent kycConsentBack = getKycConsentRandomSampleGenerator();

        kycCase.setConsent(kycConsentBack);
        assertThat(kycCase.getConsent()).isEqualTo(kycConsentBack);

        kycCase.consent(null);
        assertThat(kycCase.getConsent()).isNull();
    }

    @Test
    void submissionTest() {
        KycCase kycCase = getKycCaseRandomSampleGenerator();
        CdmsSubmission cdmsSubmissionBack = getCdmsSubmissionRandomSampleGenerator();

        kycCase.setSubmission(cdmsSubmissionBack);
        assertThat(kycCase.getSubmission()).isEqualTo(cdmsSubmissionBack);

        kycCase.submission(null);
        assertThat(kycCase.getSubmission()).isNull();
    }

    @Test
    void kycDocumentTest() {
        KycCase kycCase = getKycCaseRandomSampleGenerator();
        KycDocument kycDocumentBack = getKycDocumentRandomSampleGenerator();

        kycCase.addKycDocument(kycDocumentBack);
        assertThat(kycCase.getKycDocuments()).containsOnly(kycDocumentBack);
        assertThat(kycDocumentBack.getKycCase()).isEqualTo(kycCase);

        kycCase.removeKycDocument(kycDocumentBack);
        assertThat(kycCase.getKycDocuments()).doesNotContain(kycDocumentBack);
        assertThat(kycDocumentBack.getKycCase()).isNull();

        kycCase.kycDocuments(new HashSet<>(Set.of(kycDocumentBack)));
        assertThat(kycCase.getKycDocuments()).containsOnly(kycDocumentBack);
        assertThat(kycDocumentBack.getKycCase()).isEqualTo(kycCase);

        kycCase.setKycDocuments(new HashSet<>());
        assertThat(kycCase.getKycDocuments()).doesNotContain(kycDocumentBack);
        assertThat(kycDocumentBack.getKycCase()).isNull();
    }

    @Test
    void kycNotificationTest() {
        KycCase kycCase = getKycCaseRandomSampleGenerator();
        KycNotification kycNotificationBack = getKycNotificationRandomSampleGenerator();

        kycCase.addKycNotification(kycNotificationBack);
        assertThat(kycCase.getKycNotifications()).containsOnly(kycNotificationBack);
        assertThat(kycNotificationBack.getKycCase()).isEqualTo(kycCase);

        kycCase.removeKycNotification(kycNotificationBack);
        assertThat(kycCase.getKycNotifications()).doesNotContain(kycNotificationBack);
        assertThat(kycNotificationBack.getKycCase()).isNull();

        kycCase.kycNotifications(new HashSet<>(Set.of(kycNotificationBack)));
        assertThat(kycCase.getKycNotifications()).containsOnly(kycNotificationBack);
        assertThat(kycNotificationBack.getKycCase()).isEqualTo(kycCase);

        kycCase.setKycNotifications(new HashSet<>());
        assertThat(kycCase.getKycNotifications()).doesNotContain(kycNotificationBack);
        assertThat(kycNotificationBack.getKycCase()).isNull();
    }

    @Test
    void customerTest() {
        KycCase kycCase = getKycCaseRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        kycCase.setCustomer(customerBack);
        assertThat(kycCase.getCustomer()).isEqualTo(customerBack);

        kycCase.customer(null);
        assertThat(kycCase.getCustomer()).isNull();
    }
}
