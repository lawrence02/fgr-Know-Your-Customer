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
import zw.co.fgr.domain.KycNotification;
import zw.co.fgr.repository.KycNotificationRepository;
import zw.co.fgr.service.KycNotificationService;
import zw.co.fgr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.fgr.domain.KycNotification}.
 */
@RestController
@RequestMapping("/api/kyc-notifications")
public class KycNotificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(KycNotificationResource.class);

    private static final String ENTITY_NAME = "kycNotification";

    @Value("${jhipster.clientApp.name:fgrKnowYourCustomer}")
    private String applicationName;

    private final KycNotificationService kycNotificationService;

    private final KycNotificationRepository kycNotificationRepository;

    public KycNotificationResource(KycNotificationService kycNotificationService, KycNotificationRepository kycNotificationRepository) {
        this.kycNotificationService = kycNotificationService;
        this.kycNotificationRepository = kycNotificationRepository;
    }

    /**
     * {@code POST  /kyc-notifications} : Create a new kycNotification.
     *
     * @param kycNotification the kycNotification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kycNotification, or with status {@code 400 (Bad Request)} if the kycNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KycNotification> createKycNotification(@Valid @RequestBody KycNotification kycNotification)
        throws URISyntaxException {
        LOG.debug("REST request to save KycNotification : {}", kycNotification);
        if (kycNotification.getId() != null) {
            throw new BadRequestAlertException("A new kycNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        kycNotification = kycNotificationService.save(kycNotification);
        return ResponseEntity.created(new URI("/api/kyc-notifications/" + kycNotification.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, kycNotification.getId().toString()))
            .body(kycNotification);
    }

    /**
     * {@code PUT  /kyc-notifications/:id} : Updates an existing kycNotification.
     *
     * @param id the id of the kycNotification to save.
     * @param kycNotification the kycNotification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycNotification,
     * or with status {@code 400 (Bad Request)} if the kycNotification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kycNotification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KycNotification> updateKycNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KycNotification kycNotification
    ) throws URISyntaxException {
        LOG.debug("REST request to update KycNotification : {}, {}", id, kycNotification);
        if (kycNotification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycNotification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kycNotification = kycNotificationService.update(kycNotification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycNotification.getId().toString()))
            .body(kycNotification);
    }

    /**
     * {@code PATCH  /kyc-notifications/:id} : Partial updates given fields of an existing kycNotification, field will ignore if it is null
     *
     * @param id the id of the kycNotification to save.
     * @param kycNotification the kycNotification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycNotification,
     * or with status {@code 400 (Bad Request)} if the kycNotification is not valid,
     * or with status {@code 404 (Not Found)} if the kycNotification is not found,
     * or with status {@code 500 (Internal Server Error)} if the kycNotification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KycNotification> partialUpdateKycNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KycNotification kycNotification
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KycNotification partially : {}, {}", id, kycNotification);
        if (kycNotification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycNotification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KycNotification> result = kycNotificationService.partialUpdate(kycNotification);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycNotification.getId().toString())
        );
    }

    /**
     * {@code GET  /kyc-notifications} : get all the kycNotifications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycNotifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KycNotification>> getAllKycNotifications(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of KycNotifications");
        Page<KycNotification> page = kycNotificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kyc-notifications/:id} : get the "id" kycNotification.
     *
     * @param id the id of the kycNotification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kycNotification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycNotification> getKycNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KycNotification : {}", id);
        Optional<KycNotification> kycNotification = kycNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kycNotification);
    }

    /**
     * {@code DELETE  /kyc-notifications/:id} : delete the "id" kycNotification.
     *
     * @param id the id of the kycNotification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKycNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KycNotification : {}", id);
        kycNotificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
