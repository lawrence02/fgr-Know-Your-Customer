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
import zw.co.fgr.domain.KycCase;
import zw.co.fgr.repository.KycCaseRepository;
import zw.co.fgr.service.KycCaseQueryService;
import zw.co.fgr.service.KycCaseService;
import zw.co.fgr.service.criteria.KycCaseCriteria;
import zw.co.fgr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.fgr.domain.KycCase}.
 */
@RestController
@RequestMapping("/api/kyc-cases")
public class KycCaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(KycCaseResource.class);

    private static final String ENTITY_NAME = "kycCase";

    @Value("${jhipster.clientApp.name:fgrKnowYourCustomer}")
    private String applicationName;

    private final KycCaseService kycCaseService;

    private final KycCaseRepository kycCaseRepository;

    private final KycCaseQueryService kycCaseQueryService;

    public KycCaseResource(KycCaseService kycCaseService, KycCaseRepository kycCaseRepository, KycCaseQueryService kycCaseQueryService) {
        this.kycCaseService = kycCaseService;
        this.kycCaseRepository = kycCaseRepository;
        this.kycCaseQueryService = kycCaseQueryService;
    }

    /**
     * {@code POST  /kyc-cases} : Create a new kycCase.
     *
     * @param kycCase the kycCase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kycCase, or with status {@code 400 (Bad Request)} if the kycCase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KycCase> createKycCase(@Valid @RequestBody KycCase kycCase) throws URISyntaxException {
        LOG.debug("REST request to save KycCase : {}", kycCase);
        if (kycCase.getId() != null) {
            throw new BadRequestAlertException("A new kycCase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        kycCase = kycCaseService.save(kycCase);
        return ResponseEntity.created(new URI("/api/kyc-cases/" + kycCase.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, kycCase.getId().toString()))
            .body(kycCase);
    }

    /**
     * {@code PUT  /kyc-cases/:id} : Updates an existing kycCase.
     *
     * @param id the id of the kycCase to save.
     * @param kycCase the kycCase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycCase,
     * or with status {@code 400 (Bad Request)} if the kycCase is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kycCase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KycCase> updateKycCase(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KycCase kycCase
    ) throws URISyntaxException {
        LOG.debug("REST request to update KycCase : {}, {}", id, kycCase);
        if (kycCase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycCase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycCaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kycCase = kycCaseService.update(kycCase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycCase.getId().toString()))
            .body(kycCase);
    }

    /**
     * {@code PATCH  /kyc-cases/:id} : Partial updates given fields of an existing kycCase, field will ignore if it is null
     *
     * @param id the id of the kycCase to save.
     * @param kycCase the kycCase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycCase,
     * or with status {@code 400 (Bad Request)} if the kycCase is not valid,
     * or with status {@code 404 (Not Found)} if the kycCase is not found,
     * or with status {@code 500 (Internal Server Error)} if the kycCase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KycCase> partialUpdateKycCase(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KycCase kycCase
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KycCase partially : {}, {}", id, kycCase);
        if (kycCase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycCase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycCaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KycCase> result = kycCaseService.partialUpdate(kycCase);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycCase.getId().toString())
        );
    }

    /**
     * {@code GET  /kyc-cases} : get all the kycCases.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycCases in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KycCase>> getAllKycCases(
        KycCaseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get KycCases by criteria: {}", criteria);

        Page<KycCase> page = kycCaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kyc-cases/count} : count all the kycCases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countKycCases(KycCaseCriteria criteria) {
        LOG.debug("REST request to count KycCases by criteria: {}", criteria);
        return ResponseEntity.ok().body(kycCaseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /kyc-cases/:id} : get the "id" kycCase.
     *
     * @param id the id of the kycCase to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kycCase, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycCase> getKycCase(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KycCase : {}", id);
        Optional<KycCase> kycCase = kycCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kycCase);
    }

    /**
     * {@code DELETE  /kyc-cases/:id} : delete the "id" kycCase.
     *
     * @param id the id of the kycCase to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKycCase(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KycCase : {}", id);
        kycCaseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
