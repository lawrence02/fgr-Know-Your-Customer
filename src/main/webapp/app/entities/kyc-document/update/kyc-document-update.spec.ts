import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IKycCase } from 'app/entities/kyc-case/kyc-case.model';
import { KycCaseService } from 'app/entities/kyc-case/service/kyc-case.service';
import { IKycDocument } from '../kyc-document.model';
import { KycDocumentService } from '../service/kyc-document.service';

import { KycDocumentFormService } from './kyc-document-form.service';
import { KycDocumentUpdate } from './kyc-document-update';

describe('KycDocument Management Update Component', () => {
  let comp: KycDocumentUpdate;
  let fixture: ComponentFixture<KycDocumentUpdate>;
  let activatedRoute: ActivatedRoute;
  let kycDocumentFormService: KycDocumentFormService;
  let kycDocumentService: KycDocumentService;
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

    fixture = TestBed.createComponent(KycDocumentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kycDocumentFormService = TestBed.inject(KycDocumentFormService);
    kycDocumentService = TestBed.inject(KycDocumentService);
    kycCaseService = TestBed.inject(KycCaseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call KycCase query and add missing value', () => {
      const kycDocument: IKycDocument = { id: 13608 };
      const kycCase: IKycCase = { id: 1342 };
      kycDocument.kycCase = kycCase;

      const kycCaseCollection: IKycCase[] = [{ id: 1342 }];
      jest.spyOn(kycCaseService, 'query').mockReturnValue(of(new HttpResponse({ body: kycCaseCollection })));
      const additionalKycCases = [kycCase];
      const expectedCollection: IKycCase[] = [...additionalKycCases, ...kycCaseCollection];
      jest.spyOn(kycCaseService, 'addKycCaseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycDocument });
      comp.ngOnInit();

      expect(kycCaseService.query).toHaveBeenCalled();
      expect(kycCaseService.addKycCaseToCollectionIfMissing).toHaveBeenCalledWith(
        kycCaseCollection,
        ...additionalKycCases.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.kycCasesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const kycDocument: IKycDocument = { id: 13608 };
      const kycCase: IKycCase = { id: 1342 };
      kycDocument.kycCase = kycCase;

      activatedRoute.data = of({ kycDocument });
      comp.ngOnInit();

      expect(comp.kycCasesSharedCollection()).toContainEqual(kycCase);
      expect(comp.kycDocument).toEqual(kycDocument);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycDocument>>();
      const kycDocument = { id: 12821 };
      jest.spyOn(kycDocumentFormService, 'getKycDocument').mockReturnValue(kycDocument);
      jest.spyOn(kycDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycDocument }));
      saveSubject.complete();

      // THEN
      expect(kycDocumentFormService.getKycDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(kycDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(kycDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycDocument>>();
      const kycDocument = { id: 12821 };
      jest.spyOn(kycDocumentFormService, 'getKycDocument').mockReturnValue({ id: null });
      jest.spyOn(kycDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycDocument }));
      saveSubject.complete();

      // THEN
      expect(kycDocumentFormService.getKycDocument).toHaveBeenCalled();
      expect(kycDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycDocument>>();
      const kycDocument = { id: 12821 };
      jest.spyOn(kycDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kycDocumentService.update).toHaveBeenCalled();
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
