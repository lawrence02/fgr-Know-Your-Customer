package zw.co.fgr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .customerRef("customerRef1")
            .fullName("fullName1")
            .idNumber("idNumber1")
            .registrationNumber("registrationNumber1")
            .address("address1")
            .phoneNumber("phoneNumber1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .customerRef("customerRef2")
            .fullName("fullName2")
            .idNumber("idNumber2")
            .registrationNumber("registrationNumber2")
            .address("address2")
            .phoneNumber("phoneNumber2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .customerRef(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .idNumber(UUID.randomUUID().toString())
            .registrationNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
