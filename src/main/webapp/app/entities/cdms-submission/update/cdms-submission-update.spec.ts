import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICdmsSubmission } from '../cdms-submission.model';
import { CdmsSubmissionService } from '../service/cdms-submission.service';

import { CdmsSubmissionFormService } from './cdms-submission-form.service';
import { CdmsSubmissionUpdate } from './cdms-submission-update';

describe('CdmsSubmission Management Update Component', () => {
  let comp: CdmsSubmissionUpdate;
  let fixture: ComponentFixture<CdmsSubmissionUpdate>;
  let activatedRoute: ActivatedRoute;
  let cdmsSubmissionFormService: CdmsSubmissionFormService;
  let cdmsSubmissionService: CdmsSubmissionService;

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

    fixture = TestBed.createComponent(CdmsSubmissionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cdmsSubmissionFormService = TestBed.inject(CdmsSubmissionFormService);
    cdmsSubmissionService = TestBed.inject(CdmsSubmissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const cdmsSubmission: ICdmsSubmission = { id: 21337 };

      activatedRoute.data = of({ cdmsSubmission });
      comp.ngOnInit();

      expect(comp.cdmsSubmission).toEqual(cdmsSubmission);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICdmsSubmission>>();
      const cdmsSubmission = { id: 7713 };
      jest.spyOn(cdmsSubmissionFormService, 'getCdmsSubmission').mockReturnValue(cdmsSubmission);
      jest.spyOn(cdmsSubmissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cdmsSubmission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cdmsSubmission }));
      saveSubject.complete();

      // THEN
      expect(cdmsSubmissionFormService.getCdmsSubmission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cdmsSubmissionService.update).toHaveBeenCalledWith(expect.objectContaining(cdmsSubmission));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICdmsSubmission>>();
      const cdmsSubmission = { id: 7713 };
      jest.spyOn(cdmsSubmissionFormService, 'getCdmsSubmission').mockReturnValue({ id: null });
      jest.spyOn(cdmsSubmissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cdmsSubmission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cdmsSubmission }));
      saveSubject.complete();

      // THEN
      expect(cdmsSubmissionFormService.getCdmsSubmission).toHaveBeenCalled();
      expect(cdmsSubmissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICdmsSubmission>>();
      const cdmsSubmission = { id: 7713 };
      jest.spyOn(cdmsSubmissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cdmsSubmission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cdmsSubmissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
