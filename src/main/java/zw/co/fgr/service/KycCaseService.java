package zw.co.fgr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.domain.KycCase;
import zw.co.fgr.repository.KycCaseRepository;

/**
 * Service Implementation for managing {@link zw.co.fgr.domain.KycCase}.
 */
@Service
@Transactional
public class KycCaseService {

    private static final Logger LOG = LoggerFactory.getLogger(KycCaseService.class);

    private final KycCaseRepository kycCaseRepository;

    public KycCaseService(KycCaseRepository kycCaseRepository) {
        this.kycCaseRepository = kycCaseRepository;
    }

    /**
     * Save a kycCase.
     *
     * @param kycCase the entity to save.
     * @return the persisted entity.
     */
    public KycCase save(KycCase kycCase) {
        LOG.debug("Request to save KycCase : {}", kycCase);
        return kycCaseRepository.save(kycCase);
    }

    /**
     * Update a kycCase.
     *
     * @param kycCase the entity to save.
     * @return the persisted entity.
     */
    public KycCase update(KycCase kycCase) {
        LOG.debug("Request to update KycCase : {}", kycCase);
        return kycCaseRepository.save(kycCase);
    }

    /**
     * Partially update a kycCase.
     *
     * @param kycCase the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KycCase> partialUpdate(KycCase kycCase) {
        LOG.debug("Request to partially update KycCase : {}", kycCase);

        return kycCaseRepository
            .findById(kycCase.getId())
            .map(existingKycCase -> {
                if (kycCase.getKycRef() != null) {
                    existingKycCase.setKycRef(kycCase.getKycRef());
                }
                if (kycCase.getStatus() != null) {
                    existingKycCase.setStatus(kycCase.getStatus());
                }
                if (kycCase.getChannel() != null) {
                    existingKycCase.setChannel(kycCase.getChannel());
                }
                if (kycCase.getStartedAt() != null) {
                    existingKycCase.setStartedAt(kycCase.getStartedAt());
                }
                if (kycCase.getLastActivityAt() != null) {
                    existingKycCase.setLastActivityAt(kycCase.getLastActivityAt());
                }
                if (kycCase.getLastUpdatedAt() != null) {
                    existingKycCase.setLastUpdatedAt(kycCase.getLastUpdatedAt());
                }
                if (kycCase.getCompletedAt() != null) {
                    existingKycCase.setCompletedAt(kycCase.getCompletedAt());
                }
                if (kycCase.getExpiresAt() != null) {
                    existingKycCase.setExpiresAt(kycCase.getExpiresAt());
                }
                if (kycCase.getValidationErrors() != null) {
                    existingKycCase.setValidationErrors(kycCase.getValidationErrors());
                }
                if (kycCase.getInternalNotes() != null) {
                    existingKycCase.setInternalNotes(kycCase.getInternalNotes());
                }

                return existingKycCase;
            })
            .map(kycCaseRepository::save);
    }

    /**
     * Get one kycCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KycCase> findOne(Long id) {
        LOG.debug("Request to get KycCase : {}", id);
        return kycCaseRepository.findById(id);
    }

    /**
     * Delete the kycCase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KycCase : {}", id);
        kycCaseRepository.deleteById(id);
    }
}
