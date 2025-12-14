import dayjs from 'dayjs/esm';

import { IKycDocument, NewKycDocument } from './kyc-document.model';

export const sampleWithRequiredData: IKycDocument = {
  id: 9517,
  documentType: 'PASSPORT',
  fileName: 'nor selfishly gratefully',
  mimeType: 'afore',
  storagePath: 'whether decouple',
  uploadedAt: dayjs('2025-12-13T17:25'),
  expiresAt: dayjs('2025-12-14T09:58'),
};

export const sampleWithPartialData: IKycDocument = {
  id: 3865,
  documentType: 'COMPANY_REGISTRATION',
  fileName: 'wherever ameliorate woefully',
  mimeType: 'serene multicolored though',
  storagePath: 'unless',
  fileSize: 19127,
  uploadedAt: dayjs('2025-12-14T02:21'),
  expiresAt: dayjs('2025-12-14T01:30'),
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IKycDocument = {
  id: 838,
  documentType: 'MINE_LICENSE',
  fileName: 'onto',
  mimeType: 'a syringe',
  storagePath: 'burdensome',
  fileSize: 22113,
  uploadedAt: dayjs('2025-12-13T15:56'),
  expiresAt: dayjs('2025-12-14T02:20'),
  deleted: false,
  deletedAt: dayjs('2025-12-13T15:44'),
  metadata: '../fake-data/blob/hipster.txt',
  checksum: 'lest general',
};

export const sampleWithNewData: NewKycDocument = {
  documentType: 'PROOF_OF_ADDRESS',
  fileName: 'mmm inside schnitzel',
  mimeType: 'knottily',
  storagePath: 'come famously frenetically',
  uploadedAt: dayjs('2025-12-14T08:05'),
  expiresAt: dayjs('2025-12-13T12:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
