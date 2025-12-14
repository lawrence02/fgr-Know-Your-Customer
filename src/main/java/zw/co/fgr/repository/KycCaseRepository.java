package zw.co.fgr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.fgr.domain.KycCase;

/**
 * Spring Data JPA repository for the KycCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycCaseRepository extends JpaRepository<KycCase, Long>, JpaSpecificationExecutor<KycCase> {}
