package zw.co.fgr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.fgr.domain.KycDocument;

/**
 * Spring Data JPA repository for the KycDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, Long> {}
