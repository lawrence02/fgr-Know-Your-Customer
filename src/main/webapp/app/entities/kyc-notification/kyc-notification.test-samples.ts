import dayjs from 'dayjs/esm';

import { IKycNotification, NewKycNotification } from './kyc-notification.model';

export const sampleWithRequiredData: IKycNotification = {
  id: 24919,
  notificationType: 'TIMEOUT_WARNING',
  message: 'yawn impish contrail',
  sentAt: dayjs('2025-12-13T13:09'),
};

export const sampleWithPartialData: IKycNotification = {
  id: 12365,
  notificationType: 'SUBMISSION_FAILED',
  message: 'eek hm',
  sentAt: dayjs('2025-12-14T06:03'),
  deliveredAt: dayjs('2025-12-13T11:16'),
  errorMessage: 'deceivingly coil overwork',
};

export const sampleWithFullData: IKycNotification = {
  id: 21143,
  notificationType: 'HELP_REQUEST',
  message: 'empty',
  sentAt: dayjs('2025-12-13T18:40'),
  delivered: false,
  deliveredAt: dayjs('2025-12-13T13:46'),
  errorMessage: 'boldly tenant',
};

export const sampleWithNewData: NewKycNotification = {
  notificationType: 'VALIDATION_ERROR',
  message: 'gah internationalize meh',
  sentAt: dayjs('2025-12-14T06:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
