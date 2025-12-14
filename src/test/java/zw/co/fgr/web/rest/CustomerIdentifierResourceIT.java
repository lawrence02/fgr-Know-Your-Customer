package zw.co.fgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.fgr.domain.CustomerIdentifierAsserts.*;
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
import zw.co.fgr.domain.Customer;
import zw.co.fgr.domain.CustomerIdentifier;
import zw.co.fgr.domain.enumeration.ChannelType;
import zw.co.fgr.domain.enumeration.IdentifierType;
import zw.co.fgr.repository.CustomerIdentifierRepository;

/**
 * Integration tests for the {@link CustomerIdentifierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerIdentifierResourceIT {

    private static final IdentifierType DEFAULT_IDENTIFIER_TYPE = IdentifierType.PHONE_NUMBER;
    private static final IdentifierType UPDATED_IDENTIFIER_TYPE = IdentifierType.EMAIL;

    private static final String DEFAULT_IDENTIFIER_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER_VALUE = "BBBBBBBBBB";

    private static final ChannelType DEFAULT_CHANNEL = ChannelType.WHATSAPP;
    private static final ChannelType UPDATED_CHANNEL = ChannelType.WEB;

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final Boolean DEFAULT_IS_PRIMARY = false;
    private static final Boolean UPDATED_IS_PRIMARY = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VERIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERIFIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/customer-identifiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerIdentifierRepository customerIdentifierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerIdentifierMockMvc;

    private CustomerIdentifier customerIdentifier;

    private CustomerIdentifier insertedCustomerIdentifier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerIdentifier createEntity() {
        return new CustomerIdentifier()
            .identifierType(DEFAULT_IDENTIFIER_TYPE)
            .identifierValue(DEFAULT_IDENTIFIER_VALUE)
            .channel(DEFAULT_CHANNEL)
            .verified(DEFAULT_VERIFIED)
            .isPrimary(DEFAULT_IS_PRIMARY)
            .createdAt(DEFAULT_CREATED_AT)
            .verifiedAt(DEFAULT_VERIFIED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerIdentifier createUpdatedEntity() {
        return new CustomerIdentifier()
            .identifierType(UPDATED_IDENTIFIER_TYPE)
            .identifierValue(UPDATED_IDENTIFIER_VALUE)
            .channel(UPDATED_CHANNEL)
            .verified(UPDATED_VERIFIED)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .verifiedAt(UPDATED_VERIFIED_AT);
    }

    @BeforeEach
    void initTest() {
        customerIdentifier = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomerIdentifier != null) {
            customerIdentifierRepository.delete(insertedCustomerIdentifier);
            insertedCustomerIdentifier = null;
        }
    }

    @Test
    @Transactional
    void createCustomerIdentifier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerIdentifier
        var returnedCustomerIdentifier = om.readValue(
            restCustomerIdentifierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerIdentifier.class
        );

        // Validate the CustomerIdentifier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerIdentifierUpdatableFieldsEquals(
            returnedCustomerIdentifier,
            getPersistedCustomerIdentifier(returnedCustomerIdentifier)
        );

        insertedCustomerIdentifier = returnedCustomerIdentifier;
    }

    @Test
    @Transactional
    void createCustomerIdentifierWithExistingId() throws Exception {
        // Create the CustomerIdentifier with an existing ID
        customerIdentifier.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerIdentifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentifierTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerIdentifier.setIdentifierType(null);

        // Create the CustomerIdentifier, which fails.

        restCustomerIdentifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifierValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerIdentifier.setIdentifierValue(null);

        // Create the CustomerIdentifier, which fails.

        restCustomerIdentifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerIdentifier.setChannel(null);

        // Create the CustomerIdentifier, which fails.

        restCustomerIdentifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerIdentifier.setVerified(null);

        // Create the CustomerIdentifier, which fails.

        restCustomerIdentifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerIdentifier.setCreatedAt(null);

        // Create the CustomerIdentifier, which fails.

        restCustomerIdentifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiers() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList
        restCustomerIdentifierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerIdentifier.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifierType").value(hasItem(DEFAULT_IDENTIFIER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identifierValue").value(hasItem(DEFAULT_IDENTIFIER_VALUE)))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED)))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].verifiedAt").value(hasItem(DEFAULT_VERIFIED_AT.toString())));
    }

    @Test
    @Transactional
    void getCustomerIdentifier() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get the customerIdentifier
        restCustomerIdentifierMockMvc
            .perform(get(ENTITY_API_URL_ID, customerIdentifier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerIdentifier.getId().intValue()))
            .andExpect(jsonPath("$.identifierType").value(DEFAULT_IDENTIFIER_TYPE.toString()))
            .andExpect(jsonPath("$.identifierValue").value(DEFAULT_IDENTIFIER_VALUE))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED))
            .andExpect(jsonPath("$.isPrimary").value(DEFAULT_IS_PRIMARY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.verifiedAt").value(DEFAULT_VERIFIED_AT.toString()));
    }

    @Test
    @Transactional
    void getCustomerIdentifiersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        Long id = customerIdentifier.getId();

        defaultCustomerIdentifierFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerIdentifierFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerIdentifierFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierType equals to
        defaultCustomerIdentifierFiltering(
            "identifierType.equals=" + DEFAULT_IDENTIFIER_TYPE,
            "identifierType.equals=" + UPDATED_IDENTIFIER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierType in
        defaultCustomerIdentifierFiltering(
            "identifierType.in=" + DEFAULT_IDENTIFIER_TYPE + "," + UPDATED_IDENTIFIER_TYPE,
            "identifierType.in=" + UPDATED_IDENTIFIER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierType is not null
        defaultCustomerIdentifierFiltering("identifierType.specified=true", "identifierType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierValue equals to
        defaultCustomerIdentifierFiltering(
            "identifierValue.equals=" + DEFAULT_IDENTIFIER_VALUE,
            "identifierValue.equals=" + UPDATED_IDENTIFIER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierValue in
        defaultCustomerIdentifierFiltering(
            "identifierValue.in=" + DEFAULT_IDENTIFIER_VALUE + "," + UPDATED_IDENTIFIER_VALUE,
            "identifierValue.in=" + UPDATED_IDENTIFIER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierValue is not null
        defaultCustomerIdentifierFiltering("identifierValue.specified=true", "identifierValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierValueContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierValue contains
        defaultCustomerIdentifierFiltering(
            "identifierValue.contains=" + DEFAULT_IDENTIFIER_VALUE,
            "identifierValue.contains=" + UPDATED_IDENTIFIER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIdentifierValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where identifierValue does not contain
        defaultCustomerIdentifierFiltering(
            "identifierValue.doesNotContain=" + UPDATED_IDENTIFIER_VALUE,
            "identifierValue.doesNotContain=" + DEFAULT_IDENTIFIER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where channel equals to
        defaultCustomerIdentifierFiltering("channel.equals=" + DEFAULT_CHANNEL, "channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where channel in
        defaultCustomerIdentifierFiltering("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL, "channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where channel is not null
        defaultCustomerIdentifierFiltering("channel.specified=true", "channel.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where verified equals to
        defaultCustomerIdentifierFiltering("verified.equals=" + DEFAULT_VERIFIED, "verified.equals=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where verified in
        defaultCustomerIdentifierFiltering("verified.in=" + DEFAULT_VERIFIED + "," + UPDATED_VERIFIED, "verified.in=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where verified is not null
        defaultCustomerIdentifierFiltering("verified.specified=true", "verified.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIsPrimaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where isPrimary equals to
        defaultCustomerIdentifierFiltering("isPrimary.equals=" + DEFAULT_IS_PRIMARY, "isPrimary.equals=" + UPDATED_IS_PRIMARY);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIsPrimaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where isPrimary in
        defaultCustomerIdentifierFiltering(
            "isPrimary.in=" + DEFAULT_IS_PRIMARY + "," + UPDATED_IS_PRIMARY,
            "isPrimary.in=" + UPDATED_IS_PRIMARY
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByIsPrimaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where isPrimary is not null
        defaultCustomerIdentifierFiltering("isPrimary.specified=true", "isPrimary.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where createdAt equals to
        defaultCustomerIdentifierFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where createdAt in
        defaultCustomerIdentifierFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where createdAt is not null
        defaultCustomerIdentifierFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByVerifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where verifiedAt equals to
        defaultCustomerIdentifierFiltering("verifiedAt.equals=" + DEFAULT_VERIFIED_AT, "verifiedAt.equals=" + UPDATED_VERIFIED_AT);
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByVerifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where verifiedAt in
        defaultCustomerIdentifierFiltering(
            "verifiedAt.in=" + DEFAULT_VERIFIED_AT + "," + UPDATED_VERIFIED_AT,
            "verifiedAt.in=" + UPDATED_VERIFIED_AT
        );
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByVerifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        // Get all the customerIdentifierList where verifiedAt is not null
        defaultCustomerIdentifierFiltering("verifiedAt.specified=true", "verifiedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerIdentifiersByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customerIdentifierRepository.saveAndFlush(customerIdentifier);
            customer = CustomerResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        customerIdentifier.setCustomer(customer);
        customerIdentifierRepository.saveAndFlush(customerIdentifier);
        Long customerId = customer.getId();
        // Get all the customerIdentifierList where customer equals to customerId
        defaultCustomerIdentifierShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerIdentifierList where customer equals to (customerId + 1)
        defaultCustomerIdentifierShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    private void defaultCustomerIdentifierFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerIdentifierShouldBeFound(shouldBeFound);
        defaultCustomerIdentifierShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerIdentifierShouldBeFound(String filter) throws Exception {
        restCustomerIdentifierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerIdentifier.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifierType").value(hasItem(DEFAULT_IDENTIFIER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identifierValue").value(hasItem(DEFAULT_IDENTIFIER_VALUE)))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED)))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].verifiedAt").value(hasItem(DEFAULT_VERIFIED_AT.toString())));

        // Check, that the count call also returns 1
        restCustomerIdentifierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerIdentifierShouldNotBeFound(String filter) throws Exception {
        restCustomerIdentifierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerIdentifierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerIdentifier() throws Exception {
        // Get the customerIdentifier
        restCustomerIdentifierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerIdentifier() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerIdentifier
        CustomerIdentifier updatedCustomerIdentifier = customerIdentifierRepository.findById(customerIdentifier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerIdentifier are not directly saved in db
        em.detach(updatedCustomerIdentifier);
        updatedCustomerIdentifier
            .identifierType(UPDATED_IDENTIFIER_TYPE)
            .identifierValue(UPDATED_IDENTIFIER_VALUE)
            .channel(UPDATED_CHANNEL)
            .verified(UPDATED_VERIFIED)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .verifiedAt(UPDATED_VERIFIED_AT);

        restCustomerIdentifierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerIdentifier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomerIdentifier))
            )
            .andExpect(status().isOk());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerIdentifierToMatchAllProperties(updatedCustomerIdentifier);
    }

    @Test
    @Transactional
    void putNonExistingCustomerIdentifier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerIdentifier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerIdentifierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerIdentifier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerIdentifier))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerIdentifier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerIdentifier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIdentifierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerIdentifier))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerIdentifier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerIdentifier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIdentifierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerIdentifierWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerIdentifier using partial update
        CustomerIdentifier partialUpdatedCustomerIdentifier = new CustomerIdentifier();
        partialUpdatedCustomerIdentifier.setId(customerIdentifier.getId());

        partialUpdatedCustomerIdentifier
            .identifierValue(UPDATED_IDENTIFIER_VALUE)
            .channel(UPDATED_CHANNEL)
            .verified(UPDATED_VERIFIED)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .verifiedAt(UPDATED_VERIFIED_AT);

        restCustomerIdentifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerIdentifier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerIdentifier))
            )
            .andExpect(status().isOk());

        // Validate the CustomerIdentifier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerIdentifierUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerIdentifier, customerIdentifier),
            getPersistedCustomerIdentifier(customerIdentifier)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerIdentifierWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerIdentifier using partial update
        CustomerIdentifier partialUpdatedCustomerIdentifier = new CustomerIdentifier();
        partialUpdatedCustomerIdentifier.setId(customerIdentifier.getId());

        partialUpdatedCustomerIdentifier
            .identifierType(UPDATED_IDENTIFIER_TYPE)
            .identifierValue(UPDATED_IDENTIFIER_VALUE)
            .channel(UPDATED_CHANNEL)
            .verified(UPDATED_VERIFIED)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .verifiedAt(UPDATED_VERIFIED_AT);

        restCustomerIdentifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerIdentifier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerIdentifier))
            )
            .andExpect(status().isOk());

        // Validate the CustomerIdentifier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerIdentifierUpdatableFieldsEquals(
            partialUpdatedCustomerIdentifier,
            getPersistedCustomerIdentifier(partialUpdatedCustomerIdentifier)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustomerIdentifier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerIdentifier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerIdentifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerIdentifier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerIdentifier))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerIdentifier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerIdentifier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIdentifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerIdentifier))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerIdentifier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerIdentifier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerIdentifierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerIdentifier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerIdentifier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerIdentifier() throws Exception {
        // Initialize the database
        insertedCustomerIdentifier = customerIdentifierRepository.saveAndFlush(customerIdentifier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerIdentifier
        restCustomerIdentifierMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerIdentifier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerIdentifierRepository.count();
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

    protected CustomerIdentifier getPersistedCustomerIdentifier(CustomerIdentifier customerIdentifier) {
        return customerIdentifierRepository.findById(customerIdentifier.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerIdentifierToMatchAllProperties(CustomerIdentifier expectedCustomerIdentifier) {
        assertCustomerIdentifierAllPropertiesEquals(expectedCustomerIdentifier, getPersistedCustomerIdentifier(expectedCustomerIdentifier));
    }

    protected void assertPersistedCustomerIdentifierToMatchUpdatableProperties(CustomerIdentifier expectedCustomerIdentifier) {
        assertCustomerIdentifierAllUpdatablePropertiesEquals(
            expectedCustomerIdentifier,
            getPersistedCustomerIdentifier(expectedCustomerIdentifier)
        );
    }
}
