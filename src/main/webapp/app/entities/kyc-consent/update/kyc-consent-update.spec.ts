import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IKycConsent } from '../kyc-consent.model';
import { KycConsentService } from '../service/kyc-consent.service';

import { KycConsentFormService } from './kyc-consent-form.service';
import { KycConsentUpdate } from './kyc-consent-update';

describe('KycConsent Management Update Component', () => {
  let comp: KycConsentUpdate;
  let fixture: ComponentFixture<KycConsentUpdate>;
  let activatedRoute: ActivatedRoute;
  let kycConsentFormService: KycConsentFormService;
  let kycConsentService: KycConsentService;

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

    fixture = TestBed.createComponent(KycConsentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kycConsentFormService = TestBed.inject(KycConsentFormService);
    kycConsentService = TestBed.inject(KycConsentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const kycConsent: IKycConsent = { id: 2003 };

      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      expect(comp.kycConsent).toEqual(kycConsent);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycConsent>>();
      const kycConsent = { id: 12952 };
      jest.spyOn(kycConsentFormService, 'getKycConsent').mockReturnValue(kycConsent);
      jest.spyOn(kycConsentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycConsent }));
      saveSubject.complete();

      // THEN
      expect(kycConsentFormService.getKycConsent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(kycConsentService.update).toHaveBeenCalledWith(expect.objectContaining(kycConsent));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycConsent>>();
      const kycConsent = { id: 12952 };
      jest.spyOn(kycConsentFormService, 'getKycConsent').mockReturnValue({ id: null });
      jest.spyOn(kycConsentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycConsent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycConsent }));
      saveSubject.complete();

      // THEN
      expect(kycConsentFormService.getKycConsent).toHaveBeenCalled();
      expect(kycConsentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycConsent>>();
      const kycConsent = { id: 12952 };
      jest.spyOn(kycConsentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kycConsentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
