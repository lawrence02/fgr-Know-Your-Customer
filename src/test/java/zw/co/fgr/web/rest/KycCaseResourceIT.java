package zw.co.fgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.fgr.domain.KycCaseAsserts.*;
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
import zw.co.fgr.domain.CdmsSubmission;
import zw.co.fgr.domain.Customer;
import zw.co.fgr.domain.KycCase;
import zw.co.fgr.domain.KycConsent;
import zw.co.fgr.domain.enumeration.ChannelType;
import zw.co.fgr.domain.enumeration.KycStatus;
import zw.co.fgr.repository.KycCaseRepository;

/**
 * Integration tests for the {@link KycCaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KycCaseResourceIT {

    private static final String DEFAULT_KYC_REF = "FGR22405515-517";
    private static final String UPDATED_KYC_REF = "FGR49488233-610";

    private static final KycStatus DEFAULT_STATUS = KycStatus.STARTED;
    private static final KycStatus UPDATED_STATUS = KycStatus.IN_PROGRESS;

    private static final ChannelType DEFAULT_CHANNEL = ChannelType.WHATSAPP;
    private static final ChannelType UPDATED_CHANNEL = ChannelType.WEB;

    private static final Instant DEFAULT_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_ACTIVITY_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTIVITY_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VALIDATION_ERRORS = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATION_ERRORS = "BBBBBBBBBB";

    private static final String DEFAULT_INTERNAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kyc-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KycCaseRepository kycCaseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycCaseMockMvc;

    private KycCase kycCase;

    private KycCase insertedKycCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycCase createEntity() {
        return new KycCase()
            .kycRef(DEFAULT_KYC_REF)
            .status(DEFAULT_STATUS)
            .channel(DEFAULT_CHANNEL)
            .startedAt(DEFAULT_STARTED_AT)
            .lastActivityAt(DEFAULT_LAST_ACTIVITY_AT)
            .lastUpdatedAt(DEFAULT_LAST_UPDATED_AT)
            .completedAt(DEFAULT_COMPLETED_AT)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .validationErrors(DEFAULT_VALIDATION_ERRORS)
            .internalNotes(DEFAULT_INTERNAL_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycCase createUpdatedEntity() {
        return new KycCase()
            .kycRef(UPDATED_KYC_REF)
            .status(UPDATED_STATUS)
            .channel(UPDATED_CHANNEL)
            .startedAt(UPDATED_STARTED_AT)
            .lastActivityAt(UPDATED_LAST_ACTIVITY_AT)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .validationErrors(UPDATED_VALIDATION_ERRORS)
            .internalNotes(UPDATED_INTERNAL_NOTES);
    }

    @BeforeEach
    void initTest() {
        kycCase = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKycCase != null) {
            kycCaseRepository.delete(insertedKycCase);
            insertedKycCase = null;
        }
    }

    @Test
    @Transactional
    void createKycCase() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KycCase
        var returnedKycCase = om.readValue(
            restKycCaseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KycCase.class
        );

        // Validate the KycCase in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertKycCaseUpdatableFieldsEquals(returnedKycCase, getPersistedKycCase(returnedKycCase));

        insertedKycCase = returnedKycCase;
    }

    @Test
    @Transactional
    void createKycCaseWithExistingId() throws Exception {
        // Create the KycCase with an existing ID
        kycCase.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isBadRequest());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKycRefIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCase.setKycRef(null);

        // Create the KycCase, which fails.

        restKycCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCase.setStatus(null);

        // Create the KycCase, which fails.

        restKycCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCase.setChannel(null);

        // Create the KycCase, which fails.

        restKycCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCase.setStartedAt(null);

        // Create the KycCase, which fails.

        restKycCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastActivityAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCase.setLastActivityAt(null);

        // Create the KycCase, which fails.

        restKycCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKycCases() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList
        restKycCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].kycRef").value(hasItem(DEFAULT_KYC_REF)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastActivityAt").value(hasItem(DEFAULT_LAST_ACTIVITY_AT.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedAt").value(hasItem(DEFAULT_LAST_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].validationErrors").value(hasItem(DEFAULT_VALIDATION_ERRORS)))
            .andExpect(jsonPath("$.[*].internalNotes").value(hasItem(DEFAULT_INTERNAL_NOTES)));
    }

    @Test
    @Transactional
    void getKycCase() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get the kycCase
        restKycCaseMockMvc
            .perform(get(ENTITY_API_URL_ID, kycCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycCase.getId().intValue()))
            .andExpect(jsonPath("$.kycRef").value(DEFAULT_KYC_REF))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.lastActivityAt").value(DEFAULT_LAST_ACTIVITY_AT.toString()))
            .andExpect(jsonPath("$.lastUpdatedAt").value(DEFAULT_LAST_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.validationErrors").value(DEFAULT_VALIDATION_ERRORS))
            .andExpect(jsonPath("$.internalNotes").value(DEFAULT_INTERNAL_NOTES));
    }

    @Test
    @Transactional
    void getKycCasesByIdFiltering() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        Long id = kycCase.getId();

        defaultKycCaseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultKycCaseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultKycCaseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllKycCasesByKycRefIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where kycRef equals to
        defaultKycCaseFiltering("kycRef.equals=" + DEFAULT_KYC_REF, "kycRef.equals=" + UPDATED_KYC_REF);
    }

    @Test
    @Transactional
    void getAllKycCasesByKycRefIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where kycRef in
        defaultKycCaseFiltering("kycRef.in=" + DEFAULT_KYC_REF + "," + UPDATED_KYC_REF, "kycRef.in=" + UPDATED_KYC_REF);
    }

    @Test
    @Transactional
    void getAllKycCasesByKycRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where kycRef is not null
        defaultKycCaseFiltering("kycRef.specified=true", "kycRef.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByKycRefContainsSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where kycRef contains
        defaultKycCaseFiltering("kycRef.contains=" + DEFAULT_KYC_REF, "kycRef.contains=" + UPDATED_KYC_REF);
    }

    @Test
    @Transactional
    void getAllKycCasesByKycRefNotContainsSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where kycRef does not contain
        defaultKycCaseFiltering("kycRef.doesNotContain=" + UPDATED_KYC_REF, "kycRef.doesNotContain=" + DEFAULT_KYC_REF);
    }

    @Test
    @Transactional
    void getAllKycCasesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where status equals to
        defaultKycCaseFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllKycCasesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where status in
        defaultKycCaseFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllKycCasesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where status is not null
        defaultKycCaseFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where channel equals to
        defaultKycCaseFiltering("channel.equals=" + DEFAULT_CHANNEL, "channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllKycCasesByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where channel in
        defaultKycCaseFiltering("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL, "channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllKycCasesByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where channel is not null
        defaultKycCaseFiltering("channel.specified=true", "channel.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByStartedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where startedAt equals to
        defaultKycCaseFiltering("startedAt.equals=" + DEFAULT_STARTED_AT, "startedAt.equals=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllKycCasesByStartedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where startedAt in
        defaultKycCaseFiltering("startedAt.in=" + DEFAULT_STARTED_AT + "," + UPDATED_STARTED_AT, "startedAt.in=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllKycCasesByStartedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where startedAt is not null
        defaultKycCaseFiltering("startedAt.specified=true", "startedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByLastActivityAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where lastActivityAt equals to
        defaultKycCaseFiltering("lastActivityAt.equals=" + DEFAULT_LAST_ACTIVITY_AT, "lastActivityAt.equals=" + UPDATED_LAST_ACTIVITY_AT);
    }

    @Test
    @Transactional
    void getAllKycCasesByLastActivityAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where lastActivityAt in
        defaultKycCaseFiltering(
            "lastActivityAt.in=" + DEFAULT_LAST_ACTIVITY_AT + "," + UPDATED_LAST_ACTIVITY_AT,
            "lastActivityAt.in=" + UPDATED_LAST_ACTIVITY_AT
        );
    }

    @Test
    @Transactional
    void getAllKycCasesByLastActivityAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where lastActivityAt is not null
        defaultKycCaseFiltering("lastActivityAt.specified=true", "lastActivityAt.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByLastUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where lastUpdatedAt equals to
        defaultKycCaseFiltering("lastUpdatedAt.equals=" + DEFAULT_LAST_UPDATED_AT, "lastUpdatedAt.equals=" + UPDATED_LAST_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllKycCasesByLastUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where lastUpdatedAt in
        defaultKycCaseFiltering(
            "lastUpdatedAt.in=" + DEFAULT_LAST_UPDATED_AT + "," + UPDATED_LAST_UPDATED_AT,
            "lastUpdatedAt.in=" + UPDATED_LAST_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllKycCasesByLastUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where lastUpdatedAt is not null
        defaultKycCaseFiltering("lastUpdatedAt.specified=true", "lastUpdatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where completedAt equals to
        defaultKycCaseFiltering("completedAt.equals=" + DEFAULT_COMPLETED_AT, "completedAt.equals=" + UPDATED_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllKycCasesByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where completedAt in
        defaultKycCaseFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllKycCasesByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where completedAt is not null
        defaultKycCaseFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByExpiresAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where expiresAt equals to
        defaultKycCaseFiltering("expiresAt.equals=" + DEFAULT_EXPIRES_AT, "expiresAt.equals=" + UPDATED_EXPIRES_AT);
    }

    @Test
    @Transactional
    void getAllKycCasesByExpiresAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where expiresAt in
        defaultKycCaseFiltering("expiresAt.in=" + DEFAULT_EXPIRES_AT + "," + UPDATED_EXPIRES_AT, "expiresAt.in=" + UPDATED_EXPIRES_AT);
    }

    @Test
    @Transactional
    void getAllKycCasesByExpiresAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        // Get all the kycCaseList where expiresAt is not null
        defaultKycCaseFiltering("expiresAt.specified=true", "expiresAt.specified=false");
    }

    @Test
    @Transactional
    void getAllKycCasesByConsentIsEqualToSomething() throws Exception {
        KycConsent consent;
        if (TestUtil.findAll(em, KycConsent.class).isEmpty()) {
            kycCaseRepository.saveAndFlush(kycCase);
            consent = KycConsentResourceIT.createEntity();
        } else {
            consent = TestUtil.findAll(em, KycConsent.class).get(0);
        }
        em.persist(consent);
        em.flush();
        kycCase.setConsent(consent);
        kycCaseRepository.saveAndFlush(kycCase);
        Long consentId = consent.getId();
        // Get all the kycCaseList where consent equals to consentId
        defaultKycCaseShouldBeFound("consentId.equals=" + consentId);

        // Get all the kycCaseList where consent equals to (consentId + 1)
        defaultKycCaseShouldNotBeFound("consentId.equals=" + (consentId + 1));
    }

    @Test
    @Transactional
    void getAllKycCasesBySubmissionIsEqualToSomething() throws Exception {
        CdmsSubmission submission;
        if (TestUtil.findAll(em, CdmsSubmission.class).isEmpty()) {
            kycCaseRepository.saveAndFlush(kycCase);
            submission = CdmsSubmissionResourceIT.createEntity();
        } else {
            submission = TestUtil.findAll(em, CdmsSubmission.class).get(0);
        }
        em.persist(submission);
        em.flush();
        kycCase.setSubmission(submission);
        kycCaseRepository.saveAndFlush(kycCase);
        Long submissionId = submission.getId();
        // Get all the kycCaseList where submission equals to submissionId
        defaultKycCaseShouldBeFound("submissionId.equals=" + submissionId);

        // Get all the kycCaseList where submission equals to (submissionId + 1)
        defaultKycCaseShouldNotBeFound("submissionId.equals=" + (submissionId + 1));
    }

    @Test
    @Transactional
    void getAllKycCasesByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            kycCaseRepository.saveAndFlush(kycCase);
            customer = CustomerResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        kycCase.setCustomer(customer);
        kycCaseRepository.saveAndFlush(kycCase);
        Long customerId = customer.getId();
        // Get all the kycCaseList where customer equals to customerId
        defaultKycCaseShouldBeFound("customerId.equals=" + customerId);

        // Get all the kycCaseList where customer equals to (customerId + 1)
        defaultKycCaseShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultKycCaseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultKycCaseShouldBeFound(shouldBeFound);
        defaultKycCaseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKycCaseShouldBeFound(String filter) throws Exception {
        restKycCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].kycRef").value(hasItem(DEFAULT_KYC_REF)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastActivityAt").value(hasItem(DEFAULT_LAST_ACTIVITY_AT.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedAt").value(hasItem(DEFAULT_LAST_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].validationErrors").value(hasItem(DEFAULT_VALIDATION_ERRORS)))
            .andExpect(jsonPath("$.[*].internalNotes").value(hasItem(DEFAULT_INTERNAL_NOTES)));

        // Check, that the count call also returns 1
        restKycCaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKycCaseShouldNotBeFound(String filter) throws Exception {
        restKycCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKycCaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingKycCase() throws Exception {
        // Get the kycCase
        restKycCaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKycCase() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycCase
        KycCase updatedKycCase = kycCaseRepository.findById(kycCase.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKycCase are not directly saved in db
        em.detach(updatedKycCase);
        updatedKycCase
            .kycRef(UPDATED_KYC_REF)
            .status(UPDATED_STATUS)
            .channel(UPDATED_CHANNEL)
            .startedAt(UPDATED_STARTED_AT)
            .lastActivityAt(UPDATED_LAST_ACTIVITY_AT)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .validationErrors(UPDATED_VALIDATION_ERRORS)
            .internalNotes(UPDATED_INTERNAL_NOTES);

        restKycCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKycCase.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedKycCase))
            )
            .andExpect(status().isOk());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKycCaseToMatchAllProperties(updatedKycCase);
    }

    @Test
    @Transactional
    void putNonExistingKycCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCase.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycCaseMockMvc
            .perform(put(ENTITY_API_URL_ID, kycCase.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isBadRequest());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKycCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycCase))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKycCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKycCaseWithPatch() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycCase using partial update
        KycCase partialUpdatedKycCase = new KycCase();
        partialUpdatedKycCase.setId(kycCase.getId());

        partialUpdatedKycCase
            .status(UPDATED_STATUS)
            .channel(UPDATED_CHANNEL)
            .startedAt(UPDATED_STARTED_AT)
            .internalNotes(UPDATED_INTERNAL_NOTES);

        restKycCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycCase))
            )
            .andExpect(status().isOk());

        // Validate the KycCase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycCaseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedKycCase, kycCase), getPersistedKycCase(kycCase));
    }

    @Test
    @Transactional
    void fullUpdateKycCaseWithPatch() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycCase using partial update
        KycCase partialUpdatedKycCase = new KycCase();
        partialUpdatedKycCase.setId(kycCase.getId());

        partialUpdatedKycCase
            .kycRef(UPDATED_KYC_REF)
            .status(UPDATED_STATUS)
            .channel(UPDATED_CHANNEL)
            .startedAt(UPDATED_STARTED_AT)
            .lastActivityAt(UPDATED_LAST_ACTIVITY_AT)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .validationErrors(UPDATED_VALIDATION_ERRORS)
            .internalNotes(UPDATED_INTERNAL_NOTES);

        restKycCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycCase))
            )
            .andExpect(status().isOk());

        // Validate the KycCase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycCaseUpdatableFieldsEquals(partialUpdatedKycCase, getPersistedKycCase(partialUpdatedKycCase));
    }

    @Test
    @Transactional
    void patchNonExistingKycCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCase.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kycCase.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycCase))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKycCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycCase))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKycCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCase.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycCase)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKycCase() throws Exception {
        // Initialize the database
        insertedKycCase = kycCaseRepository.saveAndFlush(kycCase);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the kycCase
        restKycCaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, kycCase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return kycCaseRepository.count();
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

    protected KycCase getPersistedKycCase(KycCase kycCase) {
        return kycCaseRepository.findById(kycCase.getId()).orElseThrow();
    }

    protected void assertPersistedKycCaseToMatchAllProperties(KycCase expectedKycCase) {
        assertKycCaseAllPropertiesEquals(expectedKycCase, getPersistedKycCase(expectedKycCase));
    }

    protected void assertPersistedKycCaseToMatchUpdatableProperties(KycCase expectedKycCase) {
        assertKycCaseAllUpdatablePropertiesEquals(expectedKycCase, getPersistedKycCase(expectedKycCase));
    }
}
