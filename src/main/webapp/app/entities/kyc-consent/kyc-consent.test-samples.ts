import dayjs from 'dayjs/esm';

import { IKycConsent, NewKycConsent } from './kyc-consent.model';

export const sampleWithRequiredData: IKycConsent = {
  id: 19404,
  consentText: 'rightfully',
  consented: false,
  consentedAt: dayjs('2025-12-13T23:17'),
  channel: 'WEB',
};

export const sampleWithPartialData: IKycConsent = {
  id: 26423,
  consentText: 'finished',
  consented: true,
  consentedAt: dayjs('2025-12-13T11:35'),
  channel: 'ANDROID',
  ipAddress: 'oof formamide unlawful',
  consentVersion: 'excitedly',
};

export const sampleWithFullData: IKycConsent = {
  id: 28266,
  consentText: 'disinherit',
  consented: false,
  consentedAt: dayjs('2025-12-13T18:59'),
  channel: 'WHATSAPP',
  ipAddress: 'at',
  userAgent: 'propound yowza anti',
  consentVersion: 'wealthy wh',
};

export const sampleWithNewData: NewKycConsent = {
  consentText: 'whoever',
  consented: false,
  consentedAt: dayjs('2025-12-14T06:21'),
  channel: 'WEB',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
