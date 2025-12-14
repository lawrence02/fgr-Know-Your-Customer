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
import zw.co.fgr.domain.CustomerIdentifier;
import zw.co.fgr.repository.CustomerIdentifierRepository;
import zw.co.fgr.service.criteria.CustomerIdentifierCriteria;

/**
 * Service for executing complex queries for {@link CustomerIdentifier} entities in the database.
 * The main input is a {@link CustomerIdentifierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerIdentifier} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerIdentifierQueryService extends QueryService<CustomerIdentifier> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerIdentifierQueryService.class);

    private final CustomerIdentifierRepository customerIdentifierRepository;

    public CustomerIdentifierQueryService(CustomerIdentifierRepository customerIdentifierRepository) {
        this.customerIdentifierRepository = customerIdentifierRepository;
    }

    /**
     * Return a {@link Page} of {@link CustomerIdentifier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerIdentifier> findByCriteria(CustomerIdentifierCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerIdentifier> specification = createSpecification(criteria);
        return customerIdentifierRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerIdentifierCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerIdentifier> specification = createSpecification(criteria);
        return customerIdentifierRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerIdentifierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerIdentifier> createSpecification(CustomerIdentifierCriteria criteria) {
        Specification<CustomerIdentifier> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CustomerIdentifier_.id),
                buildSpecification(criteria.getIdentifierType(), CustomerIdentifier_.identifierType),
                buildStringSpecification(criteria.getIdentifierValue(), CustomerIdentifier_.identifierValue),
                buildSpecification(criteria.getChannel(), CustomerIdentifier_.channel),
                buildSpecification(criteria.getVerified(), CustomerIdentifier_.verified),
                buildSpecification(criteria.getIsPrimary(), CustomerIdentifier_.isPrimary),
                buildRangeSpecification(criteria.getCreatedAt(), CustomerIdentifier_.createdAt),
                buildRangeSpecification(criteria.getVerifiedAt(), CustomerIdentifier_.verifiedAt),
                buildSpecification(criteria.getCustomerId(), root ->
                    root.join(CustomerIdentifier_.customer, JoinType.LEFT).get(Customer_.id)
                )
            );
        }
        return specification;
    }
}
