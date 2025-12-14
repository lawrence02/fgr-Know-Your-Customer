package zw.co.fgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.fgr.domain.KycNotificationAsserts.*;
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
import zw.co.fgr.domain.KycNotification;
import zw.co.fgr.domain.enumeration.NotificationType;
import zw.co.fgr.repository.KycNotificationRepository;

/**
 * Integration tests for the {@link KycNotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KycNotificationResourceIT {

    private static final NotificationType DEFAULT_NOTIFICATION_TYPE = NotificationType.WELCOME;
    private static final NotificationType UPDATED_NOTIFICATION_TYPE = NotificationType.DOCUMENT_RECEIVED;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_SENT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELIVERED = false;
    private static final Boolean UPDATED_DELIVERED = true;

    private static final Instant DEFAULT_DELIVERED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kyc-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KycNotificationRepository kycNotificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycNotificationMockMvc;

    private KycNotification kycNotification;

    private KycNotification insertedKycNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycNotification createEntity() {
        return new KycNotification()
            .notificationType(DEFAULT_NOTIFICATION_TYPE)
            .message(DEFAULT_MESSAGE)
            .sentAt(DEFAULT_SENT_AT)
            .delivered(DEFAULT_DELIVERED)
            .deliveredAt(DEFAULT_DELIVERED_AT)
            .errorMessage(DEFAULT_ERROR_MESSAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycNotification createUpdatedEntity() {
        return new KycNotification()
            .notificationType(UPDATED_NOTIFICATION_TYPE)
            .message(UPDATED_MESSAGE)
            .sentAt(UPDATED_SENT_AT)
            .delivered(UPDATED_DELIVERED)
            .deliveredAt(UPDATED_DELIVERED_AT)
            .errorMessage(UPDATED_ERROR_MESSAGE);
    }

    @BeforeEach
    void initTest() {
        kycNotification = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKycNotification != null) {
            kycNotificationRepository.delete(insertedKycNotification);
            insertedKycNotification = null;
        }
    }

    @Test
    @Transactional
    void createKycNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KycNotification
        var returnedKycNotification = om.readValue(
            restKycNotificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycNotification)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KycNotification.class
        );

        // Validate the KycNotification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertKycNotificationUpdatableFieldsEquals(returnedKycNotification, getPersistedKycNotification(returnedKycNotification));

        insertedKycNotification = returnedKycNotification;
    }

    @Test
    @Transactional
    void createKycNotificationWithExistingId() throws Exception {
        // Create the KycNotification with an existing ID
        kycNotification.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycNotification)))
            .andExpect(status().isBadRequest());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNotificationTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycNotification.setNotificationType(null);

        // Create the KycNotification, which fails.

        restKycNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycNotification)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycNotification.setMessage(null);

        // Create the KycNotification, which fails.

        restKycNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycNotification)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSentAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycNotification.setSentAt(null);

        // Create the KycNotification, which fails.

        restKycNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycNotification)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKycNotifications() throws Exception {
        // Initialize the database
        insertedKycNotification = kycNotificationRepository.saveAndFlush(kycNotification);

        // Get all the kycNotificationList
        restKycNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationType").value(hasItem(DEFAULT_NOTIFICATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(DEFAULT_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED)))
            .andExpect(jsonPath("$.[*].deliveredAt").value(hasItem(DEFAULT_DELIVERED_AT.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)));
    }

    @Test
    @Transactional
    void getKycNotification() throws Exception {
        // Initialize the database
        insertedKycNotification = kycNotificationRepository.saveAndFlush(kycNotification);

        // Get the kycNotification
        restKycNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, kycNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycNotification.getId().intValue()))
            .andExpect(jsonPath("$.notificationType").value(DEFAULT_NOTIFICATION_TYPE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.sentAt").value(DEFAULT_SENT_AT.toString()))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED))
            .andExpect(jsonPath("$.deliveredAt").value(DEFAULT_DELIVERED_AT.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingKycNotification() throws Exception {
        // Get the kycNotification
        restKycNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKycNotification() throws Exception {
        // Initialize the database
        insertedKycNotification = kycNotificationRepository.saveAndFlush(kycNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycNotification
        KycNotification updatedKycNotification = kycNotificationRepository.findById(kycNotification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKycNotification are not directly saved in db
        em.detach(updatedKycNotification);
        updatedKycNotification
            .notificationType(UPDATED_NOTIFICATION_TYPE)
            .message(UPDATED_MESSAGE)
            .sentAt(UPDATED_SENT_AT)
            .delivered(UPDATED_DELIVERED)
            .deliveredAt(UPDATED_DELIVERED_AT)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restKycNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKycNotification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedKycNotification))
            )
            .andExpect(status().isOk());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKycNotificationToMatchAllProperties(updatedKycNotification);
    }

    @Test
    @Transactional
    void putNonExistingKycNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycNotification.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycNotification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKycNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycNotification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKycNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycNotification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycNotificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycNotification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKycNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedKycNotification = kycNotificationRepository.saveAndFlush(kycNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycNotification using partial update
        KycNotification partialUpdatedKycNotification = new KycNotification();
        partialUpdatedKycNotification.setId(kycNotification.getId());

        partialUpdatedKycNotification.notificationType(UPDATED_NOTIFICATION_TYPE).message(UPDATED_MESSAGE).sentAt(UPDATED_SENT_AT);

        restKycNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycNotification))
            )
            .andExpect(status().isOk());

        // Validate the KycNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycNotificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKycNotification, kycNotification),
            getPersistedKycNotification(kycNotification)
        );
    }

    @Test
    @Transactional
    void fullUpdateKycNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedKycNotification = kycNotificationRepository.saveAndFlush(kycNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycNotification using partial update
        KycNotification partialUpdatedKycNotification = new KycNotification();
        partialUpdatedKycNotification.setId(kycNotification.getId());

        partialUpdatedKycNotification
            .notificationType(UPDATED_NOTIFICATION_TYPE)
            .message(UPDATED_MESSAGE)
            .sentAt(UPDATED_SENT_AT)
            .delivered(UPDATED_DELIVERED)
            .deliveredAt(UPDATED_DELIVERED_AT)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restKycNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycNotification))
            )
            .andExpect(status().isOk());

        // Validate the KycNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycNotificationUpdatableFieldsEquals(
            partialUpdatedKycNotification,
            getPersistedKycNotification(partialUpdatedKycNotification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingKycNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycNotification.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kycNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKycNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycNotification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycNotification))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKycNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycNotification.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycNotificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycNotification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKycNotification() throws Exception {
        // Initialize the database
        insertedKycNotification = kycNotificationRepository.saveAndFlush(kycNotification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the kycNotification
        restKycNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, kycNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return kycNotificationRepository.count();
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

    protected KycNotification getPersistedKycNotification(KycNotification kycNotification) {
        return kycNotificationRepository.findById(kycNotification.getId()).orElseThrow();
    }

    protected void assertPersistedKycNotificationToMatchAllProperties(KycNotification expectedKycNotification) {
        assertKycNotificationAllPropertiesEquals(expectedKycNotification, getPersistedKycNotification(expectedKycNotification));
    }

    protected void assertPersistedKycNotificationToMatchUpdatableProperties(KycNotification expectedKycNotification) {
        assertKycNotificationAllUpdatablePropertiesEquals(expectedKycNotification, getPersistedKycNotification(expectedKycNotification));
    }
}
