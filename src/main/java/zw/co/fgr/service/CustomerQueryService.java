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
import zw.co.fgr.domain.Customer;
import zw.co.fgr.repository.CustomerRepository;
import zw.co.fgr.service.criteria.CustomerCriteria;

/**
 * Service for executing complex queries for {@link Customer} entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Customer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    public CustomerQueryService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Return a {@link Page} of {@link Customer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Customer> findByCriteria(CustomerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Customer_.id),
                buildStringSpecification(criteria.getCustomerRef(), Customer_.customerRef),
                buildSpecification(criteria.getCustomerType(), Customer_.customerType),
                buildStringSpecification(criteria.getFullName(), Customer_.fullName),
                buildRangeSpecification(criteria.getDateOfBirth(), Customer_.dateOfBirth),
                buildStringSpecification(criteria.getIdNumber(), Customer_.idNumber),
                buildStringSpecification(criteria.getRegistrationNumber(), Customer_.registrationNumber),
                buildStringSpecification(criteria.getAddress(), Customer_.address),
                buildStringSpecification(criteria.getPhoneNumber(), Customer_.phoneNumber),
                buildRangeSpecification(criteria.getCreatedAt(), Customer_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Customer_.updatedAt),
                buildSpecification(criteria.getCustomerIdentifierId(), root ->
                    root.join(Customer_.customerIdentifiers, JoinType.LEFT).get(CustomerIdentifier_.id)
                ),
                buildSpecification(criteria.getKycCaseId(), root -> root.join(Customer_.kycCases, JoinType.LEFT).get(KycCase_.id))
            );
        }
        return specification;
    }
}
