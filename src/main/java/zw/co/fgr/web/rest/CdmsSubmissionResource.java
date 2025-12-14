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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import zw.co.fgr.domain.CdmsSubmission;
import zw.co.fgr.repository.CdmsSubmissionRepository;
import zw.co.fgr.service.CdmsSubmissionQueryService;
import zw.co.fgr.service.CdmsSubmissionService;
import zw.co.fgr.service.criteria.CdmsSubmissionCriteria;
import zw.co.fgr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.fgr.domain.CdmsSubmission}.
 */
@RestController
@RequestMapping("/api/cdms-submissions")
public class CdmsSubmissionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CdmsSubmissionResource.class);

    private static final String ENTITY_NAME = "cdmsSubmission";

    @Value("${jhipster.clientApp.name:fgrKnowYourCustomer}")
    private String applicationName;

    private final CdmsSubmissionService cdmsSubmissionService;

    private final CdmsSubmissionRepository cdmsSubmissionRepository;

    private final CdmsSubmissionQueryService cdmsSubmissionQueryService;

    public CdmsSubmissionResource(
        CdmsSubmissionService cdmsSubmissionService,
        CdmsSubmissionRepository cdmsSubmissionRepository,
        CdmsSubmissionQueryService cdmsSubmissionQueryService
    ) {
        this.cdmsSubmissionService = cdmsSubmissionService;
        this.cdmsSubmissionRepository = cdmsSubmissionRepository;
        this.cdmsSubmissionQueryService = cdmsSubmissionQueryService;
    }

    /**
     * {@code POST  /cdms-submissions} : Create a new cdmsSubmission.
     *
     * @param cdmsSubmission the cdmsSubmission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cdmsSubmission, or with status {@code 400 (Bad Request)} if the cdmsSubmission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CdmsSubmission> createCdmsSubmission(@Valid @RequestBody CdmsSubmission cdmsSubmission)
        throws URISyntaxException {
        LOG.debug("REST request to save CdmsSubmission : {}", cdmsSubmission);
        if (cdmsSubmission.getId() != null) {
            throw new BadRequestAlertException("A new cdmsSubmission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cdmsSubmission = cdmsSubmissionService.save(cdmsSubmission);
        return ResponseEntity.created(new URI("/api/cdms-submissions/" + cdmsSubmission.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cdmsSubmission.getId().toString()))
            .body(cdmsSubmission);
    }

    /**
     * {@code PUT  /cdms-submissions/:id} : Updates an existing cdmsSubmission.
     *
     * @param id the id of the cdmsSubmission to save.
     * @param cdmsSubmission the cdmsSubmission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cdmsSubmission,
     * or with status {@code 400 (Bad Request)} if the cdmsSubmission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cdmsSubmission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CdmsSubmission> updateCdmsSubmission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CdmsSubmission cdmsSubmission
    ) throws URISyntaxException {
        LOG.debug("REST request to update CdmsSubmission : {}, {}", id, cdmsSubmission);
        if (cdmsSubmission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cdmsSubmission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cdmsSubmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cdmsSubmission = cdmsSubmissionService.update(cdmsSubmission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cdmsSubmission.getId().toString()))
            .body(cdmsSubmission);
    }

    /**
     * {@code PATCH  /cdms-submissions/:id} : Partial updates given fields of an existing cdmsSubmission, field will ignore if it is null
     *
     * @param id the id of the cdmsSubmission to save.
     * @param cdmsSubmission the cdmsSubmission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cdmsSubmission,
     * or with status {@code 400 (Bad Request)} if the cdmsSubmission is not valid,
     * or with status {@code 404 (Not Found)} if the cdmsSubmission is not found,
     * or with status {@code 500 (Internal Server Error)} if the cdmsSubmission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CdmsSubmission> partialUpdateCdmsSubmission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CdmsSubmission cdmsSubmission
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CdmsSubmission partially : {}, {}", id, cdmsSubmission);
        if (cdmsSubmission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cdmsSubmission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cdmsSubmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CdmsSubmission> result = cdmsSubmissionService.partialUpdate(cdmsSubmission);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cdmsSubmission.getId().toString())
        );
    }

    /**
     * {@code GET  /cdms-submissions} : get all the cdmsSubmissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cdmsSubmissions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CdmsSubmission>> getAllCdmsSubmissions(CdmsSubmissionCriteria criteria) {
        LOG.debug("REST request to get CdmsSubmissions by criteria: {}", criteria);

        List<CdmsSubmission> entityList = cdmsSubmissionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cdms-submissions/count} : count all the cdmsSubmissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCdmsSubmissions(CdmsSubmissionCriteria criteria) {
        LOG.debug("REST request to count CdmsSubmissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(cdmsSubmissionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cdms-submissions/:id} : get the "id" cdmsSubmission.
     *
     * @param id the id of the cdmsSubmission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cdmsSubmission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CdmsSubmission> getCdmsSubmission(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CdmsSubmission : {}", id);
        Optional<CdmsSubmission> cdmsSubmission = cdmsSubmissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cdmsSubmission);
    }

    /**
     * {@code DELETE  /cdms-submissions/:id} : delete the "id" cdmsSubmission.
     *
     * @param id the id of the cdmsSubmission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCdmsSubmission(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CdmsSubmission : {}", id);
        cdmsSubmissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
