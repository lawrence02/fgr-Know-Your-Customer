package zw.co.fgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.fgr.domain.CdmsSubmissionAsserts.*;
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
import zw.co.fgr.domain.enumeration.SubmissionStatus;
import zw.co.fgr.repository.CdmsSubmissionRepository;

/**
 * Integration tests for the {@link CdmsSubmissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CdmsSubmissionResourceIT {

    private static final String DEFAULT_SUBMISSION_REF = "AAAAAAAAAA";
    private static final String UPDATED_SUBMISSION_REF = "BBBBBBBBBB";

    private static final SubmissionStatus DEFAULT_STATUS = SubmissionStatus.PENDING;
    private static final SubmissionStatus UPDATED_STATUS = SubmissionStatus.SUCCESS;

    private static final String DEFAULT_RESPONSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATTEMPTS = 0;
    private static final Integer UPDATED_ATTEMPTS = 1;
    private static final Integer SMALLER_ATTEMPTS = 0 - 1;

    private static final Instant DEFAULT_SUBMITTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_ATTEMPT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ATTEMPT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEXT_RETRY_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_RETRY_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CDMS_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CDMS_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cdms-submissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CdmsSubmissionRepository cdmsSubmissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCdmsSubmissionMockMvc;

    private CdmsSubmission cdmsSubmission;

    private CdmsSubmission insertedCdmsSubmission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CdmsSubmission createEntity() {
        return new CdmsSubmission()
            .submissionRef(DEFAULT_SUBMISSION_REF)
            .status(DEFAULT_STATUS)
            .responseCode(DEFAULT_RESPONSE_CODE)
            .responseMessage(DEFAULT_RESPONSE_MESSAGE)
            .attempts(DEFAULT_ATTEMPTS)
            .submittedAt(DEFAULT_SUBMITTED_AT)
            .lastAttemptAt(DEFAULT_LAST_ATTEMPT_AT)
            .nextRetryAt(DEFAULT_NEXT_RETRY_AT)
            .cdmsCustomerId(DEFAULT_CDMS_CUSTOMER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CdmsSubmission createUpdatedEntity() {
        return new CdmsSubmission()
            .submissionRef(UPDATED_SUBMISSION_REF)
            .status(UPDATED_STATUS)
            .responseCode(UPDATED_RESPONSE_CODE)
            .responseMessage(UPDATED_RESPONSE_MESSAGE)
            .attempts(UPDATED_ATTEMPTS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .lastAttemptAt(UPDATED_LAST_ATTEMPT_AT)
            .nextRetryAt(UPDATED_NEXT_RETRY_AT)
            .cdmsCustomerId(UPDATED_CDMS_CUSTOMER_ID);
    }

    @BeforeEach
    void initTest() {
        cdmsSubmission = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCdmsSubmission != null) {
            cdmsSubmissionRepository.delete(insertedCdmsSubmission);
            insertedCdmsSubmission = null;
        }
    }

    @Test
    @Transactional
    void createCdmsSubmission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CdmsSubmission
        var returnedCdmsSubmission = om.readValue(
            restCdmsSubmissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cdmsSubmission)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CdmsSubmission.class
        );

        // Validate the CdmsSubmission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCdmsSubmissionUpdatableFieldsEquals(returnedCdmsSubmission, getPersistedCdmsSubmission(returnedCdmsSubmission));

        insertedCdmsSubmission = returnedCdmsSubmission;
    }

    @Test
    @Transactional
    void createCdmsSubmissionWithExistingId() throws Exception {
        // Create the CdmsSubmission with an existing ID
        cdmsSubmission.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCdmsSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cdmsSubmission)))
            .andExpect(status().isBadRequest());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubmissionRefIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cdmsSubmission.setSubmissionRef(null);

        // Create the CdmsSubmission, which fails.

        restCdmsSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cdmsSubmission)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cdmsSubmission.setStatus(null);

        // Create the CdmsSubmission, which fails.

        restCdmsSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cdmsSubmission)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttemptsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cdmsSubmission.setAttempts(null);

        // Create the CdmsSubmission, which fails.

        restCdmsSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cdmsSubmission)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissions() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList
        restCdmsSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cdmsSubmission.getId().intValue())))
            .andExpect(jsonPath("$.[*].submissionRef").value(hasItem(DEFAULT_SUBMISSION_REF)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].responseCode").value(hasItem(DEFAULT_RESPONSE_CODE)))
            .andExpect(jsonPath("$.[*].responseMessage").value(hasItem(DEFAULT_RESPONSE_MESSAGE)))
            .andExpect(jsonPath("$.[*].attempts").value(hasItem(DEFAULT_ATTEMPTS)))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(DEFAULT_SUBMITTED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastAttemptAt").value(hasItem(DEFAULT_LAST_ATTEMPT_AT.toString())))
            .andExpect(jsonPath("$.[*].nextRetryAt").value(hasItem(DEFAULT_NEXT_RETRY_AT.toString())))
            .andExpect(jsonPath("$.[*].cdmsCustomerId").value(hasItem(DEFAULT_CDMS_CUSTOMER_ID)));
    }

    @Test
    @Transactional
    void getCdmsSubmission() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get the cdmsSubmission
        restCdmsSubmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, cdmsSubmission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cdmsSubmission.getId().intValue()))
            .andExpect(jsonPath("$.submissionRef").value(DEFAULT_SUBMISSION_REF))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.responseCode").value(DEFAULT_RESPONSE_CODE))
            .andExpect(jsonPath("$.responseMessage").value(DEFAULT_RESPONSE_MESSAGE))
            .andExpect(jsonPath("$.attempts").value(DEFAULT_ATTEMPTS))
            .andExpect(jsonPath("$.submittedAt").value(DEFAULT_SUBMITTED_AT.toString()))
            .andExpect(jsonPath("$.lastAttemptAt").value(DEFAULT_LAST_ATTEMPT_AT.toString()))
            .andExpect(jsonPath("$.nextRetryAt").value(DEFAULT_NEXT_RETRY_AT.toString()))
            .andExpect(jsonPath("$.cdmsCustomerId").value(DEFAULT_CDMS_CUSTOMER_ID));
    }

    @Test
    @Transactional
    void getCdmsSubmissionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        Long id = cdmsSubmission.getId();

        defaultCdmsSubmissionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCdmsSubmissionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCdmsSubmissionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmissionRefIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submissionRef equals to
        defaultCdmsSubmissionFiltering("submissionRef.equals=" + DEFAULT_SUBMISSION_REF, "submissionRef.equals=" + UPDATED_SUBMISSION_REF);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmissionRefIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submissionRef in
        defaultCdmsSubmissionFiltering(
            "submissionRef.in=" + DEFAULT_SUBMISSION_REF + "," + UPDATED_SUBMISSION_REF,
            "submissionRef.in=" + UPDATED_SUBMISSION_REF
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmissionRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submissionRef is not null
        defaultCdmsSubmissionFiltering("submissionRef.specified=true", "submissionRef.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmissionRefContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submissionRef contains
        defaultCdmsSubmissionFiltering(
            "submissionRef.contains=" + DEFAULT_SUBMISSION_REF,
            "submissionRef.contains=" + UPDATED_SUBMISSION_REF
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmissionRefNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submissionRef does not contain
        defaultCdmsSubmissionFiltering(
            "submissionRef.doesNotContain=" + UPDATED_SUBMISSION_REF,
            "submissionRef.doesNotContain=" + DEFAULT_SUBMISSION_REF
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where status equals to
        defaultCdmsSubmissionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where status in
        defaultCdmsSubmissionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where status is not null
        defaultCdmsSubmissionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseCode equals to
        defaultCdmsSubmissionFiltering("responseCode.equals=" + DEFAULT_RESPONSE_CODE, "responseCode.equals=" + UPDATED_RESPONSE_CODE);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseCode in
        defaultCdmsSubmissionFiltering(
            "responseCode.in=" + DEFAULT_RESPONSE_CODE + "," + UPDATED_RESPONSE_CODE,
            "responseCode.in=" + UPDATED_RESPONSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseCode is not null
        defaultCdmsSubmissionFiltering("responseCode.specified=true", "responseCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseCode contains
        defaultCdmsSubmissionFiltering("responseCode.contains=" + DEFAULT_RESPONSE_CODE, "responseCode.contains=" + UPDATED_RESPONSE_CODE);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseCode does not contain
        defaultCdmsSubmissionFiltering(
            "responseCode.doesNotContain=" + UPDATED_RESPONSE_CODE,
            "responseCode.doesNotContain=" + DEFAULT_RESPONSE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseMessage equals to
        defaultCdmsSubmissionFiltering(
            "responseMessage.equals=" + DEFAULT_RESPONSE_MESSAGE,
            "responseMessage.equals=" + UPDATED_RESPONSE_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseMessage in
        defaultCdmsSubmissionFiltering(
            "responseMessage.in=" + DEFAULT_RESPONSE_MESSAGE + "," + UPDATED_RESPONSE_MESSAGE,
            "responseMessage.in=" + UPDATED_RESPONSE_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseMessage is not null
        defaultCdmsSubmissionFiltering("responseMessage.specified=true", "responseMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseMessage contains
        defaultCdmsSubmissionFiltering(
            "responseMessage.contains=" + DEFAULT_RESPONSE_MESSAGE,
            "responseMessage.contains=" + UPDATED_RESPONSE_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByResponseMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where responseMessage does not contain
        defaultCdmsSubmissionFiltering(
            "responseMessage.doesNotContain=" + UPDATED_RESPONSE_MESSAGE,
            "responseMessage.doesNotContain=" + DEFAULT_RESPONSE_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByAttemptsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where attempts equals to
        defaultCdmsSubmissionFiltering("attempts.equals=" + DEFAULT_ATTEMPTS, "attempts.equals=" + UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByAttemptsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where attempts in
        defaultCdmsSubmissionFiltering("attempts.in=" + DEFAULT_ATTEMPTS + "," + UPDATED_ATTEMPTS, "attempts.in=" + UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByAttemptsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where attempts is not null
        defaultCdmsSubmissionFiltering("attempts.specified=true", "attempts.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByAttemptsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where attempts is greater than or equal to
        defaultCdmsSubmissionFiltering(
            "attempts.greaterThanOrEqual=" + DEFAULT_ATTEMPTS,
            "attempts.greaterThanOrEqual=" + (DEFAULT_ATTEMPTS + 1)
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByAttemptsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where attempts is less than or equal to
        defaultCdmsSubmissionFiltering("attempts.lessThanOrEqual=" + DEFAULT_ATTEMPTS, "attempts.lessThanOrEqual=" + SMALLER_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByAttemptsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where attempts is less than
        defaultCdmsSubmissionFiltering("attempts.lessThan=" + (DEFAULT_ATTEMPTS + 1), "attempts.lessThan=" + DEFAULT_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByAttemptsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where attempts is greater than
        defaultCdmsSubmissionFiltering("attempts.greaterThan=" + SMALLER_ATTEMPTS, "attempts.greaterThan=" + DEFAULT_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmittedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submittedAt equals to
        defaultCdmsSubmissionFiltering("submittedAt.equals=" + DEFAULT_SUBMITTED_AT, "submittedAt.equals=" + UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmittedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submittedAt in
        defaultCdmsSubmissionFiltering(
            "submittedAt.in=" + DEFAULT_SUBMITTED_AT + "," + UPDATED_SUBMITTED_AT,
            "submittedAt.in=" + UPDATED_SUBMITTED_AT
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsBySubmittedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where submittedAt is not null
        defaultCdmsSubmissionFiltering("submittedAt.specified=true", "submittedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByLastAttemptAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where lastAttemptAt equals to
        defaultCdmsSubmissionFiltering(
            "lastAttemptAt.equals=" + DEFAULT_LAST_ATTEMPT_AT,
            "lastAttemptAt.equals=" + UPDATED_LAST_ATTEMPT_AT
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByLastAttemptAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where lastAttemptAt in
        defaultCdmsSubmissionFiltering(
            "lastAttemptAt.in=" + DEFAULT_LAST_ATTEMPT_AT + "," + UPDATED_LAST_ATTEMPT_AT,
            "lastAttemptAt.in=" + UPDATED_LAST_ATTEMPT_AT
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByLastAttemptAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where lastAttemptAt is not null
        defaultCdmsSubmissionFiltering("lastAttemptAt.specified=true", "lastAttemptAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByNextRetryAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where nextRetryAt equals to
        defaultCdmsSubmissionFiltering("nextRetryAt.equals=" + DEFAULT_NEXT_RETRY_AT, "nextRetryAt.equals=" + UPDATED_NEXT_RETRY_AT);
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByNextRetryAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where nextRetryAt in
        defaultCdmsSubmissionFiltering(
            "nextRetryAt.in=" + DEFAULT_NEXT_RETRY_AT + "," + UPDATED_NEXT_RETRY_AT,
            "nextRetryAt.in=" + UPDATED_NEXT_RETRY_AT
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByNextRetryAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where nextRetryAt is not null
        defaultCdmsSubmissionFiltering("nextRetryAt.specified=true", "nextRetryAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByCdmsCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where cdmsCustomerId equals to
        defaultCdmsSubmissionFiltering(
            "cdmsCustomerId.equals=" + DEFAULT_CDMS_CUSTOMER_ID,
            "cdmsCustomerId.equals=" + UPDATED_CDMS_CUSTOMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByCdmsCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where cdmsCustomerId in
        defaultCdmsSubmissionFiltering(
            "cdmsCustomerId.in=" + DEFAULT_CDMS_CUSTOMER_ID + "," + UPDATED_CDMS_CUSTOMER_ID,
            "cdmsCustomerId.in=" + UPDATED_CDMS_CUSTOMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByCdmsCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where cdmsCustomerId is not null
        defaultCdmsSubmissionFiltering("cdmsCustomerId.specified=true", "cdmsCustomerId.specified=false");
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByCdmsCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where cdmsCustomerId contains
        defaultCdmsSubmissionFiltering(
            "cdmsCustomerId.contains=" + DEFAULT_CDMS_CUSTOMER_ID,
            "cdmsCustomerId.contains=" + UPDATED_CDMS_CUSTOMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCdmsSubmissionsByCdmsCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        // Get all the cdmsSubmissionList where cdmsCustomerId does not contain
        defaultCdmsSubmissionFiltering(
            "cdmsCustomerId.doesNotContain=" + UPDATED_CDMS_CUSTOMER_ID,
            "cdmsCustomerId.doesNotContain=" + DEFAULT_CDMS_CUSTOMER_ID
        );
    }

    private void defaultCdmsSubmissionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCdmsSubmissionShouldBeFound(shouldBeFound);
        defaultCdmsSubmissionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCdmsSubmissionShouldBeFound(String filter) throws Exception {
        restCdmsSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cdmsSubmission.getId().intValue())))
            .andExpect(jsonPath("$.[*].submissionRef").value(hasItem(DEFAULT_SUBMISSION_REF)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].responseCode").value(hasItem(DEFAULT_RESPONSE_CODE)))
            .andExpect(jsonPath("$.[*].responseMessage").value(hasItem(DEFAULT_RESPONSE_MESSAGE)))
            .andExpect(jsonPath("$.[*].attempts").value(hasItem(DEFAULT_ATTEMPTS)))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(DEFAULT_SUBMITTED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastAttemptAt").value(hasItem(DEFAULT_LAST_ATTEMPT_AT.toString())))
            .andExpect(jsonPath("$.[*].nextRetryAt").value(hasItem(DEFAULT_NEXT_RETRY_AT.toString())))
            .andExpect(jsonPath("$.[*].cdmsCustomerId").value(hasItem(DEFAULT_CDMS_CUSTOMER_ID)));

        // Check, that the count call also returns 1
        restCdmsSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCdmsSubmissionShouldNotBeFound(String filter) throws Exception {
        restCdmsSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCdmsSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCdmsSubmission() throws Exception {
        // Get the cdmsSubmission
        restCdmsSubmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCdmsSubmission() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cdmsSubmission
        CdmsSubmission updatedCdmsSubmission = cdmsSubmissionRepository.findById(cdmsSubmission.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCdmsSubmission are not directly saved in db
        em.detach(updatedCdmsSubmission);
        updatedCdmsSubmission
            .submissionRef(UPDATED_SUBMISSION_REF)
            .status(UPDATED_STATUS)
            .responseCode(UPDATED_RESPONSE_CODE)
            .responseMessage(UPDATED_RESPONSE_MESSAGE)
            .attempts(UPDATED_ATTEMPTS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .lastAttemptAt(UPDATED_LAST_ATTEMPT_AT)
            .nextRetryAt(UPDATED_NEXT_RETRY_AT)
            .cdmsCustomerId(UPDATED_CDMS_CUSTOMER_ID);

        restCdmsSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCdmsSubmission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCdmsSubmission))
            )
            .andExpect(status().isOk());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCdmsSubmissionToMatchAllProperties(updatedCdmsSubmission);
    }

    @Test
    @Transactional
    void putNonExistingCdmsSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cdmsSubmission.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCdmsSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cdmsSubmission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cdmsSubmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCdmsSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cdmsSubmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdmsSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cdmsSubmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCdmsSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cdmsSubmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdmsSubmissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cdmsSubmission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCdmsSubmissionWithPatch() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cdmsSubmission using partial update
        CdmsSubmission partialUpdatedCdmsSubmission = new CdmsSubmission();
        partialUpdatedCdmsSubmission.setId(cdmsSubmission.getId());

        partialUpdatedCdmsSubmission
            .status(UPDATED_STATUS)
            .responseCode(UPDATED_RESPONSE_CODE)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .nextRetryAt(UPDATED_NEXT_RETRY_AT);

        restCdmsSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCdmsSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCdmsSubmission))
            )
            .andExpect(status().isOk());

        // Validate the CdmsSubmission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCdmsSubmissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCdmsSubmission, cdmsSubmission),
            getPersistedCdmsSubmission(cdmsSubmission)
        );
    }

    @Test
    @Transactional
    void fullUpdateCdmsSubmissionWithPatch() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cdmsSubmission using partial update
        CdmsSubmission partialUpdatedCdmsSubmission = new CdmsSubmission();
        partialUpdatedCdmsSubmission.setId(cdmsSubmission.getId());

        partialUpdatedCdmsSubmission
            .submissionRef(UPDATED_SUBMISSION_REF)
            .status(UPDATED_STATUS)
            .responseCode(UPDATED_RESPONSE_CODE)
            .responseMessage(UPDATED_RESPONSE_MESSAGE)
            .attempts(UPDATED_ATTEMPTS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .lastAttemptAt(UPDATED_LAST_ATTEMPT_AT)
            .nextRetryAt(UPDATED_NEXT_RETRY_AT)
            .cdmsCustomerId(UPDATED_CDMS_CUSTOMER_ID);

        restCdmsSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCdmsSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCdmsSubmission))
            )
            .andExpect(status().isOk());

        // Validate the CdmsSubmission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCdmsSubmissionUpdatableFieldsEquals(partialUpdatedCdmsSubmission, getPersistedCdmsSubmission(partialUpdatedCdmsSubmission));
    }

    @Test
    @Transactional
    void patchNonExistingCdmsSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cdmsSubmission.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCdmsSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cdmsSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cdmsSubmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCdmsSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cdmsSubmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdmsSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cdmsSubmission))
            )
            .andExpect(status().isBadRequest());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCdmsSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cdmsSubmission.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdmsSubmissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cdmsSubmission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CdmsSubmission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCdmsSubmission() throws Exception {
        // Initialize the database
        insertedCdmsSubmission = cdmsSubmissionRepository.saveAndFlush(cdmsSubmission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cdmsSubmission
        restCdmsSubmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, cdmsSubmission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cdmsSubmissionRepository.count();
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

    protected CdmsSubmission getPersistedCdmsSubmission(CdmsSubmission cdmsSubmission) {
        return cdmsSubmissionRepository.findById(cdmsSubmission.getId()).orElseThrow();
    }

    protected void assertPersistedCdmsSubmissionToMatchAllProperties(CdmsSubmission expectedCdmsSubmission) {
        assertCdmsSubmissionAllPropertiesEquals(expectedCdmsSubmission, getPersistedCdmsSubmission(expectedCdmsSubmission));
    }

    protected void assertPersistedCdmsSubmissionToMatchUpdatableProperties(CdmsSubmission expectedCdmsSubmission) {
        assertCdmsSubmissionAllUpdatablePropertiesEquals(expectedCdmsSubmission, getPersistedCdmsSubmission(expectedCdmsSubmission));
    }
}
