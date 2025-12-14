import dayjs from 'dayjs/esm';

import { SubmissionStatus } from 'app/entities/enumerations/submission-status.model';

export interface ICdmsSubmission {
  id: number;
  submissionRef?: string | null;
  status?: keyof typeof SubmissionStatus | null;
  responseCode?: string | null;
  responseMessage?: string | null;
  attempts?: number | null;
  submittedAt?: dayjs.Dayjs | null;
  lastAttemptAt?: dayjs.Dayjs | null;
  nextRetryAt?: dayjs.Dayjs | null;
  cdmsCustomerId?: string | null;
}

export type NewCdmsSubmission = Omit<ICdmsSubmission, 'id'> & { id: null };
