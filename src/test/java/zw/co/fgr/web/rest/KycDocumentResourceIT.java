package zw.co.fgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.fgr.domain.KycDocumentAsserts.*;
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
import zw.co.fgr.domain.KycDocument;
import zw.co.fgr.domain.enumeration.DocumentType;
import zw.co.fgr.repository.KycDocumentRepository;

/**
 * Integration tests for the {@link KycDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KycDocumentResourceIT {

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.NATIONAL_ID;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.PASSPORT;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STORAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final Instant DEFAULT_UPLOADED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String DEFAULT_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECKSUM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kyc-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KycDocumentRepository kycDocumentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycDocumentMockMvc;

    private KycDocument kycDocument;

    private KycDocument insertedKycDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycDocument createEntity() {
        return new KycDocument()
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .fileName(DEFAULT_FILE_NAME)
            .mimeType(DEFAULT_MIME_TYPE)
            .storagePath(DEFAULT_STORAGE_PATH)
            .fileSize(DEFAULT_FILE_SIZE)
            .uploadedAt(DEFAULT_UPLOADED_AT)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .deleted(DEFAULT_DELETED)
            .deletedAt(DEFAULT_DELETED_AT)
            .metadata(DEFAULT_METADATA)
            .checksum(DEFAULT_CHECKSUM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycDocument createUpdatedEntity() {
        return new KycDocument()
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .mimeType(UPDATED_MIME_TYPE)
            .storagePath(UPDATED_STORAGE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadedAt(UPDATED_UPLOADED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .metadata(UPDATED_METADATA)
            .checksum(UPDATED_CHECKSUM);
    }

    @BeforeEach
    void initTest() {
        kycDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKycDocument != null) {
            kycDocumentRepository.delete(insertedKycDocument);
            insertedKycDocument = null;
        }
    }

    @Test
    @Transactional
    void createKycDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KycDocument
        var returnedKycDocument = om.readValue(
            restKycDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KycDocument.class
        );

        // Validate the KycDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertKycDocumentUpdatableFieldsEquals(returnedKycDocument, getPersistedKycDocument(returnedKycDocument));

        insertedKycDocument = returnedKycDocument;
    }

    @Test
    @Transactional
    void createKycDocumentWithExistingId() throws Exception {
        // Create the KycDocument with an existing ID
        kycDocument.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isBadRequest());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDocument.setDocumentType(null);

        // Create the KycDocument, which fails.

        restKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDocument.setFileName(null);

        // Create the KycDocument, which fails.

        restKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMimeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDocument.setMimeType(null);

        // Create the KycDocument, which fails.

        restKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStoragePathIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDocument.setStoragePath(null);

        // Create the KycDocument, which fails.

        restKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUploadedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDocument.setUploadedAt(null);

        // Create the KycDocument, which fails.

        restKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiresAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDocument.setExpiresAt(null);

        // Create the KycDocument, which fails.

        restKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKycDocuments() throws Exception {
        // Initialize the database
        insertedKycDocument = kycDocumentRepository.saveAndFlush(kycDocument);

        // Get all the kycDocumentList
        restKycDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].storagePath").value(hasItem(DEFAULT_STORAGE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadedAt").value(hasItem(DEFAULT_UPLOADED_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)));
    }

    @Test
    @Transactional
    void getKycDocument() throws Exception {
        // Initialize the database
        insertedKycDocument = kycDocumentRepository.saveAndFlush(kycDocument);

        // Get the kycDocument
        restKycDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, kycDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.storagePath").value(DEFAULT_STORAGE_PATH))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.uploadedAt").value(DEFAULT_UPLOADED_AT.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM));
    }

    @Test
    @Transactional
    void getNonExistingKycDocument() throws Exception {
        // Get the kycDocument
        restKycDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKycDocument() throws Exception {
        // Initialize the database
        insertedKycDocument = kycDocumentRepository.saveAndFlush(kycDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycDocument
        KycDocument updatedKycDocument = kycDocumentRepository.findById(kycDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKycDocument are not directly saved in db
        em.detach(updatedKycDocument);
        updatedKycDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .mimeType(UPDATED_MIME_TYPE)
            .storagePath(UPDATED_STORAGE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadedAt(UPDATED_UPLOADED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .metadata(UPDATED_METADATA)
            .checksum(UPDATED_CHECKSUM);

        restKycDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKycDocument.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedKycDocument))
            )
            .andExpect(status().isOk());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKycDocumentToMatchAllProperties(updatedKycDocument);
    }

    @Test
    @Transactional
    void putNonExistingKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDocument.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycDocument.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKycDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedKycDocument = kycDocumentRepository.saveAndFlush(kycDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycDocument using partial update
        KycDocument partialUpdatedKycDocument = new KycDocument();
        partialUpdatedKycDocument.setId(kycDocument.getId());

        partialUpdatedKycDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .storagePath(UPDATED_STORAGE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadedAt(UPDATED_UPLOADED_AT)
            .expiresAt(UPDATED_EXPIRES_AT);

        restKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycDocument))
            )
            .andExpect(status().isOk());

        // Validate the KycDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKycDocument, kycDocument),
            getPersistedKycDocument(kycDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateKycDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedKycDocument = kycDocumentRepository.saveAndFlush(kycDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycDocument using partial update
        KycDocument partialUpdatedKycDocument = new KycDocument();
        partialUpdatedKycDocument.setId(kycDocument.getId());

        partialUpdatedKycDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .mimeType(UPDATED_MIME_TYPE)
            .storagePath(UPDATED_STORAGE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadedAt(UPDATED_UPLOADED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .deleted(UPDATED_DELETED)
            .deletedAt(UPDATED_DELETED_AT)
            .metadata(UPDATED_METADATA)
            .checksum(UPDATED_CHECKSUM);

        restKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycDocument))
            )
            .andExpect(status().isOk());

        // Validate the KycDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycDocumentUpdatableFieldsEquals(partialUpdatedKycDocument, getPersistedKycDocument(partialUpdatedKycDocument));
    }

    @Test
    @Transactional
    void patchNonExistingKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDocument.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kycDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycDocument)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKycDocument() throws Exception {
        // Initialize the database
        insertedKycDocument = kycDocumentRepository.saveAndFlush(kycDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the kycDocument
        restKycDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, kycDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return kycDocumentRepository.count();
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

    protected KycDocument getPersistedKycDocument(KycDocument kycDocument) {
        return kycDocumentRepository.findById(kycDocument.getId()).orElseThrow();
    }

    protected void assertPersistedKycDocumentToMatchAllProperties(KycDocument expectedKycDocument) {
        assertKycDocumentAllPropertiesEquals(expectedKycDocument, getPersistedKycDocument(expectedKycDocument));
    }

    protected void assertPersistedKycDocumentToMatchUpdatableProperties(KycDocument expectedKycDocument) {
        assertKycDocumentAllUpdatablePropertiesEquals(expectedKycDocument, getPersistedKycDocument(expectedKycDocument));
    }
}
