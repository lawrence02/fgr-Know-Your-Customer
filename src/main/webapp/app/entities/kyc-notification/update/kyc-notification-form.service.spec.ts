import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../kyc-notification.test-samples';

import { KycNotificationFormService } from './kyc-notification-form.service';

describe('KycNotification Form Service', () => {
  let service: KycNotificationFormService;

  beforeEach(() => {
    service = TestBed.inject(KycNotificationFormService);
  });

  describe('Service methods', () => {
    describe('createKycNotificationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKycNotificationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            notificationType: expect.any(Object),
            message: expect.any(Object),
            sentAt: expect.any(Object),
            delivered: expect.any(Object),
            deliveredAt: expect.any(Object),
            errorMessage: expect.any(Object),
            kycCase: expect.any(Object),
          }),
        );
      });

      it('passing IKycNotification should create a new form with FormGroup', () => {
        const formGroup = service.createKycNotificationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            notificationType: expect.any(Object),
            message: expect.any(Object),
            sentAt: expect.any(Object),
            delivered: expect.any(Object),
            deliveredAt: expect.any(Object),
            errorMessage: expect.any(Object),
            kycCase: expect.any(Object),
          }),
        );
      });
    });

    describe('getKycNotification', () => {
      it('should return NewKycNotification for default KycNotification initial value', () => {
        const formGroup = service.createKycNotificationFormGroup(sampleWithNewData);

        const kycNotification = service.getKycNotification(formGroup);

        expect(kycNotification).toMatchObject(sampleWithNewData);
      });

      it('should return NewKycNotification for empty KycNotification initial value', () => {
        const formGroup = service.createKycNotificationFormGroup();

        const kycNotification = service.getKycNotification(formGroup);

        expect(kycNotification).toMatchObject({});
      });

      it('should return IKycNotification', () => {
        const formGroup = service.createKycNotificationFormGroup(sampleWithRequiredData);

        const kycNotification = service.getKycNotification(formGroup);

        expect(kycNotification).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKycNotification should not enable id FormControl', () => {
        const formGroup = service.createKycNotificationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKycNotification should disable id FormControl', () => {
        const formGroup = service.createKycNotificationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
