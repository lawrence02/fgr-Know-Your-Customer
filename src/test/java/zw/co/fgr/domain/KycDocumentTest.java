package zw.co.fgr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zw.co.fgr.domain.KycCaseTestSamples.*;
import static zw.co.fgr.domain.KycDocumentTestSamples.*;

import org.junit.jupiter.api.Test;
import zw.co.fgr.web.rest.TestUtil;

class KycDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycDocument.class);
        KycDocument kycDocument1 = getKycDocumentSample1();
        KycDocument kycDocument2 = new KycDocument();
        assertThat(kycDocument1).isNotEqualTo(kycDocument2);

        kycDocument2.setId(kycDocument1.getId());
        assertThat(kycDocument1).isEqualTo(kycDocument2);

        kycDocument2 = getKycDocumentSample2();
        assertThat(kycDocument1).isNotEqualTo(kycDocument2);
    }

    @Test
    void kycCaseTest() {
        KycDocument kycDocument = getKycDocumentRandomSampleGenerator();
        KycCase kycCaseBack = getKycCaseRandomSampleGenerator();

        kycDocument.setKycCase(kycCaseBack);
        assertThat(kycDocument.getKycCase()).isEqualTo(kycCaseBack);

        kycDocument.kycCase(null);
        assertThat(kycDocument.getKycCase()).isNull();
    }
}
