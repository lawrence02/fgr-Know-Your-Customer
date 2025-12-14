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
import zw.co.fgr.domain.KycDocument;
import zw.co.fgr.repository.KycDocumentRepository;
import zw.co.fgr.service.KycDocumentService;
import zw.co.fgr.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zw.co.fgr.domain.KycDocument}.
 */
@RestController
@RequestMapping("/api/kyc-documents")
public class KycDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(KycDocumentResource.class);

    private static final String ENTITY_NAME = "kycDocument";

    @Value("${jhipster.clientApp.name:fgrKnowYourCustomer}")
    private String applicationName;

    private final KycDocumentService kycDocumentService;

    private final KycDocumentRepository kycDocumentRepository;

    public KycDocumentResource(KycDocumentService kycDocumentService, KycDocumentRepository kycDocumentRepository) {
        this.kycDocumentService = kycDocumentService;
        this.kycDocumentRepository = kycDocumentRepository;
    }

    /**
     * {@code POST  /kyc-documents} : Create a new kycDocument.
     *
     * @param kycDocument the kycDocument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kycDocument, or with status {@code 400 (Bad Request)} if the kycDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KycDocument> createKycDocument(@Valid @RequestBody KycDocument kycDocument) throws URISyntaxException {
        LOG.debug("REST request to save KycDocument : {}", kycDocument);
        if (kycDocument.getId() != null) {
            throw new BadRequestAlertException("A new kycDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        kycDocument = kycDocumentService.save(kycDocument);
        return ResponseEntity.created(new URI("/api/kyc-documents/" + kycDocument.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, kycDocument.getId().toString()))
            .body(kycDocument);
    }

    /**
     * {@code PUT  /kyc-documents/:id} : Updates an existing kycDocument.
     *
     * @param id the id of the kycDocument to save.
     * @param kycDocument the kycDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycDocument,
     * or with status {@code 400 (Bad Request)} if the kycDocument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kycDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KycDocument> updateKycDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KycDocument kycDocument
    ) throws URISyntaxException {
        LOG.debug("REST request to update KycDocument : {}, {}", id, kycDocument);
        if (kycDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kycDocument = kycDocumentService.update(kycDocument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycDocument.getId().toString()))
            .body(kycDocument);
    }

    /**
     * {@code PATCH  /kyc-documents/:id} : Partial updates given fields of an existing kycDocument, field will ignore if it is null
     *
     * @param id the id of the kycDocument to save.
     * @param kycDocument the kycDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycDocument,
     * or with status {@code 400 (Bad Request)} if the kycDocument is not valid,
     * or with status {@code 404 (Not Found)} if the kycDocument is not found,
     * or with status {@code 500 (Internal Server Error)} if the kycDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KycDocument> partialUpdateKycDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KycDocument kycDocument
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KycDocument partially : {}, {}", id, kycDocument);
        if (kycDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KycDocument> result = kycDocumentService.partialUpdate(kycDocument);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycDocument.getId().toString())
        );
    }

    /**
     * {@code GET  /kyc-documents} : get all the kycDocuments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KycDocument>> getAllKycDocuments(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of KycDocuments");
        Page<KycDocument> page = kycDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kyc-documents/:id} : get the "id" kycDocument.
     *
     * @param id the id of the kycDocument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kycDocument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycDocument> getKycDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KycDocument : {}", id);
        Optional<KycDocument> kycDocument = kycDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kycDocument);
    }

    /**
     * {@code DELETE  /kyc-documents/:id} : delete the "id" kycDocument.
     *
     * @param id the id of the kycDocument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKycDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KycDocument : {}", id);
        kycDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
