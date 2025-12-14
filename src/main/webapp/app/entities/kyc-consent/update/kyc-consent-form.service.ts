import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IKycConsent, NewKycConsent } from '../kyc-consent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKycConsent for edit and NewKycConsentFormGroupInput for create.
 */
type KycConsentFormGroupInput = IKycConsent | PartialWithRequiredKeyOf<NewKycConsent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IKycConsent | NewKycConsent> = Omit<T, 'consentedAt'> & {
  consentedAt?: string | null;
};

type KycConsentFormRawValue = FormValueOf<IKycConsent>;

type NewKycConsentFormRawValue = FormValueOf<NewKycConsent>;

type KycConsentFormDefaults = Pick<NewKycConsent, 'id' | 'consented' | 'consentedAt'>;

type KycConsentFormGroupContent = {
  id: FormControl<KycConsentFormRawValue['id'] | NewKycConsent['id']>;
  consentText: FormControl<KycConsentFormRawValue['consentText']>;
  consented: FormControl<KycConsentFormRawValue['consented']>;
  consentedAt: FormControl<KycConsentFormRawValue['consentedAt']>;
  channel: FormControl<KycConsentFormRawValue['channel']>;
  ipAddress: FormControl<KycConsentFormRawValue['ipAddress']>;
  userAgent: FormControl<KycConsentFormRawValue['userAgent']>;
  consentVersion: FormControl<KycConsentFormRawValue['consentVersion']>;
};

export type KycConsentFormGroup = FormGroup<KycConsentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KycConsentFormService {
  createKycConsentFormGroup(kycConsent?: KycConsentFormGroupInput): KycConsentFormGroup {
    const kycConsentRawValue = this.convertKycConsentToKycConsentRawValue({
      ...this.getFormDefaults(),
      ...(kycConsent ?? { id: null }),
    });
    return new FormGroup<KycConsentFormGroupContent>({
      id: new FormControl(
        { value: kycConsentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      consentText: new FormControl(kycConsentRawValue.consentText, {
        validators: [Validators.required, Validators.maxLength(2000)],
      }),
      consented: new FormControl(kycConsentRawValue.consented, {
        validators: [Validators.required],
      }),
      consentedAt: new FormControl(kycConsentRawValue.consentedAt, {
        validators: [Validators.required],
      }),
      channel: new FormControl(kycConsentRawValue.channel, {
        validators: [Validators.required],
      }),
      ipAddress: new FormControl(kycConsentRawValue.ipAddress, {
        validators: [Validators.maxLength(45)],
      }),
      userAgent: new FormControl(kycConsentRawValue.userAgent, {
        validators: [Validators.maxLength(500)],
      }),
      consentVersion: new FormControl(kycConsentRawValue.consentVersion, {
        validators: [Validators.maxLength(10)],
      }),
    });
  }

  getKycConsent(form: KycConsentFormGroup): IKycConsent | NewKycConsent {
    return this.convertKycConsentRawValueToKycConsent(form.getRawValue() as KycConsentFormRawValue | NewKycConsentFormRawValue);
  }

  resetForm(form: KycConsentFormGroup, kycConsent: KycConsentFormGroupInput): void {
    const kycConsentRawValue = this.convertKycConsentToKycConsentRawValue({ ...this.getFormDefaults(), ...kycConsent });
    form.reset({
      ...kycConsentRawValue,
      id: { value: kycConsentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): KycConsentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      consented: false,
      consentedAt: currentTime,
    };
  }

  private convertKycConsentRawValueToKycConsent(
    rawKycConsent: KycConsentFormRawValue | NewKycConsentFormRawValue,
  ): IKycConsent | NewKycConsent {
    return {
      ...rawKycConsent,
      consentedAt: dayjs(rawKycConsent.consentedAt, DATE_TIME_FORMAT),
    };
  }

  private convertKycConsentToKycConsentRawValue(
    kycConsent: IKycConsent | (Partial<NewKycConsent> & KycConsentFormDefaults),
  ): KycConsentFormRawValue | PartialWithRequiredKeyOf<NewKycConsentFormRawValue> {
    return {
      ...kycConsent,
      consentedAt: kycConsent.consentedAt ? kycConsent.consentedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
