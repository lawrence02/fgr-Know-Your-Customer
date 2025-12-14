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
import zw.co.fgr.domain.KycConsent;
import zw.co.fgr.repository.KycConsentRepository;
import zw.co.fgr.service.KycConsentService;
import zw.co.fgr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.fgr.domain.KycConsent}.
 */
@RestController
@RequestMapping("/api/kyc-consents")
public class KycConsentResource {

    private static final Logger LOG = LoggerFactory.getLogger(KycConsentResource.class);

    private static final String ENTITY_NAME = "kycConsent";

    @Value("${jhipster.clientApp.name:fgrKnowYourCustomer}")
    private String applicationName;

    private final KycConsentService kycConsentService;

    private final KycConsentRepository kycConsentRepository;

    public KycConsentResource(KycConsentService kycConsentService, KycConsentRepository kycConsentRepository) {
        this.kycConsentService = kycConsentService;
        this.kycConsentRepository = kycConsentRepository;
    }

    /**
     * {@code POST  /kyc-consents} : Create a new kycConsent.
     *
     * @param kycConsent the kycConsent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kycConsent, or with status {@code 400 (Bad Request)} if the kycConsent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KycConsent> createKycConsent(@Valid @RequestBody KycConsent kycConsent) throws URISyntaxException {
        LOG.debug("REST request to save KycConsent : {}", kycConsent);
        if (kycConsent.getId() != null) {
            throw new BadRequestAlertException("A new kycConsent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        kycConsent = kycConsentService.save(kycConsent);
        return ResponseEntity.created(new URI("/api/kyc-consents/" + kycConsent.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, kycConsent.getId().toString()))
            .body(kycConsent);
    }

    /**
     * {@code PUT  /kyc-consents/:id} : Updates an existing kycConsent.
     *
     * @param id the id of the kycConsent to save.
     * @param kycConsent the kycConsent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycConsent,
     * or with status {@code 400 (Bad Request)} if the kycConsent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kycConsent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KycConsent> updateKycConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KycConsent kycConsent
    ) throws URISyntaxException {
        LOG.debug("REST request to update KycConsent : {}, {}", id, kycConsent);
        if (kycConsent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycConsent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycConsentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kycConsent = kycConsentService.update(kycConsent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycConsent.getId().toString()))
            .body(kycConsent);
    }

    /**
     * {@code PATCH  /kyc-consents/:id} : Partial updates given fields of an existing kycConsent, field will ignore if it is null
     *
     * @param id the id of the kycConsent to save.
     * @param kycConsent the kycConsent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycConsent,
     * or with status {@code 400 (Bad Request)} if the kycConsent is not valid,
     * or with status {@code 404 (Not Found)} if the kycConsent is not found,
     * or with status {@code 500 (Internal Server Error)} if the kycConsent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KycConsent> partialUpdateKycConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KycConsent kycConsent
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KycConsent partially : {}, {}", id, kycConsent);
        if (kycConsent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycConsent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycConsentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KycConsent> result = kycConsentService.partialUpdate(kycConsent);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycConsent.getId().toString())
        );
    }

    /**
     * {@code GET  /kyc-consents} : get all the kycConsents.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycConsents in body.
     */
    @GetMapping("")
    public List<KycConsent> getAllKycConsents(@RequestParam(name = "filter", required = false) String filter) {
        if ("kyccase-is-null".equals(filter)) {
            LOG.debug("REST request to get all KycConsents where kycCase is null");
            return kycConsentService.findAllWhereKycCaseIsNull();
        }
        LOG.debug("REST request to get all KycConsents");
        return kycConsentService.findAll();
    }

    /**
     * {@code GET  /kyc-consents/:id} : get the "id" kycConsent.
     *
     * @param id the id of the kycConsent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kycConsent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycConsent> getKycConsent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KycConsent : {}", id);
        Optional<KycConsent> kycConsent = kycConsentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kycConsent);
    }

    /**
     * {@code DELETE  /kyc-consents/:id} : delete the "id" kycConsent.
     *
     * @param id the id of the kycConsent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKycConsent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KycConsent : {}", id);
        kycConsentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
