package zw.co.fgr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.domain.Customer;
import zw.co.fgr.repository.CustomerRepository;

/**
 * Service Implementation for managing {@link zw.co.fgr.domain.Customer}.
 */
@Service
@Transactional
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Save a customer.
     *
     * @param customer the entity to save.
     * @return the persisted entity.
     */
    public Customer save(Customer customer) {
        LOG.debug("Request to save Customer : {}", customer);
        return customerRepository.save(customer);
    }

    /**
     * Update a customer.
     *
     * @param customer the entity to save.
     * @return the persisted entity.
     */
    public Customer update(Customer customer) {
        LOG.debug("Request to update Customer : {}", customer);
        return customerRepository.save(customer);
    }

    /**
     * Partially update a customer.
     *
     * @param customer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Customer> partialUpdate(Customer customer) {
        LOG.debug("Request to partially update Customer : {}", customer);

        return customerRepository
            .findById(customer.getId())
            .map(existingCustomer -> {
                if (customer.getCustomerRef() != null) {
                    existingCustomer.setCustomerRef(customer.getCustomerRef());
                }
                if (customer.getCustomerType() != null) {
                    existingCustomer.setCustomerType(customer.getCustomerType());
                }
                if (customer.getFullName() != null) {
                    existingCustomer.setFullName(customer.getFullName());
                }
                if (customer.getDateOfBirth() != null) {
                    existingCustomer.setDateOfBirth(customer.getDateOfBirth());
                }
                if (customer.getIdNumber() != null) {
                    existingCustomer.setIdNumber(customer.getIdNumber());
                }
                if (customer.getRegistrationNumber() != null) {
                    existingCustomer.setRegistrationNumber(customer.getRegistrationNumber());
                }
                if (customer.getAddress() != null) {
                    existingCustomer.setAddress(customer.getAddress());
                }
                if (customer.getPhoneNumber() != null) {
                    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                }
                if (customer.getCreatedAt() != null) {
                    existingCustomer.setCreatedAt(customer.getCreatedAt());
                }
                if (customer.getUpdatedAt() != null) {
                    existingCustomer.setUpdatedAt(customer.getUpdatedAt());
                }

                return existingCustomer;
            })
            .map(customerRepository::save);
    }

    /**
     * Get one customer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Customer> findOne(Long id) {
        LOG.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id);
    }

    /**
     * Delete the customer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
    }
}
