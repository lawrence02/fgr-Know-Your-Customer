import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cdms-submission.test-samples';

import { CdmsSubmissionFormService } from './cdms-submission-form.service';

describe('CdmsSubmission Form Service', () => {
  let service: CdmsSubmissionFormService;

  beforeEach(() => {
    service = TestBed.inject(CdmsSubmissionFormService);
  });

  describe('Service methods', () => {
    describe('createCdmsSubmissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCdmsSubmissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            submissionRef: expect.any(Object),
            status: expect.any(Object),
            responseCode: expect.any(Object),
            responseMessage: expect.any(Object),
            attempts: expect.any(Object),
            submittedAt: expect.any(Object),
            lastAttemptAt: expect.any(Object),
            nextRetryAt: expect.any(Object),
            cdmsCustomerId: expect.any(Object),
          }),
        );
      });

      it('passing ICdmsSubmission should create a new form with FormGroup', () => {
        const formGroup = service.createCdmsSubmissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            submissionRef: expect.any(Object),
            status: expect.any(Object),
            responseCode: expect.any(Object),
            responseMessage: expect.any(Object),
            attempts: expect.any(Object),
            submittedAt: expect.any(Object),
            lastAttemptAt: expect.any(Object),
            nextRetryAt: expect.any(Object),
            cdmsCustomerId: expect.any(Object),
          }),
        );
      });
    });

    describe('getCdmsSubmission', () => {
      it('should return NewCdmsSubmission for default CdmsSubmission initial value', () => {
        const formGroup = service.createCdmsSubmissionFormGroup(sampleWithNewData);

        const cdmsSubmission = service.getCdmsSubmission(formGroup);

        expect(cdmsSubmission).toMatchObject(sampleWithNewData);
      });

      it('should return NewCdmsSubmission for empty CdmsSubmission initial value', () => {
        const formGroup = service.createCdmsSubmissionFormGroup();

        const cdmsSubmission = service.getCdmsSubmission(formGroup);

        expect(cdmsSubmission).toMatchObject({});
      });

      it('should return ICdmsSubmission', () => {
        const formGroup = service.createCdmsSubmissionFormGroup(sampleWithRequiredData);

        const cdmsSubmission = service.getCdmsSubmission(formGroup);

        expect(cdmsSubmission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICdmsSubmission should not enable id FormControl', () => {
        const formGroup = service.createCdmsSubmissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCdmsSubmission should disable id FormControl', () => {
        const formGroup = service.createCdmsSubmissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
