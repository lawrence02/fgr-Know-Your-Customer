package zw.co.fgr.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import zw.co.fgr.domain.*; // for static metamodels
import zw.co.fgr.domain.CdmsSubmission;
import zw.co.fgr.repository.CdmsSubmissionRepository;
import zw.co.fgr.service.criteria.CdmsSubmissionCriteria;

/**
 * Service for executing complex queries for {@link CdmsSubmission} entities in the database.
 * The main input is a {@link CdmsSubmissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CdmsSubmission} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CdmsSubmissionQueryService extends QueryService<CdmsSubmission> {

    private static final Logger LOG = LoggerFactory.getLogger(CdmsSubmissionQueryService.class);

    private final CdmsSubmissionRepository cdmsSubmissionRepository;

    public CdmsSubmissionQueryService(CdmsSubmissionRepository cdmsSubmissionRepository) {
        this.cdmsSubmissionRepository = cdmsSubmissionRepository;
    }

    /**
     * Return a {@link List} of {@link CdmsSubmission} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CdmsSubmission> findByCriteria(CdmsSubmissionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CdmsSubmission> specification = createSpecification(criteria);
        return cdmsSubmissionRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CdmsSubmissionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CdmsSubmission> specification = createSpecification(criteria);
        return cdmsSubmissionRepository.count(specification);
    }

    /**
     * Function to convert {@link CdmsSubmissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CdmsSubmission> createSpecification(CdmsSubmissionCriteria criteria) {
        Specification<CdmsSubmission> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CdmsSubmission_.id),
                buildStringSpecification(criteria.getSubmissionRef(), CdmsSubmission_.submissionRef),
                buildSpecification(criteria.getStatus(), CdmsSubmission_.status),
                buildStringSpecification(criteria.getResponseCode(), CdmsSubmission_.responseCode),
                buildStringSpecification(criteria.getResponseMessage(), CdmsSubmission_.responseMessage),
                buildRangeSpecification(criteria.getAttempts(), CdmsSubmission_.attempts),
                buildRangeSpecification(criteria.getSubmittedAt(), CdmsSubmission_.submittedAt),
                buildRangeSpecification(criteria.getLastAttemptAt(), CdmsSubmission_.lastAttemptAt),
                buildRangeSpecification(criteria.getNextRetryAt(), CdmsSubmission_.nextRetryAt),
                buildStringSpecification(criteria.getCdmsCustomerId(), CdmsSubmission_.cdmsCustomerId),
                buildSpecification(criteria.getKycCaseId(), root -> root.join(CdmsSubmission_.kycCase, JoinType.LEFT).get(KycCase_.id))
            );
        }
        return specification;
    }
}
