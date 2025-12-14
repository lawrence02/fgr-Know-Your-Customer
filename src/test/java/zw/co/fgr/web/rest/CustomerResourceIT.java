package zw.co.fgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zw.co.fgr.domain.CustomerAsserts.*;
import static zw.co.fgr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import zw.co.fgr.domain.enumeration.CustomerType;
import zw.co.fgr.repository.CustomerRepository;

/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_CUSTOMER_REF = "FGR-CUST-79292632";
    private static final String UPDATED_CUSTOMER_REF = "FGR-CUST-47314988";

    private static final CustomerType DEFAULT_CUSTOMER_TYPE = CustomerType.INDIVIDUAL;
    private static final CustomerType UPDATED_CUSTOMER_TYPE = CustomerType.COMPANY;

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    private Customer insertedCustomer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity() {
        return new Customer()
            .customerRef(DEFAULT_CUSTOMER_REF)
            .customerType(DEFAULT_CUSTOMER_TYPE)
            .fullName(DEFAULT_FULL_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .idNumber(DEFAULT_ID_NUMBER)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .address(DEFAULT_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity() {
        return new Customer()
            .customerRef(UPDATED_CUSTOMER_REF)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .fullName(UPDATED_FULL_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        customer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomer != null) {
            customerRepository.delete(insertedCustomer);
            insertedCustomer = null;
        }
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Customer
        var returnedCustomer = om.readValue(
            restCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Customer.class
        );

        // Validate the Customer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerUpdatableFieldsEquals(returnedCustomer, getPersistedCustomer(returnedCustomer));

        insertedCustomer = returnedCustomer;
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCustomerRefIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setCustomerRef(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setCustomerType(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setFullName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setCreatedAt(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerRef").value(hasItem(DEFAULT_CUSTOMER_REF)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customerRef").value(DEFAULT_CUSTOMER_REF))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerRefIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerRef equals to
        defaultCustomerFiltering("customerRef.equals=" + DEFAULT_CUSTOMER_REF, "customerRef.equals=" + UPDATED_CUSTOMER_REF);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerRefIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerRef in
        defaultCustomerFiltering(
            "customerRef.in=" + DEFAULT_CUSTOMER_REF + "," + UPDATED_CUSTOMER_REF,
            "customerRef.in=" + UPDATED_CUSTOMER_REF
        );
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerRef is not null
        defaultCustomerFiltering("customerRef.specified=true", "customerRef.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerRefContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerRef contains
        defaultCustomerFiltering("customerRef.contains=" + DEFAULT_CUSTOMER_REF, "customerRef.contains=" + UPDATED_CUSTOMER_REF);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerRefNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerRef does not contain
        defaultCustomerFiltering(
            "customerRef.doesNotContain=" + UPDATED_CUSTOMER_REF,
            "customerRef.doesNotContain=" + DEFAULT_CUSTOMER_REF
        );
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerType equals to
        defaultCustomerFiltering("customerType.equals=" + DEFAULT_CUSTOMER_TYPE, "customerType.equals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerType in
        defaultCustomerFiltering(
            "customerType.in=" + DEFAULT_CUSTOMER_TYPE + "," + UPDATED_CUSTOMER_TYPE,
            "customerType.in=" + UPDATED_CUSTOMER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerType is not null
        defaultCustomerFiltering("customerType.specified=true", "customerType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where fullName equals to
        defaultCustomerFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where fullName in
        defaultCustomerFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where fullName is not null
        defaultCustomerFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where fullName contains
        defaultCustomerFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where fullName does not contain
        defaultCustomerFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateOfBirth equals to
        defaultCustomerFiltering("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH, "dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCustomersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateOfBirth in
        defaultCustomerFiltering(
            "dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH,
            "dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllCustomersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateOfBirth is not null
        defaultCustomerFiltering("dateOfBirth.specified=true", "dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateOfBirth is greater than or equal to
        defaultCustomerFiltering(
            "dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllCustomersByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateOfBirth is less than or equal to
        defaultCustomerFiltering(
            "dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllCustomersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateOfBirth is less than
        defaultCustomerFiltering("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH, "dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCustomersByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateOfBirth is greater than
        defaultCustomerFiltering("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH, "dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCustomersByIdNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where idNumber equals to
        defaultCustomerFiltering("idNumber.equals=" + DEFAULT_ID_NUMBER, "idNumber.equals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByIdNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where idNumber in
        defaultCustomerFiltering("idNumber.in=" + DEFAULT_ID_NUMBER + "," + UPDATED_ID_NUMBER, "idNumber.in=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByIdNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where idNumber is not null
        defaultCustomerFiltering("idNumber.specified=true", "idNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByIdNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where idNumber contains
        defaultCustomerFiltering("idNumber.contains=" + DEFAULT_ID_NUMBER, "idNumber.contains=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByIdNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where idNumber does not contain
        defaultCustomerFiltering("idNumber.doesNotContain=" + UPDATED_ID_NUMBER, "idNumber.doesNotContain=" + DEFAULT_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationNumber equals to
        defaultCustomerFiltering(
            "registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationNumber in
        defaultCustomerFiltering(
            "registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationNumber is not null
        defaultCustomerFiltering("registrationNumber.specified=true", "registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationNumber contains
        defaultCustomerFiltering(
            "registrationNumber.contains=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.contains=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationNumber does not contain
        defaultCustomerFiltering(
            "registrationNumber.doesNotContain=" + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.doesNotContain=" + DEFAULT_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address equals to
        defaultCustomerFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address in
        defaultCustomerFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address is not null
        defaultCustomerFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address contains
        defaultCustomerFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address does not contain
        defaultCustomerFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber equals to
        defaultCustomerFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber in
        defaultCustomerFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber is not null
        defaultCustomerFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber contains
        defaultCustomerFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where phoneNumber does not contain
        defaultCustomerFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdAt equals to
        defaultCustomerFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCustomersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdAt in
        defaultCustomerFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCustomersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdAt is not null
        defaultCustomerFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where updatedAt equals to
        defaultCustomerFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCustomersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where updatedAt in
        defaultCustomerFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCustomersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where updatedAt is not null
        defaultCustomerFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    private void defaultCustomerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerShouldBeFound(shouldBeFound);
        defaultCustomerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerRef").value(hasItem(DEFAULT_CUSTOMER_REF)))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomer() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .customerRef(UPDATED_CUSTOMER_REF)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .fullName(UPDATED_FULL_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerToMatchAllProperties(updatedCustomer);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customer.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .customerRef(UPDATED_CUSTOMER_REF)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .idNumber(UPDATED_ID_NUMBER)
            .updatedAt(UPDATED_UPDATED_AT);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCustomer, customer), getPersistedCustomer(customer));
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .customerRef(UPDATED_CUSTOMER_REF)
            .customerType(UPDATED_CUSTOMER_TYPE)
            .fullName(UPDATED_FULL_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerUpdatableFieldsEquals(partialUpdatedCustomer, getPersistedCustomer(partialUpdatedCustomer));
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerRepository.count();
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

    protected Customer getPersistedCustomer(Customer customer) {
        return customerRepository.findById(customer.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerToMatchAllProperties(Customer expectedCustomer) {
        assertCustomerAllPropertiesEquals(expectedCustomer, getPersistedCustomer(expectedCustomer));
    }

    protected void assertPersistedCustomerToMatchUpdatableProperties(Customer expectedCustomer) {
        assertCustomerAllUpdatablePropertiesEquals(expectedCustomer, getPersistedCustomer(expectedCustomer));
    }
}
