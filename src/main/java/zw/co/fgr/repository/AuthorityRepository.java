package zw.co.fgr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.fgr.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
