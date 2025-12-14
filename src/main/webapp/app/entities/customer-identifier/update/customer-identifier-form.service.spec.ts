import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../customer-identifier.test-samples';

import { CustomerIdentifierFormService } from './customer-identifier-form.service';

describe('CustomerIdentifier Form Service', () => {
  let service: CustomerIdentifierFormService;

  beforeEach(() => {
    service = TestBed.inject(CustomerIdentifierFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerIdentifierFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerIdentifierFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifierType: expect.any(Object),
            identifierValue: expect.any(Object),
            channel: expect.any(Object),
            verified: expect.any(Object),
            isPrimary: expect.any(Object),
            createdAt: expect.any(Object),
            verifiedAt: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing ICustomerIdentifier should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerIdentifierFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifierType: expect.any(Object),
            identifierValue: expect.any(Object),
            channel: expect.any(Object),
            verified: expect.any(Object),
            isPrimary: expect.any(Object),
            createdAt: expect.any(Object),
            verifiedAt: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomerIdentifier', () => {
      it('should return NewCustomerIdentifier for default CustomerIdentifier initial value', () => {
        const formGroup = service.createCustomerIdentifierFormGroup(sampleWithNewData);

        const customerIdentifier = service.getCustomerIdentifier(formGroup);

        expect(customerIdentifier).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerIdentifier for empty CustomerIdentifier initial value', () => {
        const formGroup = service.createCustomerIdentifierFormGroup();

        const customerIdentifier = service.getCustomerIdentifier(formGroup);

        expect(customerIdentifier).toMatchObject({});
      });

      it('should return ICustomerIdentifier', () => {
        const formGroup = service.createCustomerIdentifierFormGroup(sampleWithRequiredData);

        const customerIdentifier = service.getCustomerIdentifier(formGroup);

        expect(customerIdentifier).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerIdentifier should not enable id FormControl', () => {
        const formGroup = service.createCustomerIdentifierFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerIdentifier should disable id FormControl', () => {
        const formGroup = service.createCustomerIdentifierFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
