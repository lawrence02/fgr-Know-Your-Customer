package zw.co.fgr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import zw.co.fgr.domain.enumeration.CustomerType;

/**
 * Criteria class for the {@link zw.co.fgr.domain.Customer} entity. This class is used
 * in {@link zw.co.fgr.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CustomerType
     */
    public static class CustomerTypeFilter extends Filter<CustomerType> {

        public CustomerTypeFilter() {}

        public CustomerTypeFilter(CustomerTypeFilter filter) {
            super(filter);
        }

        @Override
        public CustomerTypeFilter copy() {
            return new CustomerTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter customerRef;

    private CustomerTypeFilter customerType;

    private StringFilter fullName;

    private LocalDateFilter dateOfBirth;

    private StringFilter idNumber;

    private StringFilter registrationNumber;

    private StringFilter address;

    private StringFilter phoneNumber;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter customerIdentifierId;

    private LongFilter kycCaseId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.customerRef = other.optionalCustomerRef().map(StringFilter::copy).orElse(null);
        this.customerType = other.optionalCustomerType().map(CustomerTypeFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.dateOfBirth = other.optionalDateOfBirth().map(LocalDateFilter::copy).orElse(null);
        this.idNumber = other.optionalIdNumber().map(StringFilter::copy).orElse(null);
        this.registrationNumber = other.optionalRegistrationNumber().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.customerIdentifierId = other.optionalCustomerIdentifierId().map(LongFilter::copy).orElse(null);
        this.kycCaseId = other.optionalKycCaseId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public StringFilter getCustomerRef() {
        return customerRef;
    }

    public Optional<StringFilter> optionalCustomerRef() {
        return Optional.ofNullable(customerRef);
    }

    public StringFilter customerRef() {
        if (customerRef == null) {
            setCustomerRef(new StringFilter());
        }
        return customerRef;
    }

    public void setCustomerRef(StringFilter customerRef) {
        this.customerRef = customerRef;
    }

    public CustomerTypeFilter getCustomerType() {
        return customerType;
    }

    public Optional<CustomerTypeFilter> optionalCustomerType() {
        return Optional.ofNullable(customerType);
    }

    public CustomerTypeFilter customerType() {
        if (customerType == null) {
            setCustomerType(new CustomerTypeFilter());
        }
        return customerType;
    }

    public void setCustomerType(CustomerTypeFilter customerType) {
        this.customerType = customerType;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<LocalDateFilter> optionalDateOfBirth() {
        return Optional.ofNullable(dateOfBirth);
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            setDateOfBirth(new LocalDateFilter());
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public StringFilter getIdNumber() {
        return idNumber;
    }

    public Optional<StringFilter> optionalIdNumber() {
        return Optional.ofNullable(idNumber);
    }

    public StringFilter idNumber() {
        if (idNumber == null) {
            setIdNumber(new StringFilter());
        }
        return idNumber;
    }

    public void setIdNumber(StringFilter idNumber) {
        this.idNumber = idNumber;
    }

    public StringFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public Optional<StringFilter> optionalRegistrationNumber() {
        return Optional.ofNullable(registrationNumber);
    }

    public StringFilter registrationNumber() {
        if (registrationNumber == null) {
            setRegistrationNumber(new StringFilter());
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(StringFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getCustomerIdentifierId() {
        return customerIdentifierId;
    }

    public Optional<LongFilter> optionalCustomerIdentifierId() {
        return Optional.ofNullable(customerIdentifierId);
    }

    public LongFilter customerIdentifierId() {
        if (customerIdentifierId == null) {
            setCustomerIdentifierId(new LongFilter());
        }
        return customerIdentifierId;
    }

    public void setCustomerIdentifierId(LongFilter customerIdentifierId) {
        this.customerIdentifierId = customerIdentifierId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerRef, that.customerRef) &&
            Objects.equals(customerType, that.customerType) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(idNumber, that.idNumber) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(customerIdentifierId, that.customerIdentifierId) &&
            Objects.equals(kycCaseId, that.kycCaseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            customerRef,
            customerType,
            fullName,
            dateOfBirth,
            idNumber,
            registrationNumber,
            address,
            phoneNumber,
            createdAt,
            updatedAt,
            customerIdentifierId,
            kycCaseId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCustomerRef().map(f -> "customerRef=" + f + ", ").orElse("") +
            optionalCustomerType().map(f -> "customerType=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalDateOfBirth().map(f -> "dateOfBirth=" + f + ", ").orElse("") +
            optionalIdNumber().map(f -> "idNumber=" + f + ", ").orElse("") +
            optionalRegistrationNumber().map(f -> "registrationNumber=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalCustomerIdentifierId().map(f -> "customerIdentifierId=" + f + ", ").orElse("") +
            optionalKycCaseId().map(f -> "kycCaseId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
