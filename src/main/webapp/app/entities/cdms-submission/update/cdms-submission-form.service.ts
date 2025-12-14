import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICdmsSubmission, NewCdmsSubmission } from '../cdms-submission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICdmsSubmission for edit and NewCdmsSubmissionFormGroupInput for create.
 */
type CdmsSubmissionFormGroupInput = ICdmsSubmission | PartialWithRequiredKeyOf<NewCdmsSubmission>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICdmsSubmission | NewCdmsSubmission> = Omit<T, 'submittedAt' | 'lastAttemptAt' | 'nextRetryAt'> & {
  submittedAt?: string | null;
  lastAttemptAt?: string | null;
  nextRetryAt?: string | null;
};

type CdmsSubmissionFormRawValue = FormValueOf<ICdmsSubmission>;

type NewCdmsSubmissionFormRawValue = FormValueOf<NewCdmsSubmission>;

type CdmsSubmissionFormDefaults = Pick<NewCdmsSubmission, 'id' | 'submittedAt' | 'lastAttemptAt' | 'nextRetryAt'>;

type CdmsSubmissionFormGroupContent = {
  id: FormControl<CdmsSubmissionFormRawValue['id'] | NewCdmsSubmission['id']>;
  submissionRef: FormControl<CdmsSubmissionFormRawValue['submissionRef']>;
  status: FormControl<CdmsSubmissionFormRawValue['status']>;
  responseCode: FormControl<CdmsSubmissionFormRawValue['responseCode']>;
  responseMessage: FormControl<CdmsSubmissionFormRawValue['responseMessage']>;
  attempts: FormControl<CdmsSubmissionFormRawValue['attempts']>;
  submittedAt: FormControl<CdmsSubmissionFormRawValue['submittedAt']>;
  lastAttemptAt: FormControl<CdmsSubmissionFormRawValue['lastAttemptAt']>;
  nextRetryAt: FormControl<CdmsSubmissionFormRawValue['nextRetryAt']>;
  cdmsCustomerId: FormControl<CdmsSubmissionFormRawValue['cdmsCustomerId']>;
};

export type CdmsSubmissionFormGroup = FormGroup<CdmsSubmissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CdmsSubmissionFormService {
  createCdmsSubmissionFormGroup(cdmsSubmission?: CdmsSubmissionFormGroupInput): CdmsSubmissionFormGroup {
    const cdmsSubmissionRawValue = this.convertCdmsSubmissionToCdmsSubmissionRawValue({
      ...this.getFormDefaults(),
      ...(cdmsSubmission ?? { id: null }),
    });
    return new FormGroup<CdmsSubmissionFormGroupContent>({
      id: new FormControl(
        { value: cdmsSubmissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      submissionRef: new FormControl(cdmsSubmissionRawValue.submissionRef, {
        validators: [Validators.required],
      }),
      status: new FormControl(cdmsSubmissionRawValue.status, {
        validators: [Validators.required],
      }),
      responseCode: new FormControl(cdmsSubmissionRawValue.responseCode, {
        validators: [Validators.maxLength(50)],
      }),
      responseMessage: new FormControl(cdmsSubmissionRawValue.responseMessage, {
        validators: [Validators.maxLength(1000)],
      }),
      attempts: new FormControl(cdmsSubmissionRawValue.attempts, {
        validators: [Validators.required, Validators.min(0), Validators.max(3)],
      }),
      submittedAt: new FormControl(cdmsSubmissionRawValue.submittedAt),
      lastAttemptAt: new FormControl(cdmsSubmissionRawValue.lastAttemptAt),
      nextRetryAt: new FormControl(cdmsSubmissionRawValue.nextRetryAt),
      cdmsCustomerId: new FormControl(cdmsSubmissionRawValue.cdmsCustomerId, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getCdmsSubmission(form: CdmsSubmissionFormGroup): ICdmsSubmission | NewCdmsSubmission {
    return this.convertCdmsSubmissionRawValueToCdmsSubmission(
      form.getRawValue() as CdmsSubmissionFormRawValue | NewCdmsSubmissionFormRawValue,
    );
  }

  resetForm(form: CdmsSubmissionFormGroup, cdmsSubmission: CdmsSubmissionFormGroupInput): void {
    const cdmsSubmissionRawValue = this.convertCdmsSubmissionToCdmsSubmissionRawValue({ ...this.getFormDefaults(), ...cdmsSubmission });
    form.reset({
      ...cdmsSubmissionRawValue,
      id: { value: cdmsSubmissionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CdmsSubmissionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submittedAt: currentTime,
      lastAttemptAt: currentTime,
      nextRetryAt: currentTime,
    };
  }

  private convertCdmsSubmissionRawValueToCdmsSubmission(
    rawCdmsSubmission: CdmsSubmissionFormRawValue | NewCdmsSubmissionFormRawValue,
  ): ICdmsSubmission | NewCdmsSubmission {
    return {
      ...rawCdmsSubmission,
      submittedAt: dayjs(rawCdmsSubmission.submittedAt, DATE_TIME_FORMAT),
      lastAttemptAt: dayjs(rawCdmsSubmission.lastAttemptAt, DATE_TIME_FORMAT),
      nextRetryAt: dayjs(rawCdmsSubmission.nextRetryAt, DATE_TIME_FORMAT),
    };
  }

  private convertCdmsSubmissionToCdmsSubmissionRawValue(
    cdmsSubmission: ICdmsSubmission | (Partial<NewCdmsSubmission> & CdmsSubmissionFormDefaults),
  ): CdmsSubmissionFormRawValue | PartialWithRequiredKeyOf<NewCdmsSubmissionFormRawValue> {
    return {
      ...cdmsSubmission,
      submittedAt: cdmsSubmission.submittedAt ? cdmsSubmission.submittedAt.format(DATE_TIME_FORMAT) : undefined,
      lastAttemptAt: cdmsSubmission.lastAttemptAt ? cdmsSubmission.lastAttemptAt.format(DATE_TIME_FORMAT) : undefined,
      nextRetryAt: cdmsSubmission.nextRetryAt ? cdmsSubmission.nextRetryAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
