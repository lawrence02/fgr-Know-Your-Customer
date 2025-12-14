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
import zw.co.fgr.domain.enumeration.NotificationType;

/**
 * Notification and communication tracking
 */
@Schema(description = "Notification and communication tracking")
@Entity
@Table(name = "kyc_notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycNotification implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @NotNull
    @Size(max = 1000)
    @Column(name = "message", length = 1000, nullable = false)
    private String message;

    @NotNull
    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    @Column(name = "delivered")
    private Boolean delivered;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Size(max = 500)
    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "consent", "submission", "kycDocuments", "kycNotifications", "customer" }, allowSetters = true)
    private KycCase kycCase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KycNotification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public KycNotification notificationType(NotificationType notificationType) {
        this.setNotificationType(notificationType);
        return this;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return this.message;
    }

    public KycNotification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getSentAt() {
        return this.sentAt;
    }

    public KycNotification sentAt(Instant sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getDelivered() {
        return this.delivered;
    }

    public KycNotification delivered(Boolean delivered) {
        this.setDelivered(delivered);
        return this;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Instant getDeliveredAt() {
        return this.deliveredAt;
    }

    public KycNotification deliveredAt(Instant deliveredAt) {
        this.setDeliveredAt(deliveredAt);
        return this;
    }

    public void setDeliveredAt(Instant deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public KycNotification errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public KycCase getKycCase() {
        return this.kycCase;
    }

    public void setKycCase(KycCase kycCase) {
        this.kycCase = kycCase;
    }

    public KycNotification kycCase(KycCase kycCase) {
        this.setKycCase(kycCase);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycNotification)) {
            return false;
        }
        return getId() != null && getId().equals(((KycNotification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycNotification{" +
            "id=" + getId() +
            ", notificationType='" + getNotificationType() + "'" +
            ", message='" + getMessage() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", delivered='" + getDelivered() + "'" +
            ", deliveredAt='" + getDeliveredAt() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
