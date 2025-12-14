import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IKycNotification, NewKycNotification } from '../kyc-notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKycNotification for edit and NewKycNotificationFormGroupInput for create.
 */
type KycNotificationFormGroupInput = IKycNotification | PartialWithRequiredKeyOf<NewKycNotification>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IKycNotification | NewKycNotification> = Omit<T, 'sentAt' | 'deliveredAt'> & {
  sentAt?: string | null;
  deliveredAt?: string | null;
};

type KycNotificationFormRawValue = FormValueOf<IKycNotification>;

type NewKycNotificationFormRawValue = FormValueOf<NewKycNotification>;

type KycNotificationFormDefaults = Pick<NewKycNotification, 'id' | 'sentAt' | 'delivered' | 'deliveredAt'>;

type KycNotificationFormGroupContent = {
  id: FormControl<KycNotificationFormRawValue['id'] | NewKycNotification['id']>;
  notificationType: FormControl<KycNotificationFormRawValue['notificationType']>;
  message: FormControl<KycNotificationFormRawValue['message']>;
  sentAt: FormControl<KycNotificationFormRawValue['sentAt']>;
  delivered: FormControl<KycNotificationFormRawValue['delivered']>;
  deliveredAt: FormControl<KycNotificationFormRawValue['deliveredAt']>;
  errorMessage: FormControl<KycNotificationFormRawValue['errorMessage']>;
  kycCase: FormControl<KycNotificationFormRawValue['kycCase']>;
};

export type KycNotificationFormGroup = FormGroup<KycNotificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KycNotificationFormService {
  createKycNotificationFormGroup(kycNotification?: KycNotificationFormGroupInput): KycNotificationFormGroup {
    const kycNotificationRawValue = this.convertKycNotificationToKycNotificationRawValue({
      ...this.getFormDefaults(),
      ...(kycNotification ?? { id: null }),
    });
    return new FormGroup<KycNotificationFormGroupContent>({
      id: new FormControl(
        { value: kycNotificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      notificationType: new FormControl(kycNotificationRawValue.notificationType, {
        validators: [Validators.required],
      }),
      message: new FormControl(kycNotificationRawValue.message, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      sentAt: new FormControl(kycNotificationRawValue.sentAt, {
        validators: [Validators.required],
      }),
      delivered: new FormControl(kycNotificationRawValue.delivered),
      deliveredAt: new FormControl(kycNotificationRawValue.deliveredAt),
      errorMessage: new FormControl(kycNotificationRawValue.errorMessage, {
        validators: [Validators.maxLength(500)],
      }),
      kycCase: new FormControl(kycNotificationRawValue.kycCase),
    });
  }

  getKycNotification(form: KycNotificationFormGroup): IKycNotification | NewKycNotification {
    return this.convertKycNotificationRawValueToKycNotification(
      form.getRawValue() as KycNotificationFormRawValue | NewKycNotificationFormRawValue,
    );
  }

  resetForm(form: KycNotificationFormGroup, kycNotification: KycNotificationFormGroupInput): void {
    const kycNotificationRawValue = this.convertKycNotificationToKycNotificationRawValue({ ...this.getFormDefaults(), ...kycNotification });
    form.reset({
      ...kycNotificationRawValue,
      id: { value: kycNotificationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): KycNotificationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      sentAt: currentTime,
      delivered: false,
      deliveredAt: currentTime,
    };
  }

  private convertKycNotificationRawValueToKycNotification(
    rawKycNotification: KycNotificationFormRawValue | NewKycNotificationFormRawValue,
  ): IKycNotification | NewKycNotification {
    return {
      ...rawKycNotification,
      sentAt: dayjs(rawKycNotification.sentAt, DATE_TIME_FORMAT),
      deliveredAt: dayjs(rawKycNotification.deliveredAt, DATE_TIME_FORMAT),
    };
  }

  private convertKycNotificationToKycNotificationRawValue(
    kycNotification: IKycNotification | (Partial<NewKycNotification> & KycNotificationFormDefaults),
  ): KycNotificationFormRawValue | PartialWithRequiredKeyOf<NewKycNotificationFormRawValue> {
    return {
      ...kycNotification,
      sentAt: kycNotification.sentAt ? kycNotification.sentAt.format(DATE_TIME_FORMAT) : undefined,
      deliveredAt: kycNotification.deliveredAt ? kycNotification.deliveredAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
