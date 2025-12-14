import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../kyc-case.test-samples';

import { KycCaseFormService } from './kyc-case-form.service';

describe('KycCase Form Service', () => {
  let service: KycCaseFormService;

  beforeEach(() => {
    service = TestBed.inject(KycCaseFormService);
  });

  describe('Service methods', () => {
    describe('createKycCaseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKycCaseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kycRef: expect.any(Object),
            status: expect.any(Object),
            channel: expect.any(Object),
            startedAt: expect.any(Object),
            lastActivityAt: expect.any(Object),
            lastUpdatedAt: expect.any(Object),
            completedAt: expect.any(Object),
            expiresAt: expect.any(Object),
            validationErrors: expect.any(Object),
            internalNotes: expect.any(Object),
            consent: expect.any(Object),
            submission: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing IKycCase should create a new form with FormGroup', () => {
        const formGroup = service.createKycCaseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kycRef: expect.any(Object),
            status: expect.any(Object),
            channel: expect.any(Object),
            startedAt: expect.any(Object),
            lastActivityAt: expect.any(Object),
            lastUpdatedAt: expect.any(Object),
            completedAt: expect.any(Object),
            expiresAt: expect.any(Object),
            validationErrors: expect.any(Object),
            internalNotes: expect.any(Object),
            consent: expect.any(Object),
            submission: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getKycCase', () => {
      it('should return NewKycCase for default KycCase initial value', () => {
        const formGroup = service.createKycCaseFormGroup(sampleWithNewData);

        const kycCase = service.getKycCase(formGroup);

        expect(kycCase).toMatchObject(sampleWithNewData);
      });

      it('should return NewKycCase for empty KycCase initial value', () => {
        const formGroup = service.createKycCaseFormGroup();

        const kycCase = service.getKycCase(formGroup);

        expect(kycCase).toMatchObject({});
      });

      it('should return IKycCase', () => {
        const formGroup = service.createKycCaseFormGroup(sampleWithRequiredData);

        const kycCase = service.getKycCase(formGroup);

        expect(kycCase).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKycCase should not enable id FormControl', () => {
        const formGroup = service.createKycCaseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKycCase should disable id FormControl', () => {
        const formGroup = service.createKycCaseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
