import dayjs from 'dayjs/esm';

import { ChannelType } from 'app/entities/enumerations/channel-type.model';

export interface IKycConsent {
  id: number;
  consentText?: string | null;
  consented?: boolean | null;
  consentedAt?: dayjs.Dayjs | null;
  channel?: keyof typeof ChannelType | null;
  ipAddress?: string | null;
  userAgent?: string | null;
  consentVersion?: string | null;
}

export type NewKycConsent = Omit<IKycConsent, 'id'> & { id: null };
