package zw.co.fgr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import zw.co.fgr.domain.enumeration.ChannelType;
import zw.co.fgr.domain.enumeration.KycStatus;

/**
 * KYC case tracking for each submission attempt
 */
@Schema(description = "KYC case tracking for each submission attempt")
@Entity
@Table(name = "kyc_case")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycCase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^FGR[0-9]{8}-[0-9]{3}$")
    @Column(name = "kyc_ref", nullable = false, unique = true)
    private String kycRef;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private KycStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ChannelType channel;

    @NotNull
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @NotNull
    @Column(name = "last_activity_at", nullable = false)
    private Instant lastActivityAt;

    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Lob
    @Column(name = "validation_errors")
    private String validationErrors;

    @Lob
    @Column(name = "internal_notes")
    private String internalNotes;

    @JsonIgnoreProperties(value = { "kycCase" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private KycConsent consent;

    @JsonIgnoreProperties(value = { "kycCase" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CdmsSubmission submission;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kycCase")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kycCase" }, allowSetters = true)
    private Set<KycDocument> kycDocuments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kycCase")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kycCase" }, allowSetters = true)
    private Set<KycNotification> kycNotifications = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customerIdentifiers", "kycCases" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KycCase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKycRef() {
        return this.kycRef;
    }

    public KycCase kycRef(String kycRef) {
        this.setKycRef(kycRef);
        return this;
    }

    public void setKycRef(String kycRef) {
        this.kycRef = kycRef;
    }

    public KycStatus getStatus() {
        return this.status;
    }

    public KycCase status(KycStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(KycStatus status) {
        this.status = status;
    }

    public ChannelType getChannel() {
        return this.channel;
    }

    public KycCase channel(ChannelType channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(ChannelType channel) {
        this.channel = channel;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public KycCase startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getLastActivityAt() {
        return this.lastActivityAt;
    }

    public KycCase lastActivityAt(Instant lastActivityAt) {
        this.setLastActivityAt(lastActivityAt);
        return this;
    }

    public void setLastActivityAt(Instant lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public Instant getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }

    public KycCase lastUpdatedAt(Instant lastUpdatedAt) {
        this.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public KycCase completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public KycCase expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getValidationErrors() {
        return this.validationErrors;
    }

    public KycCase validationErrors(String validationErrors) {
        this.setValidationErrors(validationErrors);
        return this;
    }

    public void setValidationErrors(String validationErrors) {
        this.validationErrors = validationErrors;
    }

    public String getInternalNotes() {
        return this.internalNotes;
    }

    public KycCase internalNotes(String internalNotes) {
        this.setInternalNotes(internalNotes);
        return this;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public KycConsent getConsent() {
        return this.consent;
    }

    public void setConsent(KycConsent kycConsent) {
        this.consent = kycConsent;
    }

    public KycCase consent(KycConsent kycConsent) {
        this.setConsent(kycConsent);
        return this;
    }

    public CdmsSubmission getSubmission() {
        return this.submission;
    }

    public void setSubmission(CdmsSubmission cdmsSubmission) {
        this.submission = cdmsSubmission;
    }

    public KycCase submission(CdmsSubmission cdmsSubmission) {
        this.setSubmission(cdmsSubmission);
        return this;
    }

    public Set<KycDocument> getKycDocuments() {
        return this.kycDocuments;
    }

    public void setKycDocuments(Set<KycDocument> kycDocuments) {
        if (this.kycDocuments != null) {
            this.kycDocuments.forEach(i -> i.setKycCase(null));
        }
        if (kycDocuments != null) {
            kycDocuments.forEach(i -> i.setKycCase(this));
        }
        this.kycDocuments = kycDocuments;
    }

    public KycCase kycDocuments(Set<KycDocument> kycDocuments) {
        this.setKycDocuments(kycDocuments);
        return this;
    }

    public KycCase addKycDocument(KycDocument kycDocument) {
        this.kycDocuments.add(kycDocument);
        kycDocument.setKycCase(this);
        return this;
    }

    public KycCase removeKycDocument(KycDocument kycDocument) {
        this.kycDocuments.remove(kycDocument);
        kycDocument.setKycCase(null);
        return this;
    }

    public Set<KycNotification> getKycNotifications() {
        return this.kycNotifications;
    }

    public void setKycNotifications(Set<KycNotification> kycNotifications) {
        if (this.kycNotifications != null) {
            this.kycNotifications.forEach(i -> i.setKycCase(null));
        }
        if (kycNotifications != null) {
            kycNotifications.forEach(i -> i.setKycCase(this));
        }
        this.kycNotifications = kycNotifications;
    }

    public KycCase kycNotifications(Set<KycNotification> kycNotifications) {
        this.setKycNotifications(kycNotifications);
        return this;
    }

    public KycCase addKycNotification(KycNotification kycNotification) {
        this.kycNotifications.add(kycNotification);
        kycNotification.setKycCase(this);
        return this;
    }

    public KycCase removeKycNotification(KycNotification kycNotification) {
        this.kycNotifications.remove(kycNotification);
        kycNotification.setKycCase(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public KycCase customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycCase)) {
            return false;
        }
        return getId() != null && getId().equals(((KycCase) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycCase{" +
            "id=" + getId() +
            ", kycRef='" + getKycRef() + "'" +
            ", status='" + getStatus() + "'" +
            ", channel='" + getChannel() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", lastActivityAt='" + getLastActivityAt() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", validationErrors='" + getValidationErrors() + "'" +
            ", internalNotes='" + getInternalNotes() + "'" +
            "}";
    }
}
