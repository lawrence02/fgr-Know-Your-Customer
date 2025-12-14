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

/**
 * Consent tracking for compliance
 */
@Schema(description = "Consent tracking for compliance")
@Entity
@Table(name = "kyc_consent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycConsent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 2000)
    @Column(name = "consent_text", length = 2000, nullable = false)
    private String consentText;

    @NotNull
    @Column(name = "consented", nullable = false)
    private Boolean consented;

    @NotNull
    @Column(name = "consented_at", nullable = false)
    private Instant consentedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ChannelType channel;

    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Size(max = 500)
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Size(max = 10)
    @Column(name = "consent_version", length = 10)
    private String consentVersion;

    @JsonIgnoreProperties(value = { "consent", "submission", "kycDocuments", "kycNotifications", "customer" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "consent")
    private KycCase kycCase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KycConsent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsentText() {
        return this.consentText;
    }

    public KycConsent consentText(String consentText) {
        this.setConsentText(consentText);
        return this;
    }

    public void setConsentText(String consentText) {
        this.consentText = consentText;
    }

    public Boolean getConsented() {
        return this.consented;
    }

    public KycConsent consented(Boolean consented) {
        this.setConsented(consented);
        return this;
    }

    public void setConsented(Boolean consented) {
        this.consented = consented;
    }

    public Instant getConsentedAt() {
        return this.consentedAt;
    }

    public KycConsent consentedAt(Instant consentedAt) {
        this.setConsentedAt(consentedAt);
        return this;
    }

    public void setConsentedAt(Instant consentedAt) {
        this.consentedAt = consentedAt;
    }

    public ChannelType getChannel() {
        return this.channel;
    }

    public KycConsent channel(ChannelType channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(ChannelType channel) {
        this.channel = channel;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public KycConsent ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public KycConsent userAgent(String userAgent) {
        this.setUserAgent(userAgent);
        return this;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getConsentVersion() {
        return this.consentVersion;
    }

    public KycConsent consentVersion(String consentVersion) {
        this.setConsentVersion(consentVersion);
        return this;
    }

    public void setConsentVersion(String consentVersion) {
        this.consentVersion = consentVersion;
    }

    public KycCase getKycCase() {
        return this.kycCase;
    }

    public void setKycCase(KycCase kycCase) {
        if (this.kycCase != null) {
            this.kycCase.setConsent(null);
        }
        if (kycCase != null) {
            kycCase.setConsent(this);
        }
        this.kycCase = kycCase;
    }

    public KycConsent kycCase(KycCase kycCase) {
        this.setKycCase(kycCase);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycConsent)) {
            return false;
        }
        return getId() != null && getId().equals(((KycConsent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycConsent{" +
            "id=" + getId() +
            ", consentText='" + getConsentText() + "'" +
            ", consented='" + getConsented() + "'" +
            ", consentedAt='" + getConsentedAt() + "'" +
            ", channel='" + getChannel() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", consentVersion='" + getConsentVersion() + "'" +
            "}";
    }
}
