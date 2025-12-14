package zw.co.fgr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.domain.CustomerIdentifier;
import zw.co.fgr.repository.CustomerIdentifierRepository;

/**
 * Service Implementation for managing {@link zw.co.fgr.domain.CustomerIdentifier}.
 */
@Service
@Transactional
public class CustomerIdentifierService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerIdentifierService.class);

    private final CustomerIdentifierRepository customerIdentifierRepository;

    public CustomerIdentifierService(CustomerIdentifierRepository customerIdentifierRepository) {
        this.customerIdentifierRepository = customerIdentifierRepository;
    }

    /**
     * Save a customerIdentifier.
     *
     * @param customerIdentifier the entity to save.
     * @return the persisted entity.
     */
    public CustomerIdentifier save(CustomerIdentifier customerIdentifier) {
        LOG.debug("Request to save CustomerIdentifier : {}", customerIdentifier);
        return customerIdentifierRepository.save(customerIdentifier);
    }

    /**
     * Update a customerIdentifier.
     *
     * @param customerIdentifier the entity to save.
     * @return the persisted entity.
     */
    public CustomerIdentifier update(CustomerIdentifier customerIdentifier) {
        LOG.debug("Request to update CustomerIdentifier : {}", customerIdentifier);
        return customerIdentifierRepository.save(customerIdentifier);
    }

    /**
     * Partially update a customerIdentifier.
     *
     * @param customerIdentifier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerIdentifier> partialUpdate(CustomerIdentifier customerIdentifier) {
        LOG.debug("Request to partially update CustomerIdentifier : {}", customerIdentifier);

        return customerIdentifierRepository
            .findById(customerIdentifier.getId())
            .map(existingCustomerIdentifier -> {
                if (customerIdentifier.getIdentifierType() != null) {
                    existingCustomerIdentifier.setIdentifierType(customerIdentifier.getIdentifierType());
                }
                if (customerIdentifier.getIdentifierValue() != null) {
                    existingCustomerIdentifier.setIdentifierValue(customerIdentifier.getIdentifierValue());
                }
                if (customerIdentifier.getChannel() != null) {
                    existingCustomerIdentifier.setChannel(customerIdentifier.getChannel());
                }
                if (customerIdentifier.getVerified() != null) {
                    existingCustomerIdentifier.setVerified(customerIdentifier.getVerified());
                }
                if (customerIdentifier.getIsPrimary() != null) {
                    existingCustomerIdentifier.setIsPrimary(customerIdentifier.getIsPrimary());
                }
                if (customerIdentifier.getCreatedAt() != null) {
                    existingCustomerIdentifier.setCreatedAt(customerIdentifier.getCreatedAt());
                }
                if (customerIdentifier.getVerifiedAt() != null) {
                    existingCustomerIdentifier.setVerifiedAt(customerIdentifier.getVerifiedAt());
                }

                return existingCustomerIdentifier;
            })
            .map(customerIdentifierRepository::save);
    }

    /**
     * Get one customerIdentifier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerIdentifier> findOne(Long id) {
        LOG.debug("Request to get CustomerIdentifier : {}", id);
        return customerIdentifierRepository.findById(id);
    }

    /**
     * Delete the customerIdentifier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerIdentifier : {}", id);
        customerIdentifierRepository.deleteById(id);
    }
}
