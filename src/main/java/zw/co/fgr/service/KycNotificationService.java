package zw.co.fgr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.domain.KycNotification;
import zw.co.fgr.repository.KycNotificationRepository;

/**
 * Service Implementation for managing {@link zw.co.fgr.domain.KycNotification}.
 */
@Service
@Transactional
public class KycNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(KycNotificationService.class);

    private final KycNotificationRepository kycNotificationRepository;

    public KycNotificationService(KycNotificationRepository kycNotificationRepository) {
        this.kycNotificationRepository = kycNotificationRepository;
    }

    /**
     * Save a kycNotification.
     *
     * @param kycNotification the entity to save.
     * @return the persisted entity.
     */
    public KycNotification save(KycNotification kycNotification) {
        LOG.debug("Request to save KycNotification : {}", kycNotification);
        return kycNotificationRepository.save(kycNotification);
    }

    /**
     * Update a kycNotification.
     *
     * @param kycNotification the entity to save.
     * @return the persisted entity.
     */
    public KycNotification update(KycNotification kycNotification) {
        LOG.debug("Request to update KycNotification : {}", kycNotification);
        return kycNotificationRepository.save(kycNotification);
    }

    /**
     * Partially update a kycNotification.
     *
     * @param kycNotification the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KycNotification> partialUpdate(KycNotification kycNotification) {
        LOG.debug("Request to partially update KycNotification : {}", kycNotification);

        return kycNotificationRepository
            .findById(kycNotification.getId())
            .map(existingKycNotification -> {
                if (kycNotification.getNotificationType() != null) {
                    existingKycNotification.setNotificationType(kycNotification.getNotificationType());
                }
                if (kycNotification.getMessage() != null) {
                    existingKycNotification.setMessage(kycNotification.getMessage());
                }
                if (kycNotification.getSentAt() != null) {
                    existingKycNotification.setSentAt(kycNotification.getSentAt());
                }
                if (kycNotification.getDelivered() != null) {
                    existingKycNotification.setDelivered(kycNotification.getDelivered());
                }
                if (kycNotification.getDeliveredAt() != null) {
                    existingKycNotification.setDeliveredAt(kycNotification.getDeliveredAt());
                }
                if (kycNotification.getErrorMessage() != null) {
                    existingKycNotification.setErrorMessage(kycNotification.getErrorMessage());
                }

                return existingKycNotification;
            })
            .map(kycNotificationRepository::save);
    }

    /**
     * Get all the kycNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<KycNotification> findAll(Pageable pageable) {
        LOG.debug("Request to get all KycNotifications");
        return kycNotificationRepository.findAll(pageable);
    }

    /**
     * Get one kycNotification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KycNotification> findOne(Long id) {
        LOG.debug("Request to get KycNotification : {}", id);
        return kycNotificationRepository.findById(id);
    }

    /**
     * Delete the kycNotification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KycNotification : {}", id);
        kycNotificationRepository.deleteById(id);
    }
}
