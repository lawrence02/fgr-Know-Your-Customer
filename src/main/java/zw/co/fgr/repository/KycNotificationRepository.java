package zw.co.fgr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zw.co.fgr.domain.KycNotification;

/**
 * Spring Data JPA repository for the KycNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycNotificationRepository extends JpaRepository<KycNotification, Long> {}
