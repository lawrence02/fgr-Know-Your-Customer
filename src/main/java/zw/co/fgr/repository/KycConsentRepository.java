package zw.co.fgr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.fgr.domain.KycConsent;

/**
 * Spring Data JPA repository for the KycConsent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycConsentRepository extends JpaRepository<KycConsent, Long> {}
