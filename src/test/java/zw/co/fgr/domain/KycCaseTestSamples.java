package zw.co.fgr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KycCaseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static KycCase getKycCaseSample1() {
        return new KycCase().id(1L).kycRef("kycRef1");
    }

    public static KycCase getKycCaseSample2() {
        return new KycCase().id(2L).kycRef("kycRef2");
    }

    public static KycCase getKycCaseRandomSampleGenerator() {
        return new KycCase().id(longCount.incrementAndGet()).kycRef(UUID.randomUUID().toString());
    }
}
