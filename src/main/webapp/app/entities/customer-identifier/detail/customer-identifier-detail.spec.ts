import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CustomerIdentifierDetail } from './customer-identifier-detail';

describe('CustomerIdentifier Management Detail Component', () => {
  let comp: CustomerIdentifierDetail;
  let fixture: ComponentFixture<CustomerIdentifierDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./customer-identifier-detail').then(m => m.CustomerIdentifierDetail),
              resolve: { customerIdentifier: () => of({ id: 29355 }) },
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
    fixture = TestBed.createComponent(CustomerIdentifierDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load customerIdentifier on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CustomerIdentifierDetail);

      // THEN
      expect(instance.customerIdentifier()).toEqual(expect.objectContaining({ id: 29355 }));
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
