import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IKycDocument, NewKycDocument } from '../kyc-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKycDocument for edit and NewKycDocumentFormGroupInput for create.
 */
type KycDocumentFormGroupInput = IKycDocument | PartialWithRequiredKeyOf<NewKycDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IKycDocument | NewKycDocument> = Omit<T, 'uploadedAt' | 'expiresAt' | 'deletedAt'> & {
  uploadedAt?: string | null;
  expiresAt?: string | null;
  deletedAt?: string | null;
};

type KycDocumentFormRawValue = FormValueOf<IKycDocument>;

type NewKycDocumentFormRawValue = FormValueOf<NewKycDocument>;

type KycDocumentFormDefaults = Pick<NewKycDocument, 'id' | 'uploadedAt' | 'expiresAt' | 'deleted' | 'deletedAt'>;

type KycDocumentFormGroupContent = {
  id: FormControl<KycDocumentFormRawValue['id'] | NewKycDocument['id']>;
  documentType: FormControl<KycDocumentFormRawValue['documentType']>;
  fileName: FormControl<KycDocumentFormRawValue['fileName']>;
  mimeType: FormControl<KycDocumentFormRawValue['mimeType']>;
  storagePath: FormControl<KycDocumentFormRawValue['storagePath']>;
  fileSize: FormControl<KycDocumentFormRawValue['fileSize']>;
  uploadedAt: FormControl<KycDocumentFormRawValue['uploadedAt']>;
  expiresAt: FormControl<KycDocumentFormRawValue['expiresAt']>;
  deleted: FormControl<KycDocumentFormRawValue['deleted']>;
  deletedAt: FormControl<KycDocumentFormRawValue['deletedAt']>;
  metadata: FormControl<KycDocumentFormRawValue['metadata']>;
  checksum: FormControl<KycDocumentFormRawValue['checksum']>;
  kycCase: FormControl<KycDocumentFormRawValue['kycCase']>;
};

export type KycDocumentFormGroup = FormGroup<KycDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KycDocumentFormService {
  createKycDocumentFormGroup(kycDocument?: KycDocumentFormGroupInput): KycDocumentFormGroup {
    const kycDocumentRawValue = this.convertKycDocumentToKycDocumentRawValue({
      ...this.getFormDefaults(),
      ...(kycDocument ?? { id: null }),
    });
    return new FormGroup<KycDocumentFormGroupContent>({
      id: new FormControl(
        { value: kycDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentType: new FormControl(kycDocumentRawValue.documentType, {
        validators: [Validators.required],
      }),
      fileName: new FormControl(kycDocumentRawValue.fileName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      mimeType: new FormControl(kycDocumentRawValue.mimeType, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      storagePath: new FormControl(kycDocumentRawValue.storagePath, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      fileSize: new FormControl(kycDocumentRawValue.fileSize),
      uploadedAt: new FormControl(kycDocumentRawValue.uploadedAt, {
        validators: [Validators.required],
      }),
      expiresAt: new FormControl(kycDocumentRawValue.expiresAt, {
        validators: [Validators.required],
      }),
      deleted: new FormControl(kycDocumentRawValue.deleted),
      deletedAt: new FormControl(kycDocumentRawValue.deletedAt),
      metadata: new FormControl(kycDocumentRawValue.metadata),
      checksum: new FormControl(kycDocumentRawValue.checksum, {
        validators: [Validators.maxLength(64)],
      }),
      kycCase: new FormControl(kycDocumentRawValue.kycCase),
    });
  }

  getKycDocument(form: KycDocumentFormGroup): IKycDocument | NewKycDocument {
    return this.convertKycDocumentRawValueToKycDocument(form.getRawValue() as KycDocumentFormRawValue | NewKycDocumentFormRawValue);
  }

  resetForm(form: KycDocumentFormGroup, kycDocument: KycDocumentFormGroupInput): void {
    const kycDocumentRawValue = this.convertKycDocumentToKycDocumentRawValue({ ...this.getFormDefaults(), ...kycDocument });
    form.reset({
      ...kycDocumentRawValue,
      id: { value: kycDocumentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): KycDocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadedAt: currentTime,
      expiresAt: currentTime,
      deleted: false,
      deletedAt: currentTime,
    };
  }

  private convertKycDocumentRawValueToKycDocument(
    rawKycDocument: KycDocumentFormRawValue | NewKycDocumentFormRawValue,
  ): IKycDocument | NewKycDocument {
    return {
      ...rawKycDocument,
      uploadedAt: dayjs(rawKycDocument.uploadedAt, DATE_TIME_FORMAT),
      expiresAt: dayjs(rawKycDocument.expiresAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawKycDocument.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertKycDocumentToKycDocumentRawValue(
    kycDocument: IKycDocument | (Partial<NewKycDocument> & KycDocumentFormDefaults),
  ): KycDocumentFormRawValue | PartialWithRequiredKeyOf<NewKycDocumentFormRawValue> {
    return {
      ...kycDocument,
      uploadedAt: kycDocument.uploadedAt ? kycDocument.uploadedAt.format(DATE_TIME_FORMAT) : undefined,
      expiresAt: kycDocument.expiresAt ? kycDocument.expiresAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: kycDocument.deletedAt ? kycDocument.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
