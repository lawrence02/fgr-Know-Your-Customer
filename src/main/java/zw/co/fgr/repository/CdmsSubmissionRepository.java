package zw.co.fgr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.fgr.domain.CdmsSubmission;

/**
 * Spring Data JPA repository for the CdmsSubmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CdmsSubmissionRepository extends JpaRepository<CdmsSubmission, Long>, JpaSpecificationExecutor<CdmsSubmission> {}
