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
import zw.co.fgr.domain.enumeration.SubmissionStatus;

/**
 * CDMS API submission tracking
 */
@Schema(description = "CDMS API submission tracking")
@Entity
@Table(name = "cdms_submission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CdmsSubmission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "submission_ref", nullable = false, unique = true)
    private String submissionRef;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubmissionStatus status;

    @Size(max = 50)
    @Column(name = "response_code", length = 50)
    private String responseCode;

    @Size(max = 1000)
    @Column(name = "response_message", length = 1000)
    private String responseMessage;

    @NotNull
    @Min(value = 0)
    @Max(value = 3)
    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    @Size(max = 100)
    @Column(name = "cdms_customer_id", length = 100)
    private String cdmsCustomerId;

    @JsonIgnoreProperties(value = { "consent", "submission", "kycDocuments", "kycNotifications", "customer" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "submission")
    private KycCase kycCase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CdmsSubmission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmissionRef() {
        return this.submissionRef;
    }

    public CdmsSubmission submissionRef(String submissionRef) {
        this.setSubmissionRef(submissionRef);
        return this;
    }

    public void setSubmissionRef(String submissionRef) {
        this.submissionRef = submissionRef;
    }

    public SubmissionStatus getStatus() {
        return this.status;
    }

    public CdmsSubmission status(SubmissionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public CdmsSubmission responseCode(String responseCode) {
        this.setResponseCode(responseCode);
        return this;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public CdmsSubmission responseMessage(String responseMessage) {
        this.setResponseMessage(responseMessage);
        return this;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getAttempts() {
        return this.attempts;
    }

    public CdmsSubmission attempts(Integer attempts) {
        this.setAttempts(attempts);
        return this;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Instant getSubmittedAt() {
        return this.submittedAt;
    }

    public CdmsSubmission submittedAt(Instant submittedAt) {
        this.setSubmittedAt(submittedAt);
        return this;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getLastAttemptAt() {
        return this.lastAttemptAt;
    }

    public CdmsSubmission lastAttemptAt(Instant lastAttemptAt) {
        this.setLastAttemptAt(lastAttemptAt);
        return this;
    }

    public void setLastAttemptAt(Instant lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public Instant getNextRetryAt() {
        return this.nextRetryAt;
    }

    public CdmsSubmission nextRetryAt(Instant nextRetryAt) {
        this.setNextRetryAt(nextRetryAt);
        return this;
    }

    public void setNextRetryAt(Instant nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
    }

    public String getCdmsCustomerId() {
        return this.cdmsCustomerId;
    }

    public CdmsSubmission cdmsCustomerId(String cdmsCustomerId) {
        this.setCdmsCustomerId(cdmsCustomerId);
        return this;
    }

    public void setCdmsCustomerId(String cdmsCustomerId) {
        this.cdmsCustomerId = cdmsCustomerId;
    }

    public KycCase getKycCase() {
        return this.kycCase;
    }

    public void setKycCase(KycCase kycCase) {
        if (this.kycCase != null) {
            this.kycCase.setSubmission(null);
        }
        if (kycCase != null) {
            kycCase.setSubmission(this);
        }
        this.kycCase = kycCase;
    }

    public CdmsSubmission kycCase(KycCase kycCase) {
        this.setKycCase(kycCase);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CdmsSubmission)) {
            return false;
        }
        return getId() != null && getId().equals(((CdmsSubmission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CdmsSubmission{" +
            "id=" + getId() +
            ", submissionRef='" + getSubmissionRef() + "'" +
            ", status='" + getStatus() + "'" +
            ", responseCode='" + getResponseCode() + "'" +
            ", responseMessage='" + getResponseMessage() + "'" +
            ", attempts=" + getAttempts() +
            ", submittedAt='" + getSubmittedAt() + "'" +
            ", lastAttemptAt='" + getLastAttemptAt() + "'" +
            ", nextRetryAt='" + getNextRetryAt() + "'" +
            ", cdmsCustomerId='" + getCdmsCustomerId() + "'" +
            "}";
    }
}
