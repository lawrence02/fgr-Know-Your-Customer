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
import zw.co.fgr.domain.enumeration.DocumentType;

/**
 * Document storage and tracking
 */
@Schema(description = "Document storage and tracking")
@Entity
@Table(name = "kyc_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;

    @NotNull
    @Size(max = 100)
    @Column(name = "mime_type", length = 100, nullable = false)
    private String mimeType;

    @NotNull
    @Size(max = 500)
    @Column(name = "storage_path", length = 500, nullable = false)
    private String storagePath;

    @Column(name = "file_size")
    private Long fileSize;

    @NotNull
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Lob
    @Column(name = "metadata")
    private String metadata;

    @Size(max = 64)
    @Column(name = "checksum", length = 64)
    private String checksum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "consent", "submission", "kycDocuments", "kycNotifications", "customer" }, allowSetters = true)
    private KycCase kycCase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KycDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public KycDocument documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public KycDocument fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public KycDocument mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getStoragePath() {
        return this.storagePath;
    }

    public KycDocument storagePath(String storagePath) {
        this.setStoragePath(storagePath);
        return this;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public KycDocument fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getUploadedAt() {
        return this.uploadedAt;
    }

    public KycDocument uploadedAt(Instant uploadedAt) {
        this.setUploadedAt(uploadedAt);
        return this;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public KycDocument expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public KycDocument deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public KycDocument deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public KycDocument metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public KycDocument checksum(String checksum) {
        this.setChecksum(checksum);
        return this;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public KycCase getKycCase() {
        return this.kycCase;
    }

    public void setKycCase(KycCase kycCase) {
        this.kycCase = kycCase;
    }

    public KycDocument kycCase(KycCase kycCase) {
        this.setKycCase(kycCase);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((KycDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycDocument{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", storagePath='" + getStoragePath() + "'" +
            ", fileSize=" + getFileSize() +
            ", uploadedAt='" + getUploadedAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", checksum='" + getChecksum() + "'" +
            "}";
    }
}
