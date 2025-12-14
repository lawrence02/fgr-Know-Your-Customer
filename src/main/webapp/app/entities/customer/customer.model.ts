import dayjs from 'dayjs/esm';

import { CustomerType } from 'app/entities/enumerations/customer-type.model';

export interface ICustomer {
  id: number;
  customerRef?: string | null;
  customerType?: keyof typeof CustomerType | null;
  fullName?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  idNumber?: string | null;
  registrationNumber?: string | null;
  address?: string | null;
  phoneNumber?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
