package zw.co.fgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.fgr.domain.KycConsentAsserts.*;
import static zw.co.fgr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zw.co.fgr.IntegrationTest;
import zw.co.fgr.domain.KycConsent;
import zw.co.fgr.domain.enumeration.ChannelType;
import zw.co.fgr.repository.KycConsentRepository;

/**
 * Integration tests for the {@link KycConsentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KycConsentResourceIT {

    private static final String DEFAULT_CONSENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONSENT_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONSENTED = false;
    private static final Boolean UPDATED_CONSENTED = true;

    private static final Instant DEFAULT_CONSENTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONSENTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ChannelType DEFAULT_CHANNEL = ChannelType.WHATSAPP;
    private static final ChannelType UPDATED_CHANNEL = ChannelType.WEB;

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONSENT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_CONSENT_VERSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kyc-consents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KycConsentRepository kycConsentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycConsentMockMvc;

    private KycConsent kycConsent;

    private KycConsent insertedKycConsent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycConsent createEntity() {
        return new KycConsent()
            .consentText(DEFAULT_CONSENT_TEXT)
            .consented(DEFAULT_CONSENTED)
            .consentedAt(DEFAULT_CONSENTED_AT)
            .channel(DEFAULT_CHANNEL)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .userAgent(DEFAULT_USER_AGENT)
            .consentVersion(DEFAULT_CONSENT_VERSION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycConsent createUpdatedEntity() {
        return new KycConsent()
            .consentText(UPDATED_CONSENT_TEXT)
            .consented(UPDATED_CONSENTED)
            .consentedAt(UPDATED_CONSENTED_AT)
            .channel(UPDATED_CHANNEL)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .consentVersion(UPDATED_CONSENT_VERSION);
    }

    @BeforeEach
    void initTest() {
        kycConsent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKycConsent != null) {
            kycConsentRepository.delete(insertedKycConsent);
            insertedKycConsent = null;
        }
    }

    @Test
    @Transactional
    void createKycConsent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KycConsent
        var returnedKycConsent = om.readValue(
            restKycConsentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KycConsent.class
        );

        // Validate the KycConsent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertKycConsentUpdatableFieldsEquals(returnedKycConsent, getPersistedKycConsent(returnedKycConsent));

        insertedKycConsent = returnedKycConsent;
    }

    @Test
    @Transactional
    void createKycConsentWithExistingId() throws Exception {
        // Create the KycConsent with an existing ID
        kycConsent.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent)))
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConsentTextIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycConsent.setConsentText(null);

        // Create the KycConsent, which fails.

        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConsentedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycConsent.setConsented(null);

        // Create the KycConsent, which fails.

        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConsentedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycConsent.setConsentedAt(null);

        // Create the KycConsent, which fails.

        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycConsent.setChannel(null);

        // Create the KycConsent, which fails.

        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKycConsents() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        // Get all the kycConsentList
        restKycConsentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycConsent.getId().intValue())))
            .andExpect(jsonPath("$.[*].consentText").value(hasItem(DEFAULT_CONSENT_TEXT)))
            .andExpect(jsonPath("$.[*].consented").value(hasItem(DEFAULT_CONSENTED)))
            .andExpect(jsonPath("$.[*].consentedAt").value(hasItem(DEFAULT_CONSENTED_AT.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT)))
            .andExpect(jsonPath("$.[*].consentVersion").value(hasItem(DEFAULT_CONSENT_VERSION)));
    }

    @Test
    @Transactional
    void getKycConsent() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        // Get the kycConsent
        restKycConsentMockMvc
            .perform(get(ENTITY_API_URL_ID, kycConsent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycConsent.getId().intValue()))
            .andExpect(jsonPath("$.consentText").value(DEFAULT_CONSENT_TEXT))
            .andExpect(jsonPath("$.consented").value(DEFAULT_CONSENTED))
            .andExpect(jsonPath("$.consentedAt").value(DEFAULT_CONSENTED_AT.toString()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.userAgent").value(DEFAULT_USER_AGENT))
            .andExpect(jsonPath("$.consentVersion").value(DEFAULT_CONSENT_VERSION));
    }

    @Test
    @Transactional
    void getNonExistingKycConsent() throws Exception {
        // Get the kycConsent
        restKycConsentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKycConsent() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycConsent
        KycConsent updatedKycConsent = kycConsentRepository.findById(kycConsent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKycConsent are not directly saved in db
        em.detach(updatedKycConsent);
        updatedKycConsent
            .consentText(UPDATED_CONSENT_TEXT)
            .consented(UPDATED_CONSENTED)
            .consentedAt(UPDATED_CONSENTED_AT)
            .channel(UPDATED_CHANNEL)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .consentVersion(UPDATED_CONSENT_VERSION);

        restKycConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKycConsent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedKycConsent))
            )
            .andExpect(status().isOk());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKycConsentToMatchAllProperties(updatedKycConsent);
    }

    @Test
    @Transactional
    void putNonExistingKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycConsent.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycConsent))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKycConsentWithPatch() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycConsent using partial update
        KycConsent partialUpdatedKycConsent = new KycConsent();
        partialUpdatedKycConsent.setId(kycConsent.getId());

        partialUpdatedKycConsent.ipAddress(UPDATED_IP_ADDRESS);

        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycConsent))
            )
            .andExpect(status().isOk());

        // Validate the KycConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycConsentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKycConsent, kycConsent),
            getPersistedKycConsent(kycConsent)
        );
    }

    @Test
    @Transactional
    void fullUpdateKycConsentWithPatch() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycConsent using partial update
        KycConsent partialUpdatedKycConsent = new KycConsent();
        partialUpdatedKycConsent.setId(kycConsent.getId());

        partialUpdatedKycConsent
            .consentText(UPDATED_CONSENT_TEXT)
            .consented(UPDATED_CONSENTED)
            .consentedAt(UPDATED_CONSENTED_AT)
            .channel(UPDATED_CHANNEL)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .consentVersion(UPDATED_CONSENT_VERSION);

        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycConsent))
            )
            .andExpect(status().isOk());

        // Validate the KycConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycConsentUpdatableFieldsEquals(partialUpdatedKycConsent, getPersistedKycConsent(partialUpdatedKycConsent));
    }

    @Test
    @Transactional
    void patchNonExistingKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kycConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycConsent))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycConsent))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycConsent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKycConsent() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the kycConsent
        restKycConsentMockMvc
            .perform(delete(ENTITY_API_URL_ID, kycConsent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return kycConsentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected KycConsent getPersistedKycConsent(KycConsent kycConsent) {
        return kycConsentRepository.findById(kycConsent.getId()).orElseThrow();
    }

    protected void assertPersistedKycConsentToMatchAllProperties(KycConsent expectedKycConsent) {
        assertKycConsentAllPropertiesEquals(expectedKycConsent, getPersistedKycConsent(expectedKycConsent));
    }

    protected void assertPersistedKycConsentToMatchUpdatableProperties(KycConsent expectedKycConsent) {
        assertKycConsentAllUpdatablePropertiesEquals(expectedKycConsent, getPersistedKycConsent(expectedKycConsent));
    }
}
