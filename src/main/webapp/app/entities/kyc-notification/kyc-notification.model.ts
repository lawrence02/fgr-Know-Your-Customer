import dayjs from 'dayjs/esm';

import { NotificationType } from 'app/entities/enumerations/notification-type.model';
import { IKycCase } from 'app/entities/kyc-case/kyc-case.model';

export interface IKycNotification {
  id: number;
  notificationType?: keyof typeof NotificationType | null;
  message?: string | null;
  sentAt?: dayjs.Dayjs | null;
  delivered?: boolean | null;
  deliveredAt?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  kycCase?: IKycCase | null;
}

export type NewKycNotification = Omit<IKycNotification, 'id'> & { id: null };
