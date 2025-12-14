package zw.co.fgr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import zw.co.fgr.domain.enumeration.ChannelType;
import zw.co.fgr.domain.enumeration.IdentifierType;

/**
 * Customer identifiers across different channels
 */
@Schema(description = "Customer identifiers across different channels")
@Entity
@Table(name = "customer_identifier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerIdentifier implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "identifier_type", nullable = false)
    private IdentifierType identifierType;

    @NotNull
    @Size(max = 255)
    @Column(name = "identifier_value", length = 255, nullable = false)
    private String identifierValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ChannelType channel;

    @NotNull
    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customerIdentifiers", "kycCases" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerIdentifier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdentifierType getIdentifierType() {
        return this.identifierType;
    }

    public CustomerIdentifier identifierType(IdentifierType identifierType) {
        this.setIdentifierType(identifierType);
        return this;
    }

    public void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierValue() {
        return this.identifierValue;
    }

    public CustomerIdentifier identifierValue(String identifierValue) {
        this.setIdentifierValue(identifierValue);
        return this;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }

    public ChannelType getChannel() {
        return this.channel;
    }

    public CustomerIdentifier channel(ChannelType channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(ChannelType channel) {
        this.channel = channel;
    }

    public Boolean getVerified() {
        return this.verified;
    }

    public CustomerIdentifier verified(Boolean verified) {
        this.setVerified(verified);
        return this;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getIsPrimary() {
        return this.isPrimary;
    }

    public CustomerIdentifier isPrimary(Boolean isPrimary) {
        this.setIsPrimary(isPrimary);
        return this;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CustomerIdentifier createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getVerifiedAt() {
        return this.verifiedAt;
    }

    public CustomerIdentifier verifiedAt(Instant verifiedAt) {
        this.setVerifiedAt(verifiedAt);
        return this;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerIdentifier customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerIdentifier)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomerIdentifier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerIdentifier{" +
            "id=" + getId() +
            ", identifierType='" + getIdentifierType() + "'" +
            ", identifierValue='" + getIdentifierValue() + "'" +
            ", channel='" + getChannel() + "'" +
            ", verified='" + getVerified() + "'" +
            ", isPrimary='" + getIsPrimary() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", verifiedAt='" + getVerifiedAt() + "'" +
            "}";
    }
}
