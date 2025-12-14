package zw.co.fgr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import zw.co.fgr.domain.enumeration.SubmissionStatus;

/**
 * Criteria class for the {@link zw.co.fgr.domain.CdmsSubmission} entity. This class is used
 * in {@link zw.co.fgr.web.rest.CdmsSubmissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cdms-submissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CdmsSubmissionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SubmissionStatus
     */
    public static class SubmissionStatusFilter extends Filter<SubmissionStatus> {

        public SubmissionStatusFilter() {}

        public SubmissionStatusFilter(SubmissionStatusFilter filter) {
            super(filter);
        }

        @Override
        public SubmissionStatusFilter copy() {
            return new SubmissionStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter submissionRef;

    private SubmissionStatusFilter status;

    private StringFilter responseCode;

    private StringFilter responseMessage;

    private IntegerFilter attempts;

    private InstantFilter submittedAt;

    private InstantFilter lastAttemptAt;

    private InstantFilter nextRetryAt;

    private StringFilter cdmsCustomerId;

    private LongFilter kycCaseId;

    private Boolean distinct;

    public CdmsSubmissionCriteria() {}

    public CdmsSubmissionCriteria(CdmsSubmissionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.submissionRef = other.optionalSubmissionRef().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(SubmissionStatusFilter::copy).orElse(null);
        this.responseCode = other.optionalResponseCode().map(StringFilter::copy).orElse(null);
        this.responseMessage = other.optionalResponseMessage().map(StringFilter::copy).orElse(null);
        this.attempts = other.optionalAttempts().map(IntegerFilter::copy).orElse(null);
        this.submittedAt = other.optionalSubmittedAt().map(InstantFilter::copy).orElse(null);
        this.lastAttemptAt = other.optionalLastAttemptAt().map(InstantFilter::copy).orElse(null);
        this.nextRetryAt = other.optionalNextRetryAt().map(InstantFilter::copy).orElse(null);
        this.cdmsCustomerId = other.optionalCdmsCustomerId().map(StringFilter::copy).orElse(null);
        this.kycCaseId = other.optionalKycCaseId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CdmsSubmissionCriteria copy() {
        return new CdmsSubmissionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSubmissionRef() {
        return submissionRef;
    }

    public Optional<StringFilter> optionalSubmissionRef() {
        return Optional.ofNullable(submissionRef);
    }

    public StringFilter submissionRef() {
        if (submissionRef == null) {
            setSubmissionRef(new StringFilter());
        }
        return submissionRef;
    }

    public void setSubmissionRef(StringFilter submissionRef) {
        this.submissionRef = submissionRef;
    }

    public SubmissionStatusFilter getStatus() {
        return status;
    }

    public Optional<SubmissionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public SubmissionStatusFilter status() {
        if (status == null) {
            setStatus(new SubmissionStatusFilter());
        }
        return status;
    }

    public void setStatus(SubmissionStatusFilter status) {
        this.status = status;
    }

    public StringFilter getResponseCode() {
        return responseCode;
    }

    public Optional<StringFilter> optionalResponseCode() {
        return Optional.ofNullable(responseCode);
    }

    public StringFilter responseCode() {
        if (responseCode == null) {
            setResponseCode(new StringFilter());
        }
        return responseCode;
    }

    public void setResponseCode(StringFilter responseCode) {
        this.responseCode = responseCode;
    }

    public StringFilter getResponseMessage() {
        return responseMessage;
    }

    public Optional<StringFilter> optionalResponseMessage() {
        return Optional.ofNullable(responseMessage);
    }

    public StringFilter responseMessage() {
        if (responseMessage == null) {
            setResponseMessage(new StringFilter());
        }
        return responseMessage;
    }

    public void setResponseMessage(StringFilter responseMessage) {
        this.responseMessage = responseMessage;
    }

    public IntegerFilter getAttempts() {
        return attempts;
    }

    public Optional<IntegerFilter> optionalAttempts() {
        return Optional.ofNullable(attempts);
    }

    public IntegerFilter attempts() {
        if (attempts == null) {
            setAttempts(new IntegerFilter());
        }
        return attempts;
    }

    public void setAttempts(IntegerFilter attempts) {
        this.attempts = attempts;
    }

    public InstantFilter getSubmittedAt() {
        return submittedAt;
    }

    public Optional<InstantFilter> optionalSubmittedAt() {
        return Optional.ofNullable(submittedAt);
    }

    public InstantFilter submittedAt() {
        if (submittedAt == null) {
            setSubmittedAt(new InstantFilter());
        }
        return submittedAt;
    }

    public void setSubmittedAt(InstantFilter submittedAt) {
        this.submittedAt = submittedAt;
    }

    public InstantFilter getLastAttemptAt() {
        return lastAttemptAt;
    }

    public Optional<InstantFilter> optionalLastAttemptAt() {
        return Optional.ofNullable(lastAttemptAt);
    }

    public InstantFilter lastAttemptAt() {
        if (lastAttemptAt == null) {
            setLastAttemptAt(new InstantFilter());
        }
        return lastAttemptAt;
    }

    public void setLastAttemptAt(InstantFilter lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public InstantFilter getNextRetryAt() {
        return nextRetryAt;
    }

    public Optional<InstantFilter> optionalNextRetryAt() {
        return Optional.ofNullable(nextRetryAt);
    }

    public InstantFilter nextRetryAt() {
        if (nextRetryAt == null) {
            setNextRetryAt(new InstantFilter());
        }
        return nextRetryAt;
    }

    public void setNextRetryAt(InstantFilter nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
    }

    public StringFilter getCdmsCustomerId() {
        return cdmsCustomerId;
    }

    public Optional<StringFilter> optionalCdmsCustomerId() {
        return Optional.ofNullable(cdmsCustomerId);
    }

    public StringFilter cdmsCustomerId() {
        if (cdmsCustomerId == null) {
            setCdmsCustomerId(new StringFilter());
        }
        return cdmsCustomerId;
    }

    public void setCdmsCustomerId(StringFilter cdmsCustomerId) {
        this.cdmsCustomerId = cdmsCustomerId;
    }

    public LongFilter getKycCaseId() {
        return kycCaseId;
    }

    public Optional<LongFilter> optionalKycCaseId() {
        return Optional.ofNullable(kycCaseId);
    }

    public LongFilter kycCaseId() {
        if (kycCaseId == null) {
            setKycCaseId(new LongFilter());
        }
        return kycCaseId;
    }

    public void setKycCaseId(LongFilter kycCaseId) {
        this.kycCaseId = kycCaseId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CdmsSubmissionCriteria that = (CdmsSubmissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(submissionRef, that.submissionRef) &&
            Objects.equals(status, that.status) &&
            Objects.equals(responseCode, that.responseCode) &&
            Objects.equals(responseMessage, that.responseMessage) &&
            Objects.equals(attempts, that.attempts) &&
            Objects.equals(submittedAt, that.submittedAt) &&
            Objects.equals(lastAttemptAt, that.lastAttemptAt) &&
            Objects.equals(nextRetryAt, that.nextRetryAt) &&
            Objects.equals(cdmsCustomerId, that.cdmsCustomerId) &&
            Objects.equals(kycCaseId, that.kycCaseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            submissionRef,
            status,
            responseCode,
            responseMessage,
            attempts,
            submittedAt,
            lastAttemptAt,
            nextRetryAt,
            cdmsCustomerId,
            kycCaseId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CdmsSubmissionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSubmissionRef().map(f -> "submissionRef=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalResponseCode().map(f -> "responseCode=" + f + ", ").orElse("") +
            optionalResponseMessage().map(f -> "responseMessage=" + f + ", ").orElse("") +
            optionalAttempts().map(f -> "attempts=" + f + ", ").orElse("") +
            optionalSubmittedAt().map(f -> "submittedAt=" + f + ", ").orElse("") +
            optionalLastAttemptAt().map(f -> "lastAttemptAt=" + f + ", ").orElse("") +
            optionalNextRetryAt().map(f -> "nextRetryAt=" + f + ", ").orElse("") +
            optionalCdmsCustomerId().map(f -> "cdmsCustomerId=" + f + ", ").orElse("") +
            optionalKycCaseId().map(f -> "kycCaseId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
