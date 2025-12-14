package zw.co.fgr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KycDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static KycDocument getKycDocumentSample1() {
        return new KycDocument()
            .id(1L)
            .fileName("fileName1")
            .mimeType("mimeType1")
            .storagePath("storagePath1")
            .fileSize(1L)
            .checksum("checksum1");
    }

    public static KycDocument getKycDocumentSample2() {
        return new KycDocument()
            .id(2L)
            .fileName("fileName2")
            .mimeType("mimeType2")
            .storagePath("storagePath2")
            .fileSize(2L)
            .checksum("checksum2");
    }

    public static KycDocument getKycDocumentRandomSampleGenerator() {
        return new KycDocument()
            .id(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .mimeType(UUID.randomUUID().toString())
            .storagePath(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .checksum(UUID.randomUUID().toString());
    }
}
