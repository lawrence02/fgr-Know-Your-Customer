package zw.co.fgr.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.domain.KycConsent;
import zw.co.fgr.repository.KycConsentRepository;

/**
 * Service Implementation for managing {@link zw.co.fgr.domain.KycConsent}.
 */
@Service
@Transactional
public class KycConsentService {

    private static final Logger LOG = LoggerFactory.getLogger(KycConsentService.class);

    private final KycConsentRepository kycConsentRepository;

    public KycConsentService(KycConsentRepository kycConsentRepository) {
        this.kycConsentRepository = kycConsentRepository;
    }

    /**
     * Save a kycConsent.
     *
     * @param kycConsent the entity to save.
     * @return the persisted entity.
     */
    public KycConsent save(KycConsent kycConsent) {
        LOG.debug("Request to save KycConsent : {}", kycConsent);
        return kycConsentRepository.save(kycConsent);
    }

    /**
     * Update a kycConsent.
     *
     * @param kycConsent the entity to save.
     * @return the persisted entity.
     */
    public KycConsent update(KycConsent kycConsent) {
        LOG.debug("Request to update KycConsent : {}", kycConsent);
        return kycConsentRepository.save(kycConsent);
    }

    /**
     * Partially update a kycConsent.
     *
     * @param kycConsent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KycConsent> partialUpdate(KycConsent kycConsent) {
        LOG.debug("Request to partially update KycConsent : {}", kycConsent);

        return kycConsentRepository
            .findById(kycConsent.getId())
            .map(existingKycConsent -> {
                if (kycConsent.getConsentText() != null) {
                    existingKycConsent.setConsentText(kycConsent.getConsentText());
                }
                if (kycConsent.getConsented() != null) {
                    existingKycConsent.setConsented(kycConsent.getConsented());
                }
                if (kycConsent.getConsentedAt() != null) {
                    existingKycConsent.setConsentedAt(kycConsent.getConsentedAt());
                }
                if (kycConsent.getChannel() != null) {
                    existingKycConsent.setChannel(kycConsent.getChannel());
                }
                if (kycConsent.getIpAddress() != null) {
                    existingKycConsent.setIpAddress(kycConsent.getIpAddress());
                }
                if (kycConsent.getUserAgent() != null) {
                    existingKycConsent.setUserAgent(kycConsent.getUserAgent());
                }
                if (kycConsent.getConsentVersion() != null) {
                    existingKycConsent.setConsentVersion(kycConsent.getConsentVersion());
                }

                return existingKycConsent;
            })
            .map(kycConsentRepository::save);
    }

    /**
     * Get all the kycConsents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<KycConsent> findAll() {
        LOG.debug("Request to get all KycConsents");
        return kycConsentRepository.findAll();
    }

    /**
     *  Get all the kycConsents where KycCase is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<KycConsent> findAllWhereKycCaseIsNull() {
        LOG.debug("Request to get all kycConsents where KycCase is null");
        return StreamSupport.stream(kycConsentRepository.findAll().spliterator(), false)
            .filter(kycConsent -> kycConsent.getKycCase() == null)
            .toList();
    }

    /**
     * Get one kycConsent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KycConsent> findOne(Long id) {
        LOG.debug("Request to get KycConsent : {}", id);
        return kycConsentRepository.findById(id);
    }

    /**
     * Delete the kycConsent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KycConsent : {}", id);
        kycConsentRepository.deleteById(id);
    }
}
