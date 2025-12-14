package zw.co.fgr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import zw.co.fgr.domain.enumeration.CustomerType;

/**
 * Customer entity - represents miners and gold buying agents
 */
@Schema(description = "Customer entity - represents miners and gold buying agents")
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^FGR-CUST-[0-9]{8}$")
    @Column(name = "customer_ref", nullable = false, unique = true)
    private String customerRef;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    @NotNull
    @Size(max = 255)
    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 50)
    @Column(name = "id_number", length = 50)
    private String idNumber;

    @Size(max = 100)
    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Size(max = 500)
    @Column(name = "address", length = 500)
    private String address;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<CustomerIdentifier> customerIdentifiers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "consent", "submission", "kycDocuments", "kycNotifications", "customer" }, allowSetters = true)
    private Set<KycCase> kycCases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerRef() {
        return this.customerRef;
    }

    public Customer customerRef(String customerRef) {
        this.setCustomerRef(customerRef);
        return this;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public CustomerType getCustomerType() {
        return this.customerType;
    }

    public Customer customerType(CustomerType customerType) {
        this.setCustomerType(customerType);
        return this;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Customer fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Customer dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Customer idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public Customer registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public Customer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Customer createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Customer updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<CustomerIdentifier> getCustomerIdentifiers() {
        return this.customerIdentifiers;
    }

    public void setCustomerIdentifiers(Set<CustomerIdentifier> customerIdentifiers) {
        if (this.customerIdentifiers != null) {
            this.customerIdentifiers.forEach(i -> i.setCustomer(null));
        }
        if (customerIdentifiers != null) {
            customerIdentifiers.forEach(i -> i.setCustomer(this));
        }
        this.customerIdentifiers = customerIdentifiers;
    }

    public Customer customerIdentifiers(Set<CustomerIdentifier> customerIdentifiers) {
        this.setCustomerIdentifiers(customerIdentifiers);
        return this;
    }

    public Customer addCustomerIdentifier(CustomerIdentifier customerIdentifier) {
        this.customerIdentifiers.add(customerIdentifier);
        customerIdentifier.setCustomer(this);
        return this;
    }

    public Customer removeCustomerIdentifier(CustomerIdentifier customerIdentifier) {
        this.customerIdentifiers.remove(customerIdentifier);
        customerIdentifier.setCustomer(null);
        return this;
    }

    public Set<KycCase> getKycCases() {
        return this.kycCases;
    }

    public void setKycCases(Set<KycCase> kycCases) {
        if (this.kycCases != null) {
            this.kycCases.forEach(i -> i.setCustomer(null));
        }
        if (kycCases != null) {
            kycCases.forEach(i -> i.setCustomer(this));
        }
        this.kycCases = kycCases;
    }

    public Customer kycCases(Set<KycCase> kycCases) {
        this.setKycCases(kycCases);
        return this;
    }

    public Customer addKycCase(KycCase kycCase) {
        this.kycCases.add(kycCase);
        kycCase.setCustomer(this);
        return this;
    }

    public Customer removeKycCase(KycCase kycCase) {
        this.kycCases.remove(kycCase);
        kycCase.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", customerRef='" + getCustomerRef() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
