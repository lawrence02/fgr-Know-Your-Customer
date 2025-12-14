import dayjs from 'dayjs/esm';

import { ICustomer } from 'app/entities/customer/customer.model';
import { ChannelType } from 'app/entities/enumerations/channel-type.model';
import { IdentifierType } from 'app/entities/enumerations/identifier-type.model';

export interface ICustomerIdentifier {
  id: number;
  identifierType?: keyof typeof IdentifierType | null;
  identifierValue?: string | null;
  channel?: keyof typeof ChannelType | null;
  verified?: boolean | null;
  isPrimary?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  verifiedAt?: dayjs.Dayjs | null;
  customer?: ICustomer | null;
}

export type NewCustomerIdentifier = Omit<ICustomerIdentifier, 'id'> & { id: null };
