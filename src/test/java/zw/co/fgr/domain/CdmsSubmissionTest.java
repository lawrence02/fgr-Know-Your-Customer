package zw.co.fgr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zw.co.fgr.domain.CdmsSubmissionTestSamples.*;
import static zw.co.fgr.domain.KycCaseTestSamples.*;

import org.junit.jupiter.api.Test;
import zw.co.fgr.web.rest.TestUtil;

class CdmsSubmissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CdmsSubmission.class);
        CdmsSubmission cdmsSubmission1 = getCdmsSubmissionSample1();
        CdmsSubmission cdmsSubmission2 = new CdmsSubmission();
        assertThat(cdmsSubmission1).isNotEqualTo(cdmsSubmission2);

        cdmsSubmission2.setId(cdmsSubmission1.getId());
        assertThat(cdmsSubmission1).isEqualTo(cdmsSubmission2);

        cdmsSubmission2 = getCdmsSubmissionSample2();
        assertThat(cdmsSubmission1).isNotEqualTo(cdmsSubmission2);
    }

    @Test
    void kycCaseTest() {
        CdmsSubmission cdmsSubmission = getCdmsSubmissionRandomSampleGenerator();
        KycCase kycCaseBack = getKycCaseRandomSampleGenerator();

        cdmsSubmission.setKycCase(kycCaseBack);
        assertThat(cdmsSubmission.getKycCase()).isEqualTo(kycCaseBack);
        assertThat(kycCaseBack.getSubmission()).isEqualTo(cdmsSubmission);

        cdmsSubmission.kycCase(null);
        assertThat(cdmsSubmission.getKycCase()).isNull();
        assertThat(kycCaseBack.getSubmission()).isNull();
    }
}
