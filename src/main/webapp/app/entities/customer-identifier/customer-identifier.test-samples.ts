import dayjs from 'dayjs/esm';

import { ICustomerIdentifier, NewCustomerIdentifier } from './customer-identifier.model';

export const sampleWithRequiredData: ICustomerIdentifier = {
  id: 17341,
  identifierType: 'PHONE_NUMBER',
  identifierValue: 'yearn ha',
  channel: 'WHATSAPP',
  verified: true,
  createdAt: dayjs('2025-12-13T23:11'),
};

export const sampleWithPartialData: ICustomerIdentifier = {
  id: 19786,
  identifierType: 'COMPANY_REGISTRATION',
  identifierValue: 'to',
  channel: 'USSD',
  verified: false,
  createdAt: dayjs('2025-12-14T10:17'),
};

export const sampleWithFullData: ICustomerIdentifier = {
  id: 18024,
  identifierType: 'USERNAME',
  identifierValue: 'embossing',
  channel: 'USSD',
  verified: false,
  isPrimary: false,
  createdAt: dayjs('2025-12-13T10:33'),
  verifiedAt: dayjs('2025-12-14T04:52'),
};

export const sampleWithNewData: NewCustomerIdentifier = {
  identifierType: 'NATIONAL_ID',
  identifierValue: 'over provided ugh',
  channel: 'ANDROID',
  verified: false,
  createdAt: dayjs('2025-12-13T21:34'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
