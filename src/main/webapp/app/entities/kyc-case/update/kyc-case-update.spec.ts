import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICdmsSubmission } from 'app/entities/cdms-submission/cdms-submission.model';
import { CdmsSubmissionService } from 'app/entities/cdms-submission/service/cdms-submission.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IKycConsent } from 'app/entities/kyc-consent/kyc-consent.model';
import { KycConsentService } from 'app/entities/kyc-consent/service/kyc-consent.service';
import { IKycCase } from '../kyc-case.model';
import { KycCaseService } from '../service/kyc-case.service';

import { KycCaseFormService } from './kyc-case-form.service';
import { KycCaseUpdate } from './kyc-case-update';

describe('KycCase Management Update Component', () => {
  let comp: KycCaseUpdate;
  let fixture: ComponentFixture<KycCaseUpdate>;
  let activatedRoute: ActivatedRoute;
  let kycCaseFormService: KycCaseFormService;
  let kycCaseService: KycCaseService;
  let kycConsentService: KycConsentService;
  let cdmsSubmissionService: CdmsSubmissionService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(KycCaseUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kycCaseFormService = TestBed.inject(KycCaseFormService);
    kycCaseService = TestBed.inject(KycCaseService);
    kycConsentService = TestBed.inject(KycConsentService);
    cdmsSubmissionService = TestBed.inject(CdmsSubmissionService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call consent query and add missing value', () => {
      const kycCase: IKycCase = { id: 2504 };
      const consent: IKycConsent = { id: 12952 };
      kycCase.consent = consent;

      const consentCollection: IKycConsent[] = [{ id: 12952 }];
      jest.spyOn(kycConsentService, 'query').mockReturnValue(of(new HttpResponse({ body: consentCollection })));
      const expectedCollection: IKycConsent[] = [consent, ...consentCollection];
      jest.spyOn(kycConsentService, 'addKycConsentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycCase });
      comp.ngOnInit();

      expect(kycConsentService.query).toHaveBeenCalled();
      expect(kycConsentService.addKycConsentToCollectionIfMissing).toHaveBeenCalledWith(consentCollection, consent);
      expect(comp.consentsCollection()).toEqual(expectedCollection);
    });

    it('should call submission query and add missing value', () => {
      const kycCase: IKycCase = { id: 2504 };
      const submission: ICdmsSubmission = { id: 7713 };
      kycCase.submission = submission;

      const submissionCollection: ICdmsSubmission[] = [{ id: 7713 }];
      jest.spyOn(cdmsSubmissionService, 'query').mockReturnValue(of(new HttpResponse({ body: submissionCollection })));
      const expectedCollection: ICdmsSubmission[] = [submission, ...submissionCollection];
      jest.spyOn(cdmsSubmissionService, 'addCdmsSubmissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycCase });
      comp.ngOnInit();

      expect(cdmsSubmissionService.query).toHaveBeenCalled();
      expect(cdmsSubmissionService.addCdmsSubmissionToCollectionIfMissing).toHaveBeenCalledWith(submissionCollection, submission);
      expect(comp.submissionsCollection()).toEqual(expectedCollection);
    });

    it('should call Customer query and add missing value', () => {
      const kycCase: IKycCase = { id: 2504 };
      const customer: ICustomer = { id: 26915 };
      kycCase.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycCase });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.customersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const kycCase: IKycCase = { id: 2504 };
      const consent: IKycConsent = { id: 12952 };
      kycCase.consent = consent;
      const submission: ICdmsSubmission = { id: 7713 };
      kycCase.submission = submission;
      const customer: ICustomer = { id: 26915 };
      kycCase.customer = customer;

      activatedRoute.data = of({ kycCase });
      comp.ngOnInit();

      expect(comp.consentsCollection()).toContainEqual(consent);
      expect(comp.submissionsCollection()).toContainEqual(submission);
      expect(comp.customersSharedCollection()).toContainEqual(customer);
      expect(comp.kycCase).toEqual(kycCase);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycCase>>();
      const kycCase = { id: 1342 };
      jest.spyOn(kycCaseFormService, 'getKycCase').mockReturnValue(kycCase);
      jest.spyOn(kycCaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycCase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycCase }));
      saveSubject.complete();

      // THEN
      expect(kycCaseFormService.getKycCase).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(kycCaseService.update).toHaveBeenCalledWith(expect.objectContaining(kycCase));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycCase>>();
      const kycCase = { id: 1342 };
      jest.spyOn(kycCaseFormService, 'getKycCase').mockReturnValue({ id: null });
      jest.spyOn(kycCaseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycCase: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycCase }));
      saveSubject.complete();

      // THEN
      expect(kycCaseFormService.getKycCase).toHaveBeenCalled();
      expect(kycCaseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycCase>>();
      const kycCase = { id: 1342 };
      jest.spyOn(kycCaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycCase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kycCaseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareKycConsent', () => {
      it('should forward to kycConsentService', () => {
        const entity = { id: 12952 };
        const entity2 = { id: 2003 };
        jest.spyOn(kycConsentService, 'compareKycConsent');
        comp.compareKycConsent(entity, entity2);
        expect(kycConsentService.compareKycConsent).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCdmsSubmission', () => {
      it('should forward to cdmsSubmissionService', () => {
        const entity = { id: 7713 };
        const entity2 = { id: 21337 };
        jest.spyOn(cdmsSubmissionService, 'compareCdmsSubmission');
        comp.compareCdmsSubmission(entity, entity2);
        expect(cdmsSubmissionService.compareCdmsSubmission).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('should forward to customerService', () => {
        const entity = { id: 26915 };
        const entity2 = { id: 21032 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
