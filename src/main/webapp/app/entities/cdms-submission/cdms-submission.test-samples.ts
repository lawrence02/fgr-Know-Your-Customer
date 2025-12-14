import dayjs from 'dayjs/esm';

import { ICdmsSubmission, NewCdmsSubmission } from './cdms-submission.model';

export const sampleWithRequiredData: ICdmsSubmission = {
  id: 17450,
  submissionRef: 'account',
  status: 'PENDING',
  attempts: 2,
};

export const sampleWithPartialData: ICdmsSubmission = {
  id: 28609,
  submissionRef: 'loudly',
  status: 'SUCCESS',
  responseMessage: 'crystallize capitalize tabletop',
  attempts: 0,
  nextRetryAt: dayjs('2025-12-14T08:19'),
};

export const sampleWithFullData: ICdmsSubmission = {
  id: 15300,
  submissionRef: 'order',
  status: 'PENDING',
  responseCode: 'except ick before',
  responseMessage: 'restfully except',
  attempts: 3,
  submittedAt: dayjs('2025-12-13T22:16'),
  lastAttemptAt: dayjs('2025-12-13T18:30'),
  nextRetryAt: dayjs('2025-12-13T14:26'),
  cdmsCustomerId: 'and',
};

export const sampleWithNewData: NewCdmsSubmission = {
  submissionRef: 'ribbon equally',
  status: 'FAILED',
  attempts: 2,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
