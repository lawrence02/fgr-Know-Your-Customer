import dayjs from 'dayjs/esm';

import { DocumentType } from 'app/entities/enumerations/document-type.model';
import { IKycCase } from 'app/entities/kyc-case/kyc-case.model';

export interface IKycDocument {
  id: number;
  documentType?: keyof typeof DocumentType | null;
  fileName?: string | null;
  mimeType?: string | null;
  storagePath?: string | null;
  fileSize?: number | null;
  uploadedAt?: dayjs.Dayjs | null;
  expiresAt?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  deletedAt?: dayjs.Dayjs | null;
  metadata?: string | null;
  checksum?: string | null;
  kycCase?: IKycCase | null;
}

export type NewKycDocument = Omit<IKycDocument, 'id'> & { id: null };
