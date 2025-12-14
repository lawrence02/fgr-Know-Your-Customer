import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IKycCase, NewKycCase } from '../kyc-case.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKycCase for edit and NewKycCaseFormGroupInput for create.
 */
type KycCaseFormGroupInput = IKycCase | PartialWithRequiredKeyOf<NewKycCase>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IKycCase | NewKycCase> = Omit<
  T,
  'startedAt' | 'lastActivityAt' | 'lastUpdatedAt' | 'completedAt' | 'expiresAt'
> & {
  startedAt?: string | null;
  lastActivityAt?: string | null;
  lastUpdatedAt?: string | null;
  completedAt?: string | null;
  expiresAt?: string | null;
};

type KycCaseFormRawValue = FormValueOf<IKycCase>;

type NewKycCaseFormRawValue = FormValueOf<NewKycCase>;

type KycCaseFormDefaults = Pick<NewKycCase, 'id' | 'startedAt' | 'lastActivityAt' | 'lastUpdatedAt' | 'completedAt' | 'expiresAt'>;

type KycCaseFormGroupContent = {
  id: FormControl<KycCaseFormRawValue['id'] | NewKycCase['id']>;
  kycRef: FormControl<KycCaseFormRawValue['kycRef']>;
  status: FormControl<KycCaseFormRawValue['status']>;
  channel: FormControl<KycCaseFormRawValue['channel']>;
  startedAt: FormControl<KycCaseFormRawValue['startedAt']>;
  lastActivityAt: FormControl<KycCaseFormRawValue['lastActivityAt']>;
  lastUpdatedAt: FormControl<KycCaseFormRawValue['lastUpdatedAt']>;
  completedAt: FormControl<KycCaseFormRawValue['completedAt']>;
  expiresAt: FormControl<KycCaseFormRawValue['expiresAt']>;
  validationErrors: FormControl<KycCaseFormRawValue['validationErrors']>;
  internalNotes: FormControl<KycCaseFormRawValue['internalNotes']>;
  consent: FormControl<KycCaseFormRawValue['consent']>;
  submission: FormControl<KycCaseFormRawValue['submission']>;
  customer: FormControl<KycCaseFormRawValue['customer']>;
};

export type KycCaseFormGroup = FormGroup<KycCaseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KycCaseFormService {
  createKycCaseFormGroup(kycCase?: KycCaseFormGroupInput): KycCaseFormGroup {
    const kycCaseRawValue = this.convertKycCaseToKycCaseRawValue({
      ...this.getFormDefaults(),
      ...(kycCase ?? { id: null }),
    });
    return new FormGroup<KycCaseFormGroupContent>({
      id: new FormControl(
        { value: kycCaseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      kycRef: new FormControl(kycCaseRawValue.kycRef, {
        validators: [Validators.required, Validators.pattern(String.raw`^FGR[0-9]{8}-[0-9]{3}$`)],
      }),
      status: new FormControl(kycCaseRawValue.status, {
        validators: [Validators.required],
      }),
      channel: new FormControl(kycCaseRawValue.channel, {
        validators: [Validators.required],
      }),
      startedAt: new FormControl(kycCaseRawValue.startedAt, {
        validators: [Validators.required],
      }),
      lastActivityAt: new FormControl(kycCaseRawValue.lastActivityAt, {
        validators: [Validators.required],
      }),
      lastUpdatedAt: new FormControl(kycCaseRawValue.lastUpdatedAt),
      completedAt: new FormControl(kycCaseRawValue.completedAt),
      expiresAt: new FormControl(kycCaseRawValue.expiresAt),
      validationErrors: new FormControl(kycCaseRawValue.validationErrors),
      internalNotes: new FormControl(kycCaseRawValue.internalNotes),
      consent: new FormControl(kycCaseRawValue.consent),
      submission: new FormControl(kycCaseRawValue.submission),
      customer: new FormControl(kycCaseRawValue.customer),
    });
  }

  getKycCase(form: KycCaseFormGroup): IKycCase | NewKycCase {
    return this.convertKycCaseRawValueToKycCase(form.getRawValue() as KycCaseFormRawValue | NewKycCaseFormRawValue);
  }

  resetForm(form: KycCaseFormGroup, kycCase: KycCaseFormGroupInput): void {
    const kycCaseRawValue = this.convertKycCaseToKycCaseRawValue({ ...this.getFormDefaults(), ...kycCase });
    form.reset({
      ...kycCaseRawValue,
      id: { value: kycCaseRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): KycCaseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startedAt: currentTime,
      lastActivityAt: currentTime,
      lastUpdatedAt: currentTime,
      completedAt: currentTime,
      expiresAt: currentTime,
    };
  }

  private convertKycCaseRawValueToKycCase(rawKycCase: KycCaseFormRawValue | NewKycCaseFormRawValue): IKycCase | NewKycCase {
    return {
      ...rawKycCase,
      startedAt: dayjs(rawKycCase.startedAt, DATE_TIME_FORMAT),
      lastActivityAt: dayjs(rawKycCase.lastActivityAt, DATE_TIME_FORMAT),
      lastUpdatedAt: dayjs(rawKycCase.lastUpdatedAt, DATE_TIME_FORMAT),
      completedAt: dayjs(rawKycCase.completedAt, DATE_TIME_FORMAT),
      expiresAt: dayjs(rawKycCase.expiresAt, DATE_TIME_FORMAT),
    };
  }

  private convertKycCaseToKycCaseRawValue(
    kycCase: IKycCase | (Partial<NewKycCase> & KycCaseFormDefaults),
  ): KycCaseFormRawValue | PartialWithRequiredKeyOf<NewKycCaseFormRawValue> {
    return {
      ...kycCase,
      startedAt: kycCase.startedAt ? kycCase.startedAt.format(DATE_TIME_FORMAT) : undefined,
      lastActivityAt: kycCase.lastActivityAt ? kycCase.lastActivityAt.format(DATE_TIME_FORMAT) : undefined,
      lastUpdatedAt: kycCase.lastUpdatedAt ? kycCase.lastUpdatedAt.format(DATE_TIME_FORMAT) : undefined,
      completedAt: kycCase.completedAt ? kycCase.completedAt.format(DATE_TIME_FORMAT) : undefined,
      expiresAt: kycCase.expiresAt ? kycCase.expiresAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
