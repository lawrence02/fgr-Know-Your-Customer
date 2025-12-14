import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustomerIdentifier } from '../customer-identifier.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../customer-identifier.test-samples';

import { CustomerIdentifierService, RestCustomerIdentifier } from './customer-identifier.service';

const requireRestSample: RestCustomerIdentifier = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  verifiedAt: sampleWithRequiredData.verifiedAt?.toJSON(),
};

describe('CustomerIdentifier Service', () => {
  let service: CustomerIdentifierService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerIdentifier | ICustomerIdentifier[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerIdentifierService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CustomerIdentifier', () => {
      const customerIdentifier = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerIdentifier).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerIdentifier', () => {
      const customerIdentifier = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerIdentifier).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerIdentifier', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerIdentifier', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerIdentifier', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerIdentifierToCollectionIfMissing', () => {
      it('should add a CustomerIdentifier to an empty array', () => {
        const customerIdentifier: ICustomerIdentifier = sampleWithRequiredData;
        expectedResult = service.addCustomerIdentifierToCollectionIfMissing([], customerIdentifier);
        expect(expectedResult).toEqual([customerIdentifier]);
      });

      it('should not add a CustomerIdentifier to an array that contains it', () => {
        const customerIdentifier: ICustomerIdentifier = sampleWithRequiredData;
        const customerIdentifierCollection: ICustomerIdentifier[] = [
          {
            ...customerIdentifier,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerIdentifierToCollectionIfMissing(customerIdentifierCollection, customerIdentifier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerIdentifier to an array that doesn't contain it", () => {
        const customerIdentifier: ICustomerIdentifier = sampleWithRequiredData;
        const customerIdentifierCollection: ICustomerIdentifier[] = [sampleWithPartialData];
        expectedResult = service.addCustomerIdentifierToCollectionIfMissing(customerIdentifierCollection, customerIdentifier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerIdentifier);
      });

      it('should add only unique CustomerIdentifier to an array', () => {
        const customerIdentifierArray: ICustomerIdentifier[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerIdentifierCollection: ICustomerIdentifier[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerIdentifierToCollectionIfMissing(customerIdentifierCollection, ...customerIdentifierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerIdentifier: ICustomerIdentifier = sampleWithRequiredData;
        const customerIdentifier2: ICustomerIdentifier = sampleWithPartialData;
        expectedResult = service.addCustomerIdentifierToCollectionIfMissing([], customerIdentifier, customerIdentifier2);
        expect(expectedResult).toEqual([customerIdentifier, customerIdentifier2]);
      });

      it('should accept null and undefined values', () => {
        const customerIdentifier: ICustomerIdentifier = sampleWithRequiredData;
        expectedResult = service.addCustomerIdentifierToCollectionIfMissing([], null, customerIdentifier, undefined);
        expect(expectedResult).toEqual([customerIdentifier]);
      });

      it('should return initial array if no CustomerIdentifier is added', () => {
        const customerIdentifierCollection: ICustomerIdentifier[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerIdentifierToCollectionIfMissing(customerIdentifierCollection, undefined, null);
        expect(expectedResult).toEqual(customerIdentifierCollection);
      });
    });

    describe('compareCustomerIdentifier', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerIdentifier(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29355 };
        const entity2 = null;

        const compareResult1 = service.compareCustomerIdentifier(entity1, entity2);
        const compareResult2 = service.compareCustomerIdentifier(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29355 };
        const entity2 = { id: 10258 };

        const compareResult1 = service.compareCustomerIdentifier(entity1, entity2);
        const compareResult2 = service.compareCustomerIdentifier(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29355 };
        const entity2 = { id: 29355 };

        const compareResult1 = service.compareCustomerIdentifier(entity1, entity2);
        const compareResult2 = service.compareCustomerIdentifier(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
