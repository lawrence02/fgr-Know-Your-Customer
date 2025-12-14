package zw.co.fgr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import zw.co.fgr.domain.enumeration.ChannelType;
import zw.co.fgr.domain.enumeration.KycStatus;

/**
 * Criteria class for the {@link zw.co.fgr.domain.KycCase} entity. This class is used
 * in {@link zw.co.fgr.web.rest.KycCaseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /kyc-cases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycCaseCriteria implements Serializable, Criteria {

    /**
     * Class for filtering KycStatus
     */
    public static class KycStatusFilter extends Filter<KycStatus> {

        public KycStatusFilter() {}

        public KycStatusFilter(KycStatusFilter filter) {
            super(filter);
        }

        @Override
        public KycStatusFilter copy() {
            return new KycStatusFilter(this);
        }
    }

    /**
     * Class for filtering ChannelType
     */
    public static class ChannelTypeFilter extends Filter<ChannelType> {

        public ChannelTypeFilter() {}

        public ChannelTypeFilter(ChannelTypeFilter filter) {
            super(filter);
        }

        @Override
        public ChannelTypeFilter copy() {
            return new ChannelTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter kycRef;

    private KycStatusFilter status;

    private ChannelTypeFilter channel;

    private InstantFilter startedAt;

    private InstantFilter lastActivityAt;

    private InstantFilter lastUpdatedAt;

    private InstantFilter completedAt;

    private InstantFilter expiresAt;

    private LongFilter consentId;

    private LongFilter submissionId;

    private LongFilter kycDocumentId;

    private LongFilter kycNotificationId;

    private LongFilter customerId;

    private Boolean distinct;

    public KycCaseCriteria() {}

    public KycCaseCriteria(KycCaseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.kycRef = other.optionalKycRef().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(KycStatusFilter::copy).orElse(null);
        this.channel = other.optionalChannel().map(ChannelTypeFilter::copy).orElse(null);
        this.startedAt = other.optionalStartedAt().map(InstantFilter::copy).orElse(null);
        this.lastActivityAt = other.optionalLastActivityAt().map(InstantFilter::copy).orElse(null);
        this.lastUpdatedAt = other.optionalLastUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(InstantFilter::copy).orElse(null);
        this.expiresAt = other.optionalExpiresAt().map(InstantFilter::copy).orElse(null);
        this.consentId = other.optionalConsentId().map(LongFilter::copy).orElse(null);
        this.submissionId = other.optionalSubmissionId().map(LongFilter::copy).orElse(null);
        this.kycDocumentId = other.optionalKycDocumentId().map(LongFilter::copy).orElse(null);
        this.kycNotificationId = other.optionalKycNotificationId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public KycCaseCriteria copy() {
        return new KycCaseCriteria(this);
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

    public StringFilter getKycRef() {
        return kycRef;
    }

    public Optional<StringFilter> optionalKycRef() {
        return Optional.ofNullable(kycRef);
    }

    public StringFilter kycRef() {
        if (kycRef == null) {
            setKycRef(new StringFilter());
        }
        return kycRef;
    }

    public void setKycRef(StringFilter kycRef) {
        this.kycRef = kycRef;
    }

    public KycStatusFilter getStatus() {
        return status;
    }

    public Optional<KycStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public KycStatusFilter status() {
        if (status == null) {
            setStatus(new KycStatusFilter());
        }
        return status;
    }

    public void setStatus(KycStatusFilter status) {
        this.status = status;
    }

    public ChannelTypeFilter getChannel() {
        return channel;
    }

    public Optional<ChannelTypeFilter> optionalChannel() {
        return Optional.ofNullable(channel);
    }

    public ChannelTypeFilter channel() {
        if (channel == null) {
            setChannel(new ChannelTypeFilter());
        }
        return channel;
    }

    public void setChannel(ChannelTypeFilter channel) {
        this.channel = channel;
    }

    public InstantFilter getStartedAt() {
        return startedAt;
    }

    public Optional<InstantFilter> optionalStartedAt() {
        return Optional.ofNullable(startedAt);
    }

    public InstantFilter startedAt() {
        if (startedAt == null) {
            setStartedAt(new InstantFilter());
        }
        return startedAt;
    }

    public void setStartedAt(InstantFilter startedAt) {
        this.startedAt = startedAt;
    }

    public InstantFilter getLastActivityAt() {
        return lastActivityAt;
    }

    public Optional<InstantFilter> optionalLastActivityAt() {
        return Optional.ofNullable(lastActivityAt);
    }

    public InstantFilter lastActivityAt() {
        if (lastActivityAt == null) {
            setLastActivityAt(new InstantFilter());
        }
        return lastActivityAt;
    }

    public void setLastActivityAt(InstantFilter lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public InstantFilter getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public Optional<InstantFilter> optionalLastUpdatedAt() {
        return Optional.ofNullable(lastUpdatedAt);
    }

    public InstantFilter lastUpdatedAt() {
        if (lastUpdatedAt == null) {
            setLastUpdatedAt(new InstantFilter());
        }
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(InstantFilter lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<InstantFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public InstantFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new InstantFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
    }

    public InstantFilter getExpiresAt() {
        return expiresAt;
    }

    public Optional<InstantFilter> optionalExpiresAt() {
        return Optional.ofNullable(expiresAt);
    }

    public InstantFilter expiresAt() {
        if (expiresAt == null) {
            setExpiresAt(new InstantFilter());
        }
        return expiresAt;
    }

    public void setExpiresAt(InstantFilter expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LongFilter getConsentId() {
        return consentId;
    }

    public Optional<LongFilter> optionalConsentId() {
        return Optional.ofNullable(consentId);
    }

    public LongFilter consentId() {
        if (consentId == null) {
            setConsentId(new LongFilter());
        }
        return consentId;
    }

    public void setConsentId(LongFilter consentId) {
        this.consentId = consentId;
    }

    public LongFilter getSubmissionId() {
        return submissionId;
    }

    public Optional<LongFilter> optionalSubmissionId() {
        return Optional.ofNullable(submissionId);
    }

    public LongFilter submissionId() {
        if (submissionId == null) {
            setSubmissionId(new LongFilter());
        }
        return submissionId;
    }

    public void setSubmissionId(LongFilter submissionId) {
        this.submissionId = submissionId;
    }

    public LongFilter getKycDocumentId() {
        return kycDocumentId;
    }

    public Optional<LongFilter> optionalKycDocumentId() {
        return Optional.ofNullable(kycDocumentId);
    }

    public LongFilter kycDocumentId() {
        if (kycDocumentId == null) {
            setKycDocumentId(new LongFilter());
        }
        return kycDocumentId;
    }

    public void setKycDocumentId(LongFilter kycDocumentId) {
        this.kycDocumentId = kycDocumentId;
    }

    public LongFilter getKycNotificationId() {
        return kycNotificationId;
    }

    public Optional<LongFilter> optionalKycNotificationId() {
        return Optional.ofNullable(kycNotificationId);
    }

    public LongFilter kycNotificationId() {
        if (kycNotificationId == null) {
            setKycNotificationId(new LongFilter());
        }
        return kycNotificationId;
    }

    public void setKycNotificationId(LongFilter kycNotificationId) {
        this.kycNotificationId = kycNotificationId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
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
        final KycCaseCriteria that = (KycCaseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(kycRef, that.kycRef) &&
            Objects.equals(status, that.status) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(startedAt, that.startedAt) &&
            Objects.equals(lastActivityAt, that.lastActivityAt) &&
            Objects.equals(lastUpdatedAt, that.lastUpdatedAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(expiresAt, that.expiresAt) &&
            Objects.equals(consentId, that.consentId) &&
            Objects.equals(submissionId, that.submissionId) &&
            Objects.equals(kycDocumentId, that.kycDocumentId) &&
            Objects.equals(kycNotificationId, that.kycNotificationId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            kycRef,
            status,
            channel,
            startedAt,
            lastActivityAt,
            lastUpdatedAt,
            completedAt,
            expiresAt,
            consentId,
            submissionId,
            kycDocumentId,
            kycNotificationId,
            customerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycCaseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalKycRef().map(f -> "kycRef=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalChannel().map(f -> "channel=" + f + ", ").orElse("") +
            optionalStartedAt().map(f -> "startedAt=" + f + ", ").orElse("") +
            optionalLastActivityAt().map(f -> "lastActivityAt=" + f + ", ").orElse("") +
            optionalLastUpdatedAt().map(f -> "lastUpdatedAt=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalExpiresAt().map(f -> "expiresAt=" + f + ", ").orElse("") +
            optionalConsentId().map(f -> "consentId=" + f + ", ").orElse("") +
            optionalSubmissionId().map(f -> "submissionId=" + f + ", ").orElse("") +
            optionalKycDocumentId().map(f -> "kycDocumentId=" + f + ", ").orElse("") +
            optionalKycNotificationId().map(f -> "kycNotificationId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
