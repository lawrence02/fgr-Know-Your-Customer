import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../kyc-document.test-samples';

import { KycDocumentFormService } from './kyc-document-form.service';

describe('KycDocument Form Service', () => {
  let service: KycDocumentFormService;

  beforeEach(() => {
    service = TestBed.inject(KycDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createKycDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKycDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentType: expect.any(Object),
            fileName: expect.any(Object),
            mimeType: expect.any(Object),
            storagePath: expect.any(Object),
            fileSize: expect.any(Object),
            uploadedAt: expect.any(Object),
            expiresAt: expect.any(Object),
            deleted: expect.any(Object),
            deletedAt: expect.any(Object),
            metadata: expect.any(Object),
            checksum: expect.any(Object),
            kycCase: expect.any(Object),
          }),
        );
      });

      it('passing IKycDocument should create a new form with FormGroup', () => {
        const formGroup = service.createKycDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentType: expect.any(Object),
            fileName: expect.any(Object),
            mimeType: expect.any(Object),
            storagePath: expect.any(Object),
            fileSize: expect.any(Object),
            uploadedAt: expect.any(Object),
            expiresAt: expect.any(Object),
            deleted: expect.any(Object),
            deletedAt: expect.any(Object),
            metadata: expect.any(Object),
            checksum: expect.any(Object),
            kycCase: expect.any(Object),
          }),
        );
      });
    });

    describe('getKycDocument', () => {
      it('should return NewKycDocument for default KycDocument initial value', () => {
        const formGroup = service.createKycDocumentFormGroup(sampleWithNewData);

        const kycDocument = service.getKycDocument(formGroup);

        expect(kycDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewKycDocument for empty KycDocument initial value', () => {
        const formGroup = service.createKycDocumentFormGroup();

        const kycDocument = service.getKycDocument(formGroup);

        expect(kycDocument).toMatchObject({});
      });

      it('should return IKycDocument', () => {
        const formGroup = service.createKycDocumentFormGroup(sampleWithRequiredData);

        const kycDocument = service.getKycDocument(formGroup);

        expect(kycDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKycDocument should not enable id FormControl', () => {
        const formGroup = service.createKycDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKycDocument should disable id FormControl', () => {
        const formGroup = service.createKycDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
