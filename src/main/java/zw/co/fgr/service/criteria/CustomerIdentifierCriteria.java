package zw.co.fgr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import zw.co.fgr.domain.enumeration.ChannelType;
import zw.co.fgr.domain.enumeration.IdentifierType;

/**
 * Criteria class for the {@link zw.co.fgr.domain.CustomerIdentifier} entity. This class is used
 * in {@link zw.co.fgr.web.rest.CustomerIdentifierResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-identifiers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerIdentifierCriteria implements Serializable, Criteria {

    /**
     * Class for filtering IdentifierType
     */
    public static class IdentifierTypeFilter extends Filter<IdentifierType> {

        public IdentifierTypeFilter() {}

        public IdentifierTypeFilter(IdentifierTypeFilter filter) {
            super(filter);
        }

        @Override
        public IdentifierTypeFilter copy() {
            return new IdentifierTypeFilter(this);
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

    private IdentifierTypeFilter identifierType;

    private StringFilter identifierValue;

    private ChannelTypeFilter channel;

    private BooleanFilter verified;

    private BooleanFilter isPrimary;

    private InstantFilter createdAt;

    private InstantFilter verifiedAt;

    private LongFilter customerId;

    private Boolean distinct;

    public CustomerIdentifierCriteria() {}

    public CustomerIdentifierCriteria(CustomerIdentifierCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.identifierType = other.optionalIdentifierType().map(IdentifierTypeFilter::copy).orElse(null);
        this.identifierValue = other.optionalIdentifierValue().map(StringFilter::copy).orElse(null);
        this.channel = other.optionalChannel().map(ChannelTypeFilter::copy).orElse(null);
        this.verified = other.optionalVerified().map(BooleanFilter::copy).orElse(null);
        this.isPrimary = other.optionalIsPrimary().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.verifiedAt = other.optionalVerifiedAt().map(InstantFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustomerIdentifierCriteria copy() {
        return new CustomerIdentifierCriteria(this);
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

    public IdentifierTypeFilter getIdentifierType() {
        return identifierType;
    }

    public Optional<IdentifierTypeFilter> optionalIdentifierType() {
        return Optional.ofNullable(identifierType);
    }

    public IdentifierTypeFilter identifierType() {
        if (identifierType == null) {
            setIdentifierType(new IdentifierTypeFilter());
        }
        return identifierType;
    }

    public void setIdentifierType(IdentifierTypeFilter identifierType) {
        this.identifierType = identifierType;
    }

    public StringFilter getIdentifierValue() {
        return identifierValue;
    }

    public Optional<StringFilter> optionalIdentifierValue() {
        return Optional.ofNullable(identifierValue);
    }

    public StringFilter identifierValue() {
        if (identifierValue == null) {
            setIdentifierValue(new StringFilter());
        }
        return identifierValue;
    }

    public void setIdentifierValue(StringFilter identifierValue) {
        this.identifierValue = identifierValue;
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

    public BooleanFilter getVerified() {
        return verified;
    }

    public Optional<BooleanFilter> optionalVerified() {
        return Optional.ofNullable(verified);
    }

    public BooleanFilter verified() {
        if (verified == null) {
            setVerified(new BooleanFilter());
        }
        return verified;
    }

    public void setVerified(BooleanFilter verified) {
        this.verified = verified;
    }

    public BooleanFilter getIsPrimary() {
        return isPrimary;
    }

    public Optional<BooleanFilter> optionalIsPrimary() {
        return Optional.ofNullable(isPrimary);
    }

    public BooleanFilter isPrimary() {
        if (isPrimary == null) {
            setIsPrimary(new BooleanFilter());
        }
        return isPrimary;
    }

    public void setIsPrimary(BooleanFilter isPrimary) {
        this.isPrimary = isPrimary;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getVerifiedAt() {
        return verifiedAt;
    }

    public Optional<InstantFilter> optionalVerifiedAt() {
        return Optional.ofNullable(verifiedAt);
    }

    public InstantFilter verifiedAt() {
        if (verifiedAt == null) {
            setVerifiedAt(new InstantFilter());
        }
        return verifiedAt;
    }

    public void setVerifiedAt(InstantFilter verifiedAt) {
        this.verifiedAt = verifiedAt;
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
        final CustomerIdentifierCriteria that = (CustomerIdentifierCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identifierType, that.identifierType) &&
            Objects.equals(identifierValue, that.identifierValue) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(verified, that.verified) &&
            Objects.equals(isPrimary, that.isPrimary) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(verifiedAt, that.verifiedAt) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifierType, identifierValue, channel, verified, isPrimary, createdAt, verifiedAt, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerIdentifierCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdentifierType().map(f -> "identifierType=" + f + ", ").orElse("") +
            optionalIdentifierValue().map(f -> "identifierValue=" + f + ", ").orElse("") +
            optionalChannel().map(f -> "channel=" + f + ", ").orElse("") +
            optionalVerified().map(f -> "verified=" + f + ", ").orElse("") +
            optionalIsPrimary().map(f -> "isPrimary=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalVerifiedAt().map(f -> "verifiedAt=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
