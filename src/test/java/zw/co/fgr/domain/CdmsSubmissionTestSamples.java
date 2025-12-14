package zw.co.fgr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CdmsSubmissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CdmsSubmission getCdmsSubmissionSample1() {
        return new CdmsSubmission()
            .id(1L)
            .submissionRef("submissionRef1")
            .responseCode("responseCode1")
            .responseMessage("responseMessage1")
            .attempts(1)
            .cdmsCustomerId("cdmsCustomerId1");
    }

    public static CdmsSubmission getCdmsSubmissionSample2() {
        return new CdmsSubmission()
            .id(2L)
            .submissionRef("submissionRef2")
            .responseCode("responseCode2")
            .responseMessage("responseMessage2")
            .attempts(2)
            .cdmsCustomerId("cdmsCustomerId2");
    }

    public static CdmsSubmission getCdmsSubmissionRandomSampleGenerator() {
        return new CdmsSubmission()
            .id(longCount.incrementAndGet())
            .submissionRef(UUID.randomUUID().toString())
            .responseCode(UUID.randomUUID().toString())
            .responseMessage(UUID.randomUUID().toString())
            .attempts(intCount.incrementAndGet())
            .cdmsCustomerId(UUID.randomUUID().toString());
    }
}
