package zw.co.fgr.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import zw.co.fgr.domain.*; // for static metamodels
import zw.co.fgr.domain.KycCase;
import zw.co.fgr.repository.KycCaseRepository;
import zw.co.fgr.service.criteria.KycCaseCriteria;

/**
 * Service for executing complex queries for {@link KycCase} entities in the database.
 * The main input is a {@link KycCaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link KycCase} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KycCaseQueryService extends QueryService<KycCase> {

    private static final Logger LOG = LoggerFactory.getLogger(KycCaseQueryService.class);

    private final KycCaseRepository kycCaseRepository;

    public KycCaseQueryService(KycCaseRepository kycCaseRepository) {
        this.kycCaseRepository = kycCaseRepository;
    }

    /**
     * Return a {@link Page} of {@link KycCase} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KycCase> findByCriteria(KycCaseCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<KycCase> specification = createSpecification(criteria);
        return kycCaseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KycCaseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<KycCase> specification = createSpecification(criteria);
        return kycCaseRepository.count(specification);
    }

    /**
     * Function to convert {@link KycCaseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<KycCase> createSpecification(KycCaseCriteria criteria) {
        Specification<KycCase> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), KycCase_.id),
                buildStringSpecification(criteria.getKycRef(), KycCase_.kycRef),
                buildSpecification(criteria.getStatus(), KycCase_.status),
                buildSpecification(criteria.getChannel(), KycCase_.channel),
                buildRangeSpecification(criteria.getStartedAt(), KycCase_.startedAt),
                buildRangeSpecification(criteria.getLastActivityAt(), KycCase_.lastActivityAt),
                buildRangeSpecification(criteria.getLastUpdatedAt(), KycCase_.lastUpdatedAt),
                buildRangeSpecification(criteria.getCompletedAt(), KycCase_.completedAt),
                buildRangeSpecification(criteria.getExpiresAt(), KycCase_.expiresAt),
                buildSpecification(criteria.getConsentId(), root -> root.join(KycCase_.consent, JoinType.LEFT).get(KycConsent_.id)),
                buildSpecification(criteria.getSubmissionId(), root ->
                    root.join(KycCase_.submission, JoinType.LEFT).get(CdmsSubmission_.id)
                ),
                buildSpecification(criteria.getKycDocumentId(), root ->
                    root.join(KycCase_.kycDocuments, JoinType.LEFT).get(KycDocument_.id)
                ),
                buildSpecification(criteria.getKycNotificationId(), root ->
                    root.join(KycCase_.kycNotifications, JoinType.LEFT).get(KycNotification_.id)
                ),
                buildSpecification(criteria.getCustomerId(), root -> root.join(KycCase_.customer, JoinType.LEFT).get(Customer_.id))
            );
        }
        return specification;
    }
}
