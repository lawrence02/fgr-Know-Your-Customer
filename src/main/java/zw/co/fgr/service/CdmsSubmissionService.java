package zw.co.fgr.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.domain.CdmsSubmission;
import zw.co.fgr.repository.CdmsSubmissionRepository;

/**
 * Service Implementation for managing {@link zw.co.fgr.domain.CdmsSubmission}.
 */
@Service
@Transactional
public class CdmsSubmissionService {

    private static final Logger LOG = LoggerFactory.getLogger(CdmsSubmissionService.class);

    private final CdmsSubmissionRepository cdmsSubmissionRepository;

    public CdmsSubmissionService(CdmsSubmissionRepository cdmsSubmissionRepository) {
        this.cdmsSubmissionRepository = cdmsSubmissionRepository;
    }

    /**
     * Save a cdmsSubmission.
     *
     * @param cdmsSubmission the entity to save.
     * @return the persisted entity.
     */
    public CdmsSubmission save(CdmsSubmission cdmsSubmission) {
        LOG.debug("Request to save CdmsSubmission : {}", cdmsSubmission);
        return cdmsSubmissionRepository.save(cdmsSubmission);
    }

    /**
     * Update a cdmsSubmission.
     *
     * @param cdmsSubmission the entity to save.
     * @return the persisted entity.
     */
    public CdmsSubmission update(CdmsSubmission cdmsSubmission) {
        LOG.debug("Request to update CdmsSubmission : {}", cdmsSubmission);
        return cdmsSubmissionRepository.save(cdmsSubmission);
    }

    /**
     * Partially update a cdmsSubmission.
     *
     * @param cdmsSubmission the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CdmsSubmission> partialUpdate(CdmsSubmission cdmsSubmission) {
        LOG.debug("Request to partially update CdmsSubmission : {}", cdmsSubmission);

        return cdmsSubmissionRepository
            .findById(cdmsSubmission.getId())
            .map(existingCdmsSubmission -> {
                if (cdmsSubmission.getSubmissionRef() != null) {
                    existingCdmsSubmission.setSubmissionRef(cdmsSubmission.getSubmissionRef());
                }
                if (cdmsSubmission.getStatus() != null) {
                    existingCdmsSubmission.setStatus(cdmsSubmission.getStatus());
                }
                if (cdmsSubmission.getResponseCode() != null) {
                    existingCdmsSubmission.setResponseCode(cdmsSubmission.getResponseCode());
                }
                if (cdmsSubmission.getResponseMessage() != null) {
                    existingCdmsSubmission.setResponseMessage(cdmsSubmission.getResponseMessage());
                }
                if (cdmsSubmission.getAttempts() != null) {
                    existingCdmsSubmission.setAttempts(cdmsSubmission.getAttempts());
                }
                if (cdmsSubmission.getSubmittedAt() != null) {
                    existingCdmsSubmission.setSubmittedAt(cdmsSubmission.getSubmittedAt());
                }
                if (cdmsSubmission.getLastAttemptAt() != null) {
                    existingCdmsSubmission.setLastAttemptAt(cdmsSubmission.getLastAttemptAt());
                }
                if (cdmsSubmission.getNextRetryAt() != null) {
                    existingCdmsSubmission.setNextRetryAt(cdmsSubmission.getNextRetryAt());
                }
                if (cdmsSubmission.getCdmsCustomerId() != null) {
                    existingCdmsSubmission.setCdmsCustomerId(cdmsSubmission.getCdmsCustomerId());
                }

                return existingCdmsSubmission;
            })
            .map(cdmsSubmissionRepository::save);
    }

    /**
     *  Get all the cdmsSubmissions where KycCase is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CdmsSubmission> findAllWhereKycCaseIsNull() {
        LOG.debug("Request to get all cdmsSubmissions where KycCase is null");
        return StreamSupport.stream(cdmsSubmissionRepository.findAll().spliterator(), false)
            .filter(cdmsSubmission -> cdmsSubmission.getKycCase() == null)
            .toList();
    }

    /**
     * Get one cdmsSubmission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CdmsSubmission> findOne(Long id) {
        LOG.debug("Request to get CdmsSubmission : {}", id);
        return cdmsSubmissionRepository.findById(id);
    }

    /**
     * Delete the cdmsSubmission by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CdmsSubmission : {}", id);
        cdmsSubmissionRepository.deleteById(id);
    }
}
