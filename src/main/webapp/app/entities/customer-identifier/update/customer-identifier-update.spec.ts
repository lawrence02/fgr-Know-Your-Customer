import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ICustomerIdentifier } from '../customer-identifier.model';
import { CustomerIdentifierService } from '../service/customer-identifier.service';

import { CustomerIdentifierFormService } from './customer-identifier-form.service';
import { CustomerIdentifierUpdate } from './customer-identifier-update';

describe('CustomerIdentifier Management Update Component', () => {
  let comp: CustomerIdentifierUpdate;
  let fixture: ComponentFixture<CustomerIdentifierUpdate>;
  let activatedRoute: ActivatedRoute;
  let customerIdentifierFormService: CustomerIdentifierFormService;
  let customerIdentifierService: CustomerIdentifierService;
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

    fixture = TestBed.createComponent(CustomerIdentifierUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerIdentifierFormService = TestBed.inject(CustomerIdentifierFormService);
    customerIdentifierService = TestBed.inject(CustomerIdentifierService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Customer query and add missing value', () => {
      const customerIdentifier: ICustomerIdentifier = { id: 10258 };
      const customer: ICustomer = { id: 26915 };
      customerIdentifier.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerIdentifier });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.customersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const customerIdentifier: ICustomerIdentifier = { id: 10258 };
      const customer: ICustomer = { id: 26915 };
      customerIdentifier.customer = customer;

      activatedRoute.data = of({ customerIdentifier });
      comp.ngOnInit();

      expect(comp.customersSharedCollection()).toContainEqual(customer);
      expect(comp.customerIdentifier).toEqual(customerIdentifier);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerIdentifier>>();
      const customerIdentifier = { id: 29355 };
      jest.spyOn(customerIdentifierFormService, 'getCustomerIdentifier').mockReturnValue(customerIdentifier);
      jest.spyOn(customerIdentifierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerIdentifier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerIdentifier }));
      saveSubject.complete();

      // THEN
      expect(customerIdentifierFormService.getCustomerIdentifier).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerIdentifierService.update).toHaveBeenCalledWith(expect.objectContaining(customerIdentifier));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerIdentifier>>();
      const customerIdentifier = { id: 29355 };
      jest.spyOn(customerIdentifierFormService, 'getCustomerIdentifier').mockReturnValue({ id: null });
      jest.spyOn(customerIdentifierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerIdentifier: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerIdentifier }));
      saveSubject.complete();

      // THEN
      expect(customerIdentifierFormService.getCustomerIdentifier).toHaveBeenCalled();
      expect(customerIdentifierService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerIdentifier>>();
      const customerIdentifier = { id: 29355 };
      jest.spyOn(customerIdentifierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerIdentifier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerIdentifierService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
