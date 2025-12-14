package zw.co.fgr.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import zw.co.fgr.domain.CustomerIdentifier;
import zw.co.fgr.repository.CustomerIdentifierRepository;
import zw.co.fgr.service.CustomerIdentifierQueryService;
import zw.co.fgr.service.CustomerIdentifierService;
import zw.co.fgr.service.criteria.CustomerIdentifierCriteria;
import zw.co.fgr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.fgr.domain.CustomerIdentifier}.
 */
@RestController
@RequestMapping("/api/customer-identifiers")
public class CustomerIdentifierResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerIdentifierResource.class);

    private static final String ENTITY_NAME = "customerIdentifier";

    @Value("${jhipster.clientApp.name:fgrKnowYourCustomer}")
    private String applicationName;

    private final CustomerIdentifierService customerIdentifierService;

    private final CustomerIdentifierRepository customerIdentifierRepository;

    private final CustomerIdentifierQueryService customerIdentifierQueryService;

    public CustomerIdentifierResource(
        CustomerIdentifierService customerIdentifierService,
        CustomerIdentifierRepository customerIdentifierRepository,
        CustomerIdentifierQueryService customerIdentifierQueryService
    ) {
        this.customerIdentifierService = customerIdentifierService;
        this.customerIdentifierRepository = customerIdentifierRepository;
        this.customerIdentifierQueryService = customerIdentifierQueryService;
    }

    /**
     * {@code POST  /customer-identifiers} : Create a new customerIdentifier.
     *
     * @param customerIdentifier the customerIdentifier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerIdentifier, or with status {@code 400 (Bad Request)} if the customerIdentifier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerIdentifier> createCustomerIdentifier(@Valid @RequestBody CustomerIdentifier customerIdentifier)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerIdentifier : {}", customerIdentifier);
        if (customerIdentifier.getId() != null) {
            throw new BadRequestAlertException("A new customerIdentifier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerIdentifier = customerIdentifierService.save(customerIdentifier);
        return ResponseEntity.created(new URI("/api/customer-identifiers/" + customerIdentifier.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerIdentifier.getId().toString()))
            .body(customerIdentifier);
    }

    /**
     * {@code PUT  /customer-identifiers/:id} : Updates an existing customerIdentifier.
     *
     * @param id the id of the customerIdentifier to save.
     * @param customerIdentifier the customerIdentifier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerIdentifier,
     * or with status {@code 400 (Bad Request)} if the customerIdentifier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerIdentifier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerIdentifier> updateCustomerIdentifier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerIdentifier customerIdentifier
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerIdentifier : {}, {}", id, customerIdentifier);
        if (customerIdentifier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerIdentifier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerIdentifierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerIdentifier = customerIdentifierService.update(customerIdentifier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerIdentifier.getId().toString()))
            .body(customerIdentifier);
    }

    /**
     * {@code PATCH  /customer-identifiers/:id} : Partial updates given fields of an existing customerIdentifier, field will ignore if it is null
     *
     * @param id the id of the customerIdentifier to save.
     * @param customerIdentifier the customerIdentifier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerIdentifier,
     * or with status {@code 400 (Bad Request)} if the customerIdentifier is not valid,
     * or with status {@code 404 (Not Found)} if the customerIdentifier is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerIdentifier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerIdentifier> partialUpdateCustomerIdentifier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerIdentifier customerIdentifier
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerIdentifier partially : {}, {}", id, customerIdentifier);
        if (customerIdentifier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerIdentifier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerIdentifierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerIdentifier> result = customerIdentifierService.partialUpdate(customerIdentifier);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerIdentifier.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-identifiers} : get all the customerIdentifiers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerIdentifiers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerIdentifier>> getAllCustomerIdentifiers(
        CustomerIdentifierCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerIdentifiers by criteria: {}", criteria);

        Page<CustomerIdentifier> page = customerIdentifierQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-identifiers/count} : count all the customerIdentifiers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerIdentifiers(CustomerIdentifierCriteria criteria) {
        LOG.debug("REST request to count CustomerIdentifiers by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerIdentifierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-identifiers/:id} : get the "id" customerIdentifier.
     *
     * @param id the id of the customerIdentifier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerIdentifier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerIdentifier> getCustomerIdentifier(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerIdentifier : {}", id);
        Optional<CustomerIdentifier> customerIdentifier = customerIdentifierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerIdentifier);
    }

    /**
     * {@code DELETE  /customer-identifiers/:id} : delete the "id" customerIdentifier.
     *
     * @param id the id of the customerIdentifier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerIdentifier(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerIdentifier : {}", id);
        customerIdentifierService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
