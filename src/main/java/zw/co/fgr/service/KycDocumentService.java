package zw.co.fgr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.domain.KycDocument;
import zw.co.fgr.repository.KycDocumentRepository;

/**
 * Service Implementation for managing {@link zw.co.fgr.domain.KycDocument}.
 */
@Service
@Transactional
public class KycDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(KycDocumentService.class);

    private final KycDocumentRepository kycDocumentRepository;

    public KycDocumentService(KycDocumentRepository kycDocumentRepository) {
        this.kycDocumentRepository = kycDocumentRepository;
    }

    /**
     * Save a kycDocument.
     *
     * @param kycDocument the entity to save.
     * @return the persisted entity.
     */
    public KycDocument save(KycDocument kycDocument) {
        LOG.debug("Request to save KycDocument : {}", kycDocument);
        return kycDocumentRepository.save(kycDocument);
    }

    /**
     * Update a kycDocument.
     *
     * @param kycDocument the entity to save.
     * @return the persisted entity.
     */
    public KycDocument update(KycDocument kycDocument) {
        LOG.debug("Request to update KycDocument : {}", kycDocument);
        return kycDocumentRepository.save(kycDocument);
    }

    /**
     * Partially update a kycDocument.
     *
     * @param kycDocument the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KycDocument> partialUpdate(KycDocument kycDocument) {
        LOG.debug("Request to partially update KycDocument : {}", kycDocument);

        return kycDocumentRepository
            .findById(kycDocument.getId())
            .map(existingKycDocument -> {
                if (kycDocument.getDocumentType() != null) {
                    existingKycDocument.setDocumentType(kycDocument.getDocumentType());
                }
                if (kycDocument.getFileName() != null) {
                    existingKycDocument.setFileName(kycDocument.getFileName());
                }
                if (kycDocument.getMimeType() != null) {
                    existingKycDocument.setMimeType(kycDocument.getMimeType());
                }
                if (kycDocument.getStoragePath() != null) {
                    existingKycDocument.setStoragePath(kycDocument.getStoragePath());
                }
                if (kycDocument.getFileSize() != null) {
                    existingKycDocument.setFileSize(kycDocument.getFileSize());
                }
                if (kycDocument.getUploadedAt() != null) {
                    existingKycDocument.setUploadedAt(kycDocument.getUploadedAt());
                }
                if (kycDocument.getExpiresAt() != null) {
                    existingKycDocument.setExpiresAt(kycDocument.getExpiresAt());
                }
                if (kycDocument.getDeleted() != null) {
                    existingKycDocument.setDeleted(kycDocument.getDeleted());
                }
                if (kycDocument.getDeletedAt() != null) {
                    existingKycDocument.setDeletedAt(kycDocument.getDeletedAt());
                }
                if (kycDocument.getMetadata() != null) {
                    existingKycDocument.setMetadata(kycDocument.getMetadata());
                }
                if (kycDocument.getChecksum() != null) {
                    existingKycDocument.setChecksum(kycDocument.getChecksum());
                }

                return existingKycDocument;
            })
            .map(kycDocumentRepository::save);
    }

    /**
     * Get all the kycDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<KycDocument> findAll(Pageable pageable) {
        LOG.debug("Request to get all KycDocuments");
        return kycDocumentRepository.findAll(pageable);
    }

    /**
     * Get one kycDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KycDocument> findOne(Long id) {
        LOG.debug("Request to get KycDocument : {}", id);
        return kycDocumentRepository.findById(id);
    }

    /**
     * Delete the kycDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KycDocument : {}", id);
        kycDocumentRepository.deleteById(id);
    }
}
