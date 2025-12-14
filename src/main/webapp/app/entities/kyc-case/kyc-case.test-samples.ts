import dayjs from 'dayjs/esm';

import { IKycCase, NewKycCase } from './kyc-case.model';

export const sampleWithRequiredData: IKycCase = {
  id: 11584,
  kycRef: 'FGR63716128-903',
  status: 'APPROVED',
  channel: 'WEB',
  startedAt: dayjs('2025-12-13T16:54'),
  lastActivityAt: dayjs('2025-12-14T08:34'),
};

export const sampleWithPartialData: IKycCase = {
  id: 4244,
  kycRef: 'FGR29312797-196',
  status: 'REJECTED',
  channel: 'WHATSAPP',
  startedAt: dayjs('2025-12-14T00:46'),
  lastActivityAt: dayjs('2025-12-14T10:13'),
  lastUpdatedAt: dayjs('2025-12-13T20:39'),
  expiresAt: dayjs('2025-12-13T14:27'),
  validationErrors: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IKycCase = {
  id: 7565,
  kycRef: 'FGR41376023-927',
  status: 'STARTED',
  channel: 'ANDROID',
  startedAt: dayjs('2025-12-13T12:17'),
  lastActivityAt: dayjs('2025-12-13T22:11'),
  lastUpdatedAt: dayjs('2025-12-13T12:51'),
  completedAt: dayjs('2025-12-13T19:49'),
  expiresAt: dayjs('2025-12-14T00:16'),
  validationErrors: '../fake-data/blob/hipster.txt',
  internalNotes: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewKycCase = {
  kycRef: 'FGR39405071-529',
  status: 'AWAITING_DOCUMENTS',
  channel: 'WEB',
  startedAt: dayjs('2025-12-14T09:04'),
  lastActivityAt: dayjs('2025-12-13T15:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
