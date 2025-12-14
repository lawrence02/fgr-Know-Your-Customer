import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  customerRef: 'FGR-CUST-67525298',
  customerType: 'COMPANY',
  fullName: 'kiddingly sans',
  createdAt: dayjs('2025-12-13T14:35'),
};

export const sampleWithPartialData: ICustomer = {
  id: 31728,
  customerRef: 'FGR-CUST-96641343',
  customerType: 'COMPANY',
  fullName: 'sinful',
  phoneNumber: 'smoggy yearly delibe',
  createdAt: dayjs('2025-12-13T21:55'),
  updatedAt: dayjs('2025-12-13T21:14'),
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  customerRef: 'FGR-CUST-61592068',
  customerType: 'INDIVIDUAL',
  fullName: 'bah',
  dateOfBirth: dayjs('2025-12-14'),
  idNumber: 'fooey nerve best',
  registrationNumber: 'for',
  address: 'lender phew devoted',
  phoneNumber: 'bakeware',
  createdAt: dayjs('2025-12-14T02:30'),
  updatedAt: dayjs('2025-12-13T16:59'),
};

export const sampleWithNewData: NewCustomer = {
  customerRef: 'FGR-CUST-47158439',
  customerType: 'COMPANY',
  fullName: 'the worth to',
  createdAt: dayjs('2025-12-13T18:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
