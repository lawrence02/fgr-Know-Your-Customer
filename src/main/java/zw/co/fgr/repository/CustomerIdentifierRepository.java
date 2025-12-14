package zw.co.fgr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.fgr.domain.CustomerIdentifier;

/**
 * Spring Data JPA repository for the CustomerIdentifier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerIdentifierRepository
    extends JpaRepository<CustomerIdentifier, Long>, JpaSpecificationExecutor<CustomerIdentifier> {}
