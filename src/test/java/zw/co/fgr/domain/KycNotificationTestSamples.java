package zw.co.fgr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KycNotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static KycNotification getKycNotificationSample1() {
        return new KycNotification().id(1L).message("message1").errorMessage("errorMessage1");
    }

    public static KycNotification getKycNotificationSample2() {
        return new KycNotification().id(2L).message("message2").errorMessage("errorMessage2");
    }

    public static KycNotification getKycNotificationRandomSampleGenerator() {
        return new KycNotification()
            .id(longCount.incrementAndGet())
            .message(UUID.randomUUID().toString())
            .errorMessage(UUID.randomUUID().toString());
    }
}
