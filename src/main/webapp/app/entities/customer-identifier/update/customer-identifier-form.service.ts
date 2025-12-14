import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICustomerIdentifier, NewCustomerIdentifier } from '../customer-identifier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerIdentifier for edit and NewCustomerIdentifierFormGroupInput for create.
 */
type CustomerIdentifierFormGroupInput = ICustomerIdentifier | PartialWithRequiredKeyOf<NewCustomerIdentifier>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomerIdentifier | NewCustomerIdentifier> = Omit<T, 'createdAt' | 'verifiedAt'> & {
  createdAt?: string | null;
  verifiedAt?: string | null;
};

type CustomerIdentifierFormRawValue = FormValueOf<ICustomerIdentifier>;

type NewCustomerIdentifierFormRawValue = FormValueOf<NewCustomerIdentifier>;

type CustomerIdentifierFormDefaults = Pick<NewCustomerIdentifier, 'id' | 'verified' | 'isPrimary' | 'createdAt' | 'verifiedAt'>;

type CustomerIdentifierFormGroupContent = {
  id: FormControl<CustomerIdentifierFormRawValue['id'] | NewCustomerIdentifier['id']>;
  identifierType: FormControl<CustomerIdentifierFormRawValue['identifierType']>;
  identifierValue: FormControl<CustomerIdentifierFormRawValue['identifierValue']>;
  channel: FormControl<CustomerIdentifierFormRawValue['channel']>;
  verified: FormControl<CustomerIdentifierFormRawValue['verified']>;
  isPrimary: FormControl<CustomerIdentifierFormRawValue['isPrimary']>;
  createdAt: FormControl<CustomerIdentifierFormRawValue['createdAt']>;
  verifiedAt: FormControl<CustomerIdentifierFormRawValue['verifiedAt']>;
  customer: FormControl<CustomerIdentifierFormRawValue['customer']>;
};

export type CustomerIdentifierFormGroup = FormGroup<CustomerIdentifierFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerIdentifierFormService {
  createCustomerIdentifierFormGroup(customerIdentifier?: CustomerIdentifierFormGroupInput): CustomerIdentifierFormGroup {
    const customerIdentifierRawValue = this.convertCustomerIdentifierToCustomerIdentifierRawValue({
      ...this.getFormDefaults(),
      ...(customerIdentifier ?? { id: null }),
    });
    return new FormGroup<CustomerIdentifierFormGroupContent>({
      id: new FormControl(
        { value: customerIdentifierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      identifierType: new FormControl(customerIdentifierRawValue.identifierType, {
        validators: [Validators.required],
      }),
      identifierValue: new FormControl(customerIdentifierRawValue.identifierValue, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      channel: new FormControl(customerIdentifierRawValue.channel, {
        validators: [Validators.required],
      }),
      verified: new FormControl(customerIdentifierRawValue.verified, {
        validators: [Validators.required],
      }),
      isPrimary: new FormControl(customerIdentifierRawValue.isPrimary),
      createdAt: new FormControl(customerIdentifierRawValue.createdAt, {
        validators: [Validators.required],
      }),
      verifiedAt: new FormControl(customerIdentifierRawValue.verifiedAt),
      customer: new FormControl(customerIdentifierRawValue.customer),
    });
  }

  getCustomerIdentifier(form: CustomerIdentifierFormGroup): ICustomerIdentifier | NewCustomerIdentifier {
    return this.convertCustomerIdentifierRawValueToCustomerIdentifier(
      form.getRawValue() as CustomerIdentifierFormRawValue | NewCustomerIdentifierFormRawValue,
    );
  }

  resetForm(form: CustomerIdentifierFormGroup, customerIdentifier: CustomerIdentifierFormGroupInput): void {
    const customerIdentifierRawValue = this.convertCustomerIdentifierToCustomerIdentifierRawValue({
      ...this.getFormDefaults(),
      ...customerIdentifier,
    });
    form.reset({
      ...customerIdentifierRawValue,
      id: { value: customerIdentifierRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustomerIdentifierFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      verified: false,
      isPrimary: false,
      createdAt: currentTime,
      verifiedAt: currentTime,
    };
  }

  private convertCustomerIdentifierRawValueToCustomerIdentifier(
    rawCustomerIdentifier: CustomerIdentifierFormRawValue | NewCustomerIdentifierFormRawValue,
  ): ICustomerIdentifier | NewCustomerIdentifier {
    return {
      ...rawCustomerIdentifier,
      createdAt: dayjs(rawCustomerIdentifier.createdAt, DATE_TIME_FORMAT),
      verifiedAt: dayjs(rawCustomerIdentifier.verifiedAt, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerIdentifierToCustomerIdentifierRawValue(
    customerIdentifier: ICustomerIdentifier | (Partial<NewCustomerIdentifier> & CustomerIdentifierFormDefaults),
  ): CustomerIdentifierFormRawValue | PartialWithRequiredKeyOf<NewCustomerIdentifierFormRawValue> {
    return {
      ...customerIdentifier,
      createdAt: customerIdentifier.createdAt ? customerIdentifier.createdAt.format(DATE_TIME_FORMAT) : undefined,
      verifiedAt: customerIdentifier.verifiedAt ? customerIdentifier.verifiedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
