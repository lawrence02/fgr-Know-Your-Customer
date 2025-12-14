import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomer | NewCustomer> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type CustomerFormRawValue = FormValueOf<ICustomer>;

type NewCustomerFormRawValue = FormValueOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'createdAt' | 'updatedAt'>;

type CustomerFormGroupContent = {
  id: FormControl<CustomerFormRawValue['id'] | NewCustomer['id']>;
  customerRef: FormControl<CustomerFormRawValue['customerRef']>;
  customerType: FormControl<CustomerFormRawValue['customerType']>;
  fullName: FormControl<CustomerFormRawValue['fullName']>;
  dateOfBirth: FormControl<CustomerFormRawValue['dateOfBirth']>;
  idNumber: FormControl<CustomerFormRawValue['idNumber']>;
  registrationNumber: FormControl<CustomerFormRawValue['registrationNumber']>;
  address: FormControl<CustomerFormRawValue['address']>;
  phoneNumber: FormControl<CustomerFormRawValue['phoneNumber']>;
  createdAt: FormControl<CustomerFormRawValue['createdAt']>;
  updatedAt: FormControl<CustomerFormRawValue['updatedAt']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer?: CustomerFormGroupInput): CustomerFormGroup {
    const customerRawValue = this.convertCustomerToCustomerRawValue({
      ...this.getFormDefaults(),
      ...(customer ?? { id: null }),
    });
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      customerRef: new FormControl(customerRawValue.customerRef, {
        validators: [Validators.required, Validators.pattern(String.raw`^FGR-CUST-[0-9]{8}$`)],
      }),
      customerType: new FormControl(customerRawValue.customerType, {
        validators: [Validators.required],
      }),
      fullName: new FormControl(customerRawValue.fullName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      dateOfBirth: new FormControl(customerRawValue.dateOfBirth),
      idNumber: new FormControl(customerRawValue.idNumber, {
        validators: [Validators.maxLength(50)],
      }),
      registrationNumber: new FormControl(customerRawValue.registrationNumber, {
        validators: [Validators.maxLength(100)],
      }),
      address: new FormControl(customerRawValue.address, {
        validators: [Validators.maxLength(500)],
      }),
      phoneNumber: new FormControl(customerRawValue.phoneNumber, {
        validators: [Validators.maxLength(20)],
      }),
      createdAt: new FormControl(customerRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(customerRawValue.updatedAt),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return this.convertCustomerRawValueToCustomer(form.getRawValue() as CustomerFormRawValue | NewCustomerFormRawValue);
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = this.convertCustomerToCustomerRawValue({ ...this.getFormDefaults(), ...customer });
    form.reset({
      ...customerRawValue,
      id: { value: customerRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustomerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertCustomerRawValueToCustomer(rawCustomer: CustomerFormRawValue | NewCustomerFormRawValue): ICustomer | NewCustomer {
    return {
      ...rawCustomer,
      createdAt: dayjs(rawCustomer.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawCustomer.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerToCustomerRawValue(
    customer: ICustomer | (Partial<NewCustomer> & CustomerFormDefaults),
  ): CustomerFormRawValue | PartialWithRequiredKeyOf<NewCustomerFormRawValue> {
    return {
      ...customer,
      createdAt: customer.createdAt ? customer.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: customer.updatedAt ? customer.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
