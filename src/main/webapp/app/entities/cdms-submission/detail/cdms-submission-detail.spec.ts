import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CdmsSubmissionDetail } from './cdms-submission-detail';

describe('CdmsSubmission Management Detail Component', () => {
  let comp: CdmsSubmissionDetail;
  let fixture: ComponentFixture<CdmsSubmissionDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cdms-submission-detail').then(m => m.CdmsSubmissionDetail),
              resolve: { cdmsSubmission: () => of({ id: 7713 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CdmsSubmissionDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load cdmsSubmission on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CdmsSubmissionDetail);

      // THEN
      expect(instance.cdmsSubmission()).toEqual(expect.objectContaining({ id: 7713 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
