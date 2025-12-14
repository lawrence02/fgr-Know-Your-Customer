import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IKycCase } from 'app/entities/kyc-case/kyc-case.model';
import { KycCaseService } from 'app/entities/kyc-case/service/kyc-case.service';
import { IKycNotification } from '../kyc-notification.model';
import { KycNotificationService } from '../service/kyc-notification.service';

import { KycNotificationFormService } from './kyc-notification-form.service';
import { KycNotificationUpdate } from './kyc-notification-update';

describe('KycNotification Management Update Component', () => {
  let comp: KycNotificationUpdate;
  let fixture: ComponentFixture<KycNotificationUpdate>;
  let activatedRoute: ActivatedRoute;
  let kycNotificationFormService: KycNotificationFormService;
  let kycNotificationService: KycNotificationService;
  let kycCaseService: KycCaseService;

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

    fixture = TestBed.createComponent(KycNotificationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kycNotificationFormService = TestBed.inject(KycNotificationFormService);
    kycNotificationService = TestBed.inject(KycNotificationService);
    kycCaseService = TestBed.inject(KycCaseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call KycCase query and add missing value', () => {
      const kycNotification: IKycNotification = { id: 553 };
      const kycCase: IKycCase = { id: 1342 };
      kycNotification.kycCase = kycCase;

      const kycCaseCollection: IKycCase[] = [{ id: 1342 }];
      jest.spyOn(kycCaseService, 'query').mockReturnValue(of(new HttpResponse({ body: kycCaseCollection })));
      const additionalKycCases = [kycCase];
      const expectedCollection: IKycCase[] = [...additionalKycCases, ...kycCaseCollection];
      jest.spyOn(kycCaseService, 'addKycCaseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycNotification });
      comp.ngOnInit();

      expect(kycCaseService.query).toHaveBeenCalled();
      expect(kycCaseService.addKycCaseToCollectionIfMissing).toHaveBeenCalledWith(
        kycCaseCollection,
        ...additionalKycCases.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.kycCasesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const kycNotification: IKycNotification = { id: 553 };
      const kycCase: IKycCase = { id: 1342 };
      kycNotification.kycCase = kycCase;

      activatedRoute.data = of({ kycNotification });
      comp.ngOnInit();

      expect(comp.kycCasesSharedCollection()).toContainEqual(kycCase);
      expect(comp.kycNotification).toEqual(kycNotification);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycNotification>>();
      const kycNotification = { id: 30507 };
      jest.spyOn(kycNotificationFormService, 'getKycNotification').mockReturnValue(kycNotification);
      jest.spyOn(kycNotificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycNotification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycNotification }));
      saveSubject.complete();

      // THEN
      expect(kycNotificationFormService.getKycNotification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(kycNotificationService.update).toHaveBeenCalledWith(expect.objectContaining(kycNotification));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycNotification>>();
      const kycNotification = { id: 30507 };
      jest.spyOn(kycNotificationFormService, 'getKycNotification').mockReturnValue({ id: null });
      jest.spyOn(kycNotificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycNotification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycNotification }));
      saveSubject.complete();

      // THEN
      expect(kycNotificationFormService.getKycNotification).toHaveBeenCalled();
      expect(kycNotificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycNotification>>();
      const kycNotification = { id: 30507 };
      jest.spyOn(kycNotificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycNotification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kycNotificationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareKycCase', () => {
      it('should forward to kycCaseService', () => {
        const entity = { id: 1342 };
        const entity2 = { id: 2504 };
        jest.spyOn(kycCaseService, 'compareKycCase');
        comp.compareKycCase(entity, entity2);
        expect(kycCaseService.compareKycCase).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
