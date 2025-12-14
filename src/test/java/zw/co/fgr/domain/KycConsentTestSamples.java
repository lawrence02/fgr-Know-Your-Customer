package zw.co.fgr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KycConsentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static KycConsent getKycConsentSample1() {
        return new KycConsent()
            .id(1L)
            .consentText("consentText1")
            .ipAddress("ipAddress1")
            .userAgent("userAgent1")
            .consentVersion("consentVersion1");
    }

    public static KycConsent getKycConsentSample2() {
        return new KycConsent()
            .id(2L)
            .consentText("consentText2")
            .ipAddress("ipAddress2")
            .userAgent("userAgent2")
            .consentVersion("consentVersion2");
    }

    public static KycConsent getKycConsentRandomSampleGenerator() {
        return new KycConsent()
            .id(longCount.incrementAndGet())
            .consentText(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString())
            .userAgent(UUID.randomUUID().toString())
            .consentVersion(UUID.randomUUID().toString());
    }
}
