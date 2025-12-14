package zw.co.fgr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerIdentifierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static CustomerIdentifier getCustomerIdentifierSample1() {
        return new CustomerIdentifier().id(1L).identifierValue("identifierValue1");
    }

    public static CustomerIdentifier getCustomerIdentifierSample2() {
        return new CustomerIdentifier().id(2L).identifierValue("identifierValue2");
    }

    public static CustomerIdentifier getCustomerIdentifierRandomSampleGenerator() {
        return new CustomerIdentifier().id(longCount.incrementAndGet()).identifierValue(UUID.randomUUID().toString());
    }
}
