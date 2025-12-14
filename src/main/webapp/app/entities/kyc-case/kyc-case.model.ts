import dayjs from 'dayjs/esm';

import { ICdmsSubmission } from 'app/entities/cdms-submission/cdms-submission.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { ChannelType } from 'app/entities/enumerations/channel-type.model';
import { KycStatus } from 'app/entities/enumerations/kyc-status.model';
import { IKycConsent } from 'app/entities/kyc-consent/kyc-consent.model';

export interface IKycCase {
  id: number;
  kycRef?: string | null;
  status?: keyof typeof KycStatus | null;
  channel?: keyof typeof ChannelType | null;
  startedAt?: dayjs.Dayjs | null;
  lastActivityAt?: dayjs.Dayjs | null;
  lastUpdatedAt?: dayjs.Dayjs | null;
  completedAt?: dayjs.Dayjs | null;
  expiresAt?: dayjs.Dayjs | null;
  validationErrors?: string | null;
  internalNotes?: string | null;
  consent?: IKycConsent | null;
  submission?: ICdmsSubmission | null;
  customer?: ICustomer | null;
}

export type NewKycCase = Omit<IKycCase, 'id'> & { id: null };
